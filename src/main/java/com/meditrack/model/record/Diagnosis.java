package com.meditrack.model.record;

import java.time.LocalDate;
import java.util.UUID;

public class Diagnosis {
    private String id;
    private String description;
    private String doctorId;
    private LocalDate date;

    public Diagnosis(String description, String doctorId) {
        this.id          = UUID.randomUUID().toString();
        this.description = description;
        this.doctorId    = doctorId;
        this.date        = LocalDate.now();
    }

    public String getId()          { return id; }
    public String getDescription() { return description; }
    public String getDoctorId()    { return doctorId; }
    public LocalDate getDate()     { return date; }
}
