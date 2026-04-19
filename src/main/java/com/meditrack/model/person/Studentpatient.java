package com.meditrack.model.person;

public class StudentPatient extends Patient {
    private String studentId;
    private String programme;
    private int yearOfStudy;

    public StudentPatient(String id, String name, String email,
                          String phone, String studentId,
                          String programme, int yearOfStudy) {
        super(id, name, email, phone);
        this.studentId   = studentId;
        this.programme   = programme;
        this.yearOfStudy = yearOfStudy;
    }

    @Override
    public String getSummary() {
        return "Student Patient: " + getName()
             + " | Programme: " + programme
             + " | Year: " + yearOfStudy
             + " | Student ID: " + studentId;
    }

    public String getStudentId()  { return studentId; }
    public String getProgramme()  { return programme; }
    public int getYearOfStudy()   { return yearOfStudy; }
}
