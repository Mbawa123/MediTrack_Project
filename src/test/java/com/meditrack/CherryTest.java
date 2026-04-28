package com.meditrack;

import com.meditrack.exception.*;
import com.meditrack.model.person.*;
import com.meditrack.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CherryTest {

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
        patientService.registerStudent("p2","Bob","bob@zut.ac.zm","096","S002","IT",1);
        patientService.registerStudent("p3","Carol","carol@zut.ac.zm","095","S003","ENG",3);
        staffService.registerDoctor("d1","Dr Chanda","c@zut.ac.zm","095","ST001","General","LIC001");
        staffService.registerNurse("n1","Nurse Phiri","p@zut.ac.zm","094","ST002","General","NR001");
    }

    // ═══════════════ CHERRY 1 — WAITING ROOM ═══════════════

    // TEST 21 — Normal patient added to waiting room
    @Test
    void testNormalPatientAddedToWaitingRoom() {
        appointmentService.addToWaitingRoom("p1", "NORMAL", "Headache");
        assertEquals(1, store.getWaitingRoom().size());
    }

    // TEST 22 — Urgent patient jumps to front of queue
    @Test
    void testUrgentPatientJumsToFront() {
        appointmentService.addToWaitingRoom("p1", "NORMAL", "Headache");
        appointmentService.addToWaitingRoom("p2", "NORMAL", "Fever");
        appointmentService.addToWaitingRoom("p3", "URGENT", "Chest pain");
        // p3 should be first despite being added last
        WaitingRoom.WaitingPatient next = store.getWaitingRoom().nextPatient();
        assertEquals("p3", next.getPatient().getId());
    }

    // TEST 23 — Normal patients follow FIFO order
    @Test
    void testNormalPatientsFollowFIFO() {
        appointmentService.addToWaitingRoom("p1", "NORMAL", "Headache");
        appointmentService.addToWaitingRoom("p2", "NORMAL", "Fever");
        WaitingRoom.WaitingPatient next = store.getWaitingRoom().nextPatient();
        assertEquals("p1", next.getPatient().getId()); // p1 was first
    }

    // TEST 24 — Empty waiting room throws exception
    @Test
    void testEmptyWaitingRoomThrows() {
        assertThrows(NotFoundException.class, () ->
                appointmentService.callNextPatient());
    }

    // TEST 25 — Invalid priority throws ValidationException
    @Test
    void testInvalidPriorityThrows() {
        assertThrows(ValidationException.class, () ->
                appointmentService.addToWaitingRoom("p1", "VIP", "Headache"));
    }

    // ═══════════════ CHERRY 2 — PRESCRIPTION CONFLICT ═══════════════

    // TEST 26 — Safe prescription is saved successfully
    @Test
    void testSafePrescriptionIsSaved() {
        appointmentService.addPrescription("p1", "d1", "amoxicillin", "500mg");
        assertEquals(1,
                patientService.getPatient("p1").getMedicalRecord().getPrescriptions().size());
    }

    // TEST 27 — Conflicting prescription is rejected
    @Test
    void testConflictingPrescriptionIsRejected() {
        appointmentService.addPrescription("p1", "d1", "warfarin", "5mg");
        assertThrows(ValidationException.class, () ->
                appointmentService.addPrescription("p1", "d1", "aspirin", "100mg"));
    }

    // TEST 28 — Conflict works in reverse direction too
    @Test
    void testReverseConflictIsAlsoDetected() {
        appointmentService.addPrescription("p1", "d1", "aspirin", "100mg");
        assertThrows(ValidationException.class, () ->
                appointmentService.addPrescription("p1", "d1", "warfarin", "5mg"));
    }

    // ═══════════════ CHERRY 3 — ROLE-BASED WITHOUT IF/ELSE ═══════════════

    // TEST 29 — Doctor permissions are correct (no instanceof check)
    @Test
    void testDoctorPermissionsViaInterface() {
        MedicalStaff staff = staffService.getStaff("d1");
        // We call canPrescribe() on MedicalStaff reference — no type check
        assertTrue(staff.canPrescribe());
        assertTrue(staff.canUpdateVitals());
        assertEquals("Doctor", staff.getRole());
    }

    // TEST 30 — Nurse permissions are correct (no instanceof check)
    @Test
    void testNursePermissionsViaInterface() {
        MedicalStaff staff = staffService.getStaff("n1");
        // Same method call, different result — this IS polymorphismmvn
        assertFalse(staff.canPrescribe());
        assertTrue(staff.canUpdateVitals());
        assertEquals("Nurse", staff.getRole());
    }
}