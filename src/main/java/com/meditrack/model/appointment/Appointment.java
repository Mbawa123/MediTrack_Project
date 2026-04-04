package com.meditrack.model.appointment;

import java.time.LocalDateTime;
import java.util.UUID;

public class Appointment {
    public enum Status { SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED }

    private String id;
    private String patientId;
    private String staffId;
    private LocalDateTime dateTime;
    private Status status;

    public Appointment(String patientId, String staffId, LocalDateTime dateTime) {
        this.id        = UUID.randomUUID().toString();
        this.patientId = patientId;
        this.staffId   = staffId;
        this.dateTime  = dateTime;
        this.status    = Status.SCHEDULED;
    }

    public void setStatus(Status status) {
        if (status == null) throw new IllegalArgumentException("Status cannot be null");
        this.status = status;
    }

    public String getId()              { return id; }
    public String getPatientId()       { return patientId; }
    public String getStaffId()         { return staffId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public Status getStatus()          { return status; }
}
