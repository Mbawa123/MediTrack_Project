package com.meditrack.model.person;

public interface StaffAction {
    boolean canPrescribe();
    boolean canUpdateVitals();
    String getRole();
}
