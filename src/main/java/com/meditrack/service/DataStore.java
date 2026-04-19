package com.meditrack.service;

import com.meditrack.model.appointment.Appointment;
import com.meditrack.model.person.*;
import java.util.*;

public class DataStore {
    private final Map<String, Patient>      patients     = new HashMap<>();
    private final Map<String, MedicalStaff> staff        = new HashMap<>();
    private final Map<String, Appointment>  appointments = new HashMap<>();

    public void savePatient(Patient p)               { patients.put(p.getId(), p); }
    public Patient findPatient(String id)            { return patients.get(id); }
    public Collection<Patient> allPatients()         { return patients.values(); }
    public boolean patientExists(String id)          { return patients.containsKey(id); }

    public void saveStaff(MedicalStaff s)            { staff.put(s.getId(), s); }
    public MedicalStaff findStaff(String id)         { return staff.get(id); }
    public Collection<MedicalStaff> allStaff()       { return staff.values(); }
    public boolean staffExists(String id)            { return staff.containsKey(id); }

    public void saveAppointment(Appointment a)       { appointments.put(a.getId(), a); }
    public Appointment findAppointment(String id)    { return appointments.get(id); }
    public Collection<Appointment> allAppointments() { return appointments.values(); }
}