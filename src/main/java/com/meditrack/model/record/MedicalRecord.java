package com.meditrack.model.record;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecord {

    private String patientId;
    private List<Diagnosis>    diagnoses;
    private List<Prescription> prescriptions;
    private Vitals vitals; // can be null until first measurement

    public MedicalRecord(String patientId) {
        this.patientId     = patientId;
        this.diagnoses     = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
    }

    public void addDiagnosis(Diagnosis d)       { diagnoses.add(d); }
    public void addPrescription(Prescription p) { prescriptions.add(p); }
    public void updateVitals(Vitals v)          { this.vitals = v; }

    public String getPatientId()              { return patientId; }
    public List<Diagnosis> getDiagnoses()     { return diagnoses; }
    public List<Prescription> getPrescriptions() { return prescriptions; }
    public Vitals getVitals()                 { return vitals; }
}