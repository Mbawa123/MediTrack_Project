package com.meditrack.model.person;

public class Doctor extends MedicalStaff {
    private String licenceNumber;

    public Doctor(String id, String name, String email, String phone,
                  String staffId, String department, String licenceNumber) {
        super(id, name, email, phone, staffId, department);
        this.licenceNumber = licenceNumber;
    }

    @Override public boolean canPrescribe()    { return true; }
    @Override public boolean canUpdateVitals() { return true; }
    @Override public String  getRole()         { return "Doctor"; }

    public String getLicenceNumber() { return licenceNumber; }
}
