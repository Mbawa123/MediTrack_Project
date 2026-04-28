package com.meditrack;

import com.meditrack.api.MediTrackApi;
import com.meditrack.service.*;

public class Main {
    public static void main(String[] args) {
        DataStore          store = new DataStore();
        PatientService     ps    = new PatientService(store);
        StaffService       ss    = new StaffService(store);
        AppointmentService as    = new AppointmentService(store, ps, ss);
        MediTrackApi api = new MediTrackApi(ps, ss, as, store);
        api.start(7070);
        System.out.println("MediTrack running on http://localhost:7070");
    }
}