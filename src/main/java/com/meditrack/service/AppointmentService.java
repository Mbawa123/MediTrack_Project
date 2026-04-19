package com.meditrack.service;

import com.meditrack.exception.*;
import com.meditrack.model.appointment.Appointment;
import com.meditrack.model.appointment.Appointment.Status;
import com.meditrack.model.person.*;
import com.meditrack.model.record.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    // ABSTRACTION: canPrescribe() from StaffAction interface
    // Doctor = true, Nurse = false — no instanceof needed
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

    public Prescription addPrescription(String patientId, String staffId,
                                        String medication, String dosage) {
        Patient patient = patientService.getPatient(patientId);
        MedicalStaff staff = staffService.getStaff(staffId);
        if (!staff.canPrescribe())
            throw new ForbiddenException(
                    staff.getRole() + " cannot write prescriptions. Doctor required.");
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
}