package com.meditrack.api;

import com.google.gson.*;
import com.meditrack.exception.*;
import com.meditrack.model.appointment.Appointment;
import com.meditrack.model.person.*;
import com.meditrack.model.record.*;
import com.meditrack.service.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.time.LocalDateTime;
import java.util.Map;

public class MediTrackApi {
    private final Javalin app;
    private final PatientService patientService;
    private final StaffService staffService;
    private final AppointmentService appointmentService;

    public MediTrackApi(PatientService ps, StaffService ss, AppointmentService as) {
        this.patientService     = ps;
        this.staffService       = ss;
        this.appointmentService = as;
        this.app = Javalin.create(config ->
                config.bundledPlugins.enableCors(cors -> cors.addRule(it -> it.anyHost()))
        );
        registerRoutes();
        registerExceptionHandlers();
    }

    private void registerRoutes() {
        app.post("/patients/register",                this::registerPatient);
        app.get("/patients/{id}",                     this::getPatient);
        app.get("/patients/{id}/summary",             this::getPatientSummary);
        app.post("/staff/register",                   this::registerStaff);
        app.get("/staff/{id}",                        this::getStaff);
        app.post("/appointments/book",                this::bookAppointment);
        app.put("/appointments/{id}/status",          this::updateAppointmentStatus);
        app.get("/appointments/patient/{id}",         this::getPatientAppointments);
        app.post("/records/{patientId}/diagnosis",    this::addDiagnosis);
        app.post("/records/{patientId}/prescription", this::addPrescription);
        app.put("/records/{patientId}/vitals",        this::updateVitals);
        app.get("/records/{patientId}",               this::getMedicalRecord);
    }

    private void registerExceptionHandlers() {
        app.exception(NotFoundException.class,   (e, ctx) ->
                ctx.status(404).json(Map.of("error", "NOT_FOUND", "message", e.getMessage())));
        app.exception(ValidationException.class, (e, ctx) ->
                ctx.status(400).json(Map.of("error", "VALIDATION_ERROR", "message", e.getMessage())));
        app.exception(ForbiddenException.class,  (e, ctx) ->
                ctx.status(403).json(Map.of("error", "FORBIDDEN", "message", e.getMessage())));
        app.exception(Exception.class,           (e, ctx) ->
                ctx.status(500).json(Map.of("error", "SERVER_ERROR", "message", e.getMessage())));
    }

    private void registerPatient(Context ctx) {
        JsonObject body = JsonParser.parseString(ctx.body()).getAsJsonObject();
        String type = body.get("type").getAsString();
        if (type.equalsIgnoreCase("student")) {
            StudentPatient p = patientService.registerStudent(
                    body.get("id").getAsString(), body.get("name").getAsString(),
                    body.get("email").getAsString(), body.get("phone").getAsString(),
                    body.get("studentId").getAsString(), body.get("programme").getAsString(),
                    body.get("year").getAsInt());
            ctx.status(201).json(Map.of("message", "Student registered", "id", p.getId()));
        } else {
            StaffPatient p = patientService.registerStaff(
                    body.get("id").getAsString(), body.get("name").getAsString(),
                    body.get("email").getAsString(), body.get("phone").getAsString(),
                    body.get("employeeId").getAsString(), body.get("department").getAsString(),
                    body.get("yearsOfService").getAsInt());
            ctx.status(201).json(Map.of("message", "Staff patient registered", "id", p.getId()));
        }
    }

    private void getPatient(Context ctx) {
        Patient p = patientService.getPatient(ctx.pathParam("id"));
        ctx.json(Map.of("id", p.getId(), "name", p.getName(),
                "type", p.getClass().getSimpleName(),
                "summary", p.getSummary()));
    }

    private void getPatientSummary(Context ctx) {
        ctx.json(Map.of("summary",
                patientService.getPatientSummary(ctx.pathParam("id"))));
    }

    private void registerStaff(Context ctx) {
        JsonObject body = JsonParser.parseString(ctx.body()).getAsJsonObject();
        String type = body.get("type").getAsString();
        if (type.equalsIgnoreCase("doctor")) {
            Doctor d = staffService.registerDoctor(
                    body.get("id").getAsString(), body.get("name").getAsString(),
                    body.get("email").getAsString(), body.get("phone").getAsString(),
                    body.get("staffId").getAsString(), body.get("department").getAsString(),
                    body.get("licenceNumber").getAsString());
            ctx.status(201).json(Map.of("message", "Doctor registered", "id", d.getId()));
        } else {
            Nurse n = staffService.registerNurse(
                    body.get("id").getAsString(), body.get("name").getAsString(),
                    body.get("email").getAsString(), body.get("phone").getAsString(),
                    body.get("staffId").getAsString(), body.get("department").getAsString(),
                    body.get("nurseId").getAsString());
            ctx.status(201).json(Map.of("message", "Nurse registered", "id", n.getId()));
        }
    }

    private void getStaff(Context ctx) {
        MedicalStaff s = staffService.getStaff(ctx.pathParam("id"));
        ctx.json(Map.of("id", s.getId(), "name", s.getName(),
                "role", s.getRole(), "department", s.getDepartment(),
                "canPrescribe", s.canPrescribe()));
    }

    private void bookAppointment(Context ctx) {
        JsonObject body = JsonParser.parseString(ctx.body()).getAsJsonObject();
        Appointment a = appointmentService.bookAppointment(
                body.get("patientId").getAsString(),
                body.get("staffId").getAsString(),
                LocalDateTime.parse(body.get("dateTime").getAsString()));
        ctx.status(201).json(Map.of("message", "Appointment booked",
                "appointmentId", a.getId(), "status", a.getStatus().toString()));
    }

    private void updateAppointmentStatus(Context ctx) {
        JsonObject body = JsonParser.parseString(ctx.body()).getAsJsonObject();
        Appointment a = appointmentService.updateStatus(
                ctx.pathParam("id"), body.get("status").getAsString());
        ctx.json(Map.of("appointmentId", a.getId(),
                "status", a.getStatus().toString()));
    }

    private void getPatientAppointments(Context ctx) {
        ctx.json(appointmentService.getPatientAppointments(ctx.pathParam("id")));
    }

    private void addDiagnosis(Context ctx) {
        JsonObject body = JsonParser.parseString(ctx.body()).getAsJsonObject();
        Diagnosis d = appointmentService.addDiagnosis(
                ctx.pathParam("patientId"),
                body.get("staffId").getAsString(),
                body.get("description").getAsString());
        ctx.status(201).json(Map.of("message", "Diagnosis added",
                "diagnosisId", d.getId()));
    }

    private void addPrescription(Context ctx) {
        JsonObject body = JsonParser.parseString(ctx.body()).getAsJsonObject();
        Prescription p = appointmentService.addPrescription(
                ctx.pathParam("patientId"),
                body.get("staffId").getAsString(),
                body.get("medication").getAsString(),
                body.get("dosage").getAsString());
        ctx.status(201).json(Map.of("message", "Prescription added",
                "prescriptionId", p.getId()));
    }

    private void updateVitals(Context ctx) {
        JsonObject body = JsonParser.parseString(ctx.body()).getAsJsonObject();
        Vitals v = appointmentService.updateVitals(
                ctx.pathParam("patientId"),
                body.get("staffId").getAsString(),
                body.get("bloodPressure").getAsString(),
                body.get("temperature").getAsDouble(),
                body.get("weight").getAsDouble());
        ctx.json(Map.of("message", "Vitals updated",
                "bloodPressure", v.getBloodPressure(),
                "temperature", v.getTemperature(),
                "weight", v.getWeight()));
    }

    private void getMedicalRecord(Context ctx) {
        Patient p = patientService.getPatient(ctx.pathParam("patientId"));
        ctx.json(p.getMedicalRecord());
    }

    public void start(int port) { app.start(port); }
    public void stop()          { app.stop(); }
    public Javalin getApp()     { return app; }
}