package com.meditrack.service;

import com.meditrack.exception.*;
import com.meditrack.model.person.*;
import java.util.Collection;

public class PatientService {
    private final DataStore store;

    public PatientService(DataStore store) { this.store = store; }

    public StudentPatient registerStudent(String id, String name, String email,
                                          String phone, String studentId,
                                          String programme, int year) {
        if (store.patientExists(id))
            throw new ValidationException("Patient ID already exists: " + id);
        StudentPatient p = new StudentPatient(id, name, email, phone,
                studentId, programme, year);
        store.savePatient(p);
        return p;
    }

    public StaffPatient registerStaff(String id, String name, String email,
                                      String phone, String employeeId,
                                      String department, int yearsOfService) {
        if (store.patientExists(id))
            throw new ValidationException("Patient ID already exists: " + id);
        StaffPatient p = new StaffPatient(id, name, email, phone,
                employeeId, department, yearsOfService);
        store.savePatient(p);
        return p;
    }

    public Patient getPatient(String id) {
        Patient p = store.findPatient(id);
        if (p == null) throw new NotFoundException("Patient not found: " + id);
        return p;
    }

    // POLYMORPHISM: no type check — Java picks getSummary() at runtime
    public String getPatientSummary(String id) {
        return getPatient(id).getSummary();
    }

    public Collection<Patient> getAllPatients() { return store.allPatients(); }
}