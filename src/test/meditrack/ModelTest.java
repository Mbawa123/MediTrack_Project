package com.meditrack;

import com.meditrack.model.appointment.Appointment;
import com.meditrack.model.person.*;
import com.meditrack.model.record.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

    // Helper methods to avoid repetition in tests
    StudentPatient makeStudent() {
        return new StudentPatient("p1","Alice Banda","alice@zut.ac.zm",
                "0977123456","S001","Computer Science",2);
    }
    StaffPatient makeStaff() {
        return new StaffPatient("p2","Bob Mwale","bob@zut.ac.zm",
                "0966111222","E001","Finance",5);
    }
    Doctor makeDoctor() {
        return new Doctor("d1","Dr Chanda","chanda@zut.ac.zm",
                "0955000001","ST001","General","LIC001");
    }
    Nurse makeNurse() {
        return new Nurse("n1","Nurse Phiri","phiri@zut.ac.zm",
                "0944000001","ST002","General","NR001");
    }

    // TEST 1 — Encapsulation: name cannot be blank
    @Test
    void testPersonNameCannotBeBlank() {
        StudentPatient p = makeStudent();
        assertThrows(IllegalArgumentException.class, () -> p.setName(""));
    }

    // TEST 2 — Encapsulation: email must contain @
    @Test
    void testPersonEmailValidation() {
        StudentPatient p = makeStudent();
        assertThrows(IllegalArgumentException.class, () -> p.setEmail("notanemail"));
    }

    // TEST 3 — Inheritance: StudentPatient has fields from Person
    @Test
    void testStudentInheritsFromPerson() {
        StudentPatient p = makeStudent();
        assertEquals("Alice Banda", p.getName());  // from Person
        assertEquals("S001", p.getStudentId());    // from StudentPatient
    }

    // TEST 4 — Polymorphism: student summary contains programme
    @Test
    void testStudentSummaryContainsProgramme() {
        StudentPatient p = makeStudent();
        assertTrue(p.getSummary().contains("Computer Science"));
    }

    // TEST 5 — Polymorphism: staff summary contains department
    @Test
    void testStaffSummaryContainsDepartment() {
        StaffPatient p = makeStaff();
        assertTrue(p.getSummary().contains("Finance"));
    }

    // TEST 6 — Polymorphism: summaries are DIFFERENT for same method call
    @Test
    void testPolymorphismProducesDifferentSummaries() {
        Patient student = makeStudent();
        Patient staff   = makeStaff();
        assertNotEquals(student.getSummary(), staff.getSummary());
    }

    // TEST 7 — Abstraction: Doctor canPrescribe = true
    @Test
    void testDoctorCanPrescribe() {
        Doctor d = makeDoctor();
        assertTrue(d.canPrescribe());
    }

    // TEST 8 — Abstraction: Nurse canPrescribe = false
    @Test
    void testNurseCannotPrescribe() {
        Nurse n = makeNurse();
        assertFalse(n.canPrescribe());
    }

    // TEST 9 — Appointment starts as SCHEDULED
    @Test
    void testAppointmentDefaultStatus() {
        Appointment a = new Appointment("p1","d1", LocalDateTime.now());
        assertEquals(Appointment.Status.SCHEDULED, a.getStatus());
    }

    // TEST 10 — Vitals rejects impossible temperature
    @Test
    void testVitalsTemperatureValidation() {
        Vitals v = new Vitals("120/80", 36.5, 70.0);
        assertThrows(IllegalArgumentException.class, () -> v.setTemperature(99.0));
    }
}