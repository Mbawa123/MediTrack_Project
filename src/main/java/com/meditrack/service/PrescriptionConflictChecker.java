package com.meditrack.service;

import java.util.*;

// CHERRY 2 — Prescription Conflict Checker
// Stores a map of medication → list of conflicting medications
// Before adding any prescription, we check this map
// If a conflict is found, we throw an exception with a clear message

public class PrescriptionConflictChecker {

    // Conflict map — medication name (lowercase) → list of conflicting meds
    private final Map<String, List<String>> conflictMap = new HashMap<>();

    public PrescriptionConflictChecker() {
        // Seed with common real-world drug conflicts
        addConflict("warfarin",   List.of("aspirin", "ibuprofen", "naproxen"));
        addConflict("aspirin",    List.of("warfarin", "ibuprofen"));
        addConflict("ibuprofen",  List.of("warfarin", "aspirin", "naproxen"));
        addConflict("naproxen",   List.of("ibuprofen", "warfarin"));
        addConflict("metformin",  List.of("alcohol", "contrast dye"));
        addConflict("lisinopril", List.of("potassium", "spironolactone"));
        addConflict("simvastatin",List.of("amiodarone", "clarithromycin"));
        addConflict("amoxicillin",List.of("methotrexate"));
        addConflict("ciprofloxacin", List.of("antacids", "iron supplements"));
    }

    private void addConflict(String med, List<String> conflicts) {
        conflictMap.put(med.toLowerCase(), new ArrayList<>(conflicts));
    }

    // Check if newMedication conflicts with any existing prescriptions
    // Returns the conflicting medication name if found, null if safe
    public String checkConflict(String newMedication,
                                List<String> existingMedications) {
        String newMed = newMedication.toLowerCase().trim();
        List<String> conflicts = conflictMap.getOrDefault(newMed, List.of());

        for (String existing : existingMedications) {
            String existingLower = existing.toLowerCase().trim();
            // Check if new med conflicts with existing
            if (conflicts.contains(existingLower)) {
                return existing;
            }
            // Also check reverse — existing med conflicts with new
            List<String> reverseConflicts = conflictMap.getOrDefault(existingLower, List.of());
            if (reverseConflicts.contains(newMed)) {
                return existing;
            }
        }
        return null; // no conflict found
    }
}
