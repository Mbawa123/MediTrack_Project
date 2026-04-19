package com.meditrack.service;

import com.meditrack.exception.*;
import com.meditrack.model.person.*;
import java.util.Collection;

public class StaffService {
    private final DataStore store;

    public StaffService(DataStore store) { this.store = store; }

    public Doctor registerDoctor(String id, String name, String email,
                                 String phone, String staffId,
                                 String department, String licenceNumber) {
        if (store.staffExists(id))
            throw new ValidationException("Staff ID already exists: " + id);
        Doctor d = new Doctor(id, name, email, phone, staffId, department, licenceNumber);
        store.saveStaff(d);
        return d;
    }

    public Nurse registerNurse(String id, String name, String email,
                               String phone, String staffId,
                               String department, String nurseId) {
        if (store.staffExists(id))
            throw new ValidationException("Staff ID already exists: " + id);
        Nurse n = new Nurse(id, name, email, phone, staffId, department, nurseId);
        store.saveStaff(n);
        return n;
    }

    public MedicalStaff getStaff(String id) {
        MedicalStaff s = store.findStaff(id);
        if (s == null) throw new NotFoundException("Staff not found: " + id);
        return s;
    }

    public Collection<MedicalStaff> getAllStaff() { return store.allStaff(); }
}
