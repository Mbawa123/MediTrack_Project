package com.meditrack.model.person;

import com.meditrack.model.record.MedicalRecord;
import com.meditrack.model.appointment.Appointment;
import java.util.ArrayList;
import java.util.List;

public abstract class Patient extends Person {
    private MedicalRecord medicalRecord;
    private List<Appointment> appointments;

    public Patient(String id, String name, String email, String phone) {
        super(id, name, email, phone);
        this.medicalRecord = new MedicalRecord(id);
        this.appointments  = new ArrayList<>();
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    @Override
    public abstract String getSummary();

    public MedicalRecord getMedicalRecord() { return medicalRecord; }
    public List<Appointment> getAppointments() { return appointments; }
}
