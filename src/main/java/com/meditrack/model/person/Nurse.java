package com.meditrack.model.person;

public class Nurse extends MedicalStaff {
    private String nurseId;

    public Nurse(String id, String name, String email, String phone,
                 String staffId, String department, String nurseId) {
        super(id, name, email, phone, staffId, department);
        this.nurseId = nurseId;
    }

    @Override public boolean canPrescribe()    { return false; }
    @Override public boolean canUpdateVitals() { return true; }
    @Override public String  getRole()         { return "Nurse"; }

    public String getNurseId() { return nurseId; }
}
