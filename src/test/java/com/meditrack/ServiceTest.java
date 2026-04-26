package com.meditrack;

import com.meditrack.exception.*;
import com.meditrack.model.appointment.Appointment;
import com.meditrack.model.person.*;
import com.meditrack.model.record.*;
import com.meditrack.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {

    DataStore store;
    PatientService patientService;
    StaffService staffService;
    AppointmentService appointmentService;

    @BeforeEach
    void setup() {
        store              = new DataStore();
        patientService     = new PatientService(store);
        staffService       = new StaffService(store);
        appointmentService = new AppointmentService(store, patientService, staffService);
        patientService.registerStudent("p1","Alice","alice@zut.ac.zm","097","S001","CS",2);
        patientService.registerStaff("p2","Bob","bob@zut.ac.zm","096","E001","Finance",5);
        staffService.registerDoctor("d1","Dr Chanda","c@zut.ac.zm","095","ST001","General","LIC001");
        staffService.registerNurse("n1","Nurse phiri","p@zut.ac.zm","094","ST002","General","NR001");
    }

    @Test
    void testDuplicatePatientIdThrows() {
        assertThrows(ValidationException.class, () ->
                patientService.registerStudent("p1","X","x@x.com","000","S002","IT",1));
    }

    @Test
    void testPatientNotFoundThrows() {
        assertThrows(NotFoundException.class, () -> patientService.getPatient("GHOST"));
    }

    @Test
    void testDoctorRetrievedCorrectly() {
        MedicalStaff s = staffService.getStaff("d1");
        assertEquals("Doctor", s.getRole());
    }

    @Test
    void testAppointmentBookedSuccessfully() {
        Appointment a = appointmentService.bookAppointment("p1","d1",
                LocalDateTime.of(2026,6,1,9,0));
        assertNotNull(a.getId());
        assertEquals(Appointment.Status.SCHEDULED, a.getStatus());
    }

    @Test
    void testDoubleBookingRejected() {
        appointmentService.bookAppointment("p1","d1",LocalDateTime.of(2026,6,1,9,0));
        assertThrows(ValidationException.class, () ->
                appointmentService.bookAppointment("p1","d1",LocalDateTime.of(2026,6,1,14,0)));
    }

    @Test
    void testAppointmentStatusUpdate() {
        Appointment a = appointmentService.bookAppointment("p1","d1",
                LocalDateTime.of(2026,6,2,9,0));
        appointmentService.updateStatus(a.getId(), "IN_PROGRESS");
        assertEquals(Appointment.Status.IN_PROGRESS, a.getStatus());
    }

    @Test
    void testNurseCannotAddDiagnosis() {
        assertThrows(ForbiddenException.class, () ->
                appointmentService.addDiagnosis("p1","n1","Some diagnosis"));
    }

    @Test
    void testDoctorCanAddDiagnosis() {
        Diagnosis d = appointmentService.addDiagnosis("p1","d1","Mild fever");
        assertNotNull(d.getId());
        assertEquals("Mild fever", d.getDescription());
    }

    @Test
    void testNurseCannotWritePrescription() {
        assertThrows(ForbiddenException.class, () ->
                appointmentService.addPrescription("p1","n1","Panadol","500mg"));
    }

    @Test
    void testVitalsSavedToRecord() {
        appointmentService.updateVitals("p1","d1","120/80",36.6,65.0);
        Vitals v = patientService.getPatient("p1").getMedicalRecord().getVitals();
        assertNotNull(v);
        assertEquals("120/80", v.getBloodPressure());
    }
}
