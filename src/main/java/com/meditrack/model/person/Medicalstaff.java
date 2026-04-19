package com.meditrack.model.person;

public abstract class MedicalStaff extends Person implements StaffAction {
    private String staffId;
    private String department;

    public MedicalStaff(String id, String name, String email,
                        String phone, String staffId, String department) {
        super(id, name, email, phone);
        this.staffId    = staffId;
        this.department = department;
    }

    @Override
    public String getSummary() {
        return getRole() + ": " + getName() + " | Dept: " + department;
    }

    public String getStaffId()    { return staffId; }
    public String getDepartment() { return department; }
}
