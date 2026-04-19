package com.meditrack.model.person;

public class StaffPatient extends Patient {
    private String employeeId;
    private String department;
    private int yearsOfService;

    public StaffPatient(String id, String name, String email,
                        String phone, String employeeId,
                        String department, int yearsOfService) {
        super(id, name, email, phone);
        this.employeeId     = employeeId;
        this.department     = department;
        this.yearsOfService = yearsOfService;
    }

    @Override
    public String getSummary() {
        return "Staff Patient: " + getName()
             + " | Department: " + department
             + " | Years of service: " + yearsOfService
             + " | Employee ID: " + employeeId;
    }

    public String getEmployeeId()   { return employeeId; }
    public String getDepartment()   { return department; }
    public int getYearsOfService()  { return yearsOfService; }
}
