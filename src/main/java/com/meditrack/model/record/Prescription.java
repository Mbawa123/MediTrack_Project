package com.meditrack.model.record;

import java.time.LocalDate;
import java.util.UUID;

public class Prescription {
    private String id;
    private String medication;
    private String dosage;
    private String doctorId;
    private LocalDate date;

    public Prescription(String medication, String dosage, String doctorId) {
        this.id         = UUID.randomUUID().toString();
        this.medication = medication;
        this.dosage     = dosage;
        this.doctorId   = doctorId;
        this.date       = LocalDate.now();
    }

    public String getId()         { return id; }
    public String getMedication() { return medication; }
    public String getDosage()     { return dosage; }
    public String getDoctorId()   { return doctorId; }
    public LocalDate getDate()    { return date; }
}
