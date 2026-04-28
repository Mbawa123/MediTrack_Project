package com.meditrack.service;

import com.meditrack.exception.*;
import com.meditrack.model.appointment.Appointment;
import com.meditrack.model.appointment.Appointment.Status;
import com.meditrack.model.person.*;
import com.meditrack.model.record.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AppointmentService {
    private final DataStore store;
    private final PatientService patientService;
    private final StaffService staffService;

    public AppointmentService(DataStore store,
                              PatientService patientService,
                              StaffService staffService) {
        this.store = store;
        this.patientService = patientService;
        this.staffService = staffService;
    }

    public Appointment bookAppointment(String patientId, String staffId,
                                       LocalDateTime dateTime) {
        Patient patient = patientService.getPatient(patientId);
        staffService.getStaff(staffId);

        LocalDate date = dateTime.toLocalDate();
        boolean conflict = store.allAppointments().stream().anyMatch(a ->
                a.getPatientId().equals(patientId) &&
                        a.getStaffId().equals(staffId) &&
                        a.getDateTime().toLocalDate().equals(date) &&
                        a.getStatus() != Status.CANCELLED
        );
        if (conflict)
            throw new ValidationException(
                    "Patient already has an appointment with this staff on " + date);

        Appointment a = new Appointment(patientId, staffId, dateTime);
        store.saveAppointment(a);
        patient.addAppointment(a);
        return a;
    }

    public Appointment updateStatus(String appointmentId, String statusStr) {
        Appointment a = store.findAppointment(appointmentId);
        if (a == null)
            throw new NotFoundException("Appointment not found: " + appointmentId);
        try {
            a.setStatus(Status.valueOf(statusStr.toUpperCase()));
            return a;
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid status: " + statusStr);
        }
    }

    // CHERRY 3 — No instanceof, no if/else type checks
    // canPrescribe() is defined in StaffAction interface
    // Doctor returns true, Nurse returns false — the OBJECT decides
    public Diagnosis addDiagnosis(String patientId, String staffId,
                                  String description) {
        Patient patient = patientService.getPatient(patientId);
        MedicalStaff staff = staffService.getStaff(staffId);
        if (!staff.canPrescribe())
            throw new ForbiddenException(
                    staff.getRole() + " cannot add diagnoses. Doctor required.");
        Diagnosis d = new Diagnosis(description, staffId);
        patient.getMedicalRecord().addDiagnosis(d);
        return d;
    }

    // CHERRY 2 — Prescription Conflict Check
    // Before saving, we check if new medication conflicts with existing ones
    public Prescription addPrescription(String patientId, String staffId,
                                        String medication, String dosage) {
        Patient patient = patientService.getPatient(patientId);
        MedicalStaff staff = staffService.getStaff(staffId);

        // CHERRY 3 — canPrescribe() from interface, no instanceof
        if (!staff.canPrescribe())
            throw new ForbiddenException(
                    staff.getRole() + " cannot write prescriptions. Doctor required.");

        // CHERRY 2 — Check for drug conflicts before saving
        List<String> existingMeds = patient.getMedicalRecord()
                .getPrescriptions().stream()
                .map(Prescription::getMedication)
                .collect(Collectors.toList());

        String conflict = store.getConflictChecker()
                .checkConflict(medication, existingMeds);

        if (conflict != null)
            throw new ValidationException(
                    "Prescription conflict detected: " + medication +
                            " conflicts with existing medication: " + conflict +
                            ". Prescription not saved.");

        Prescription p = new Prescription(medication, dosage, staffId);
        patient.getMedicalRecord().addPrescription(p);
        return p;
    }

    public Vitals updateVitals(String patientId, String staffId,
                               String bloodPressure, double temperature,
                               double weight) {
        Patient patient = patientService.getPatient(patientId);
        MedicalStaff staff = staffService.getStaff(staffId);
        if (!staff.canUpdateVitals())
            throw new ForbiddenException(staff.getRole() + " cannot update vitals.");
        Vitals v = new Vitals(bloodPressure, temperature, weight);
        patient.getMedicalRecord().updateVitals(v);
        return v;
    }

    public List<Appointment> getPatientAppointments(String patientId) {
        patientService.getPatient(patientId);
        return store.allAppointments().stream()
                .filter(a -> a.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public Appointment getAppointment(String id) {
        Appointment a = store.findAppointment(id);
        if (a == null) throw new NotFoundException("Appointment not found: " + id);
        return a;
    }

    // ═══════════════════════════════════════
    // CHERRY 1 — Waiting Room methods
    // ═══════════════════════════════════════
    public void addToWaitingRoom(String patientId, String priorityStr, String reason) {
        Patient patient = patientService.getPatient(patientId);
        WaitingRoom.Priority priority;
        try {
            priority = WaitingRoom.Priority.valueOf(priorityStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Priority must be NORMAL or URGENT");
        }
        store.getWaitingRoom().addPatient(patient, priority, reason);
    }

    public WaitingRoom.WaitingPatient callNextPatient() {
        if (store.getWaitingRoom().isEmpty())
            throw new NotFoundException("Waiting room is empty");
        return store.getWaitingRoom().nextPatient();
    }

    public List<Map<String, String>> getWaitingQueue() {
        return store.getWaitingRoom().getQueue().stream()
                .map(wp -> {
                    Map<String, String> m = new LinkedHashMap<>();
                    m.put("patientId", wp.getPatient().getId());
                    m.put("name",      wp.getPatient().getName());
                    m.put("priority",  wp.getPriority().toString());
                    m.put("reason",    wp.getReason());
                    return m;
                })
                .collect(Collectors.toList());
    }
}