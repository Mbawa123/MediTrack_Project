package com.meditrack.service;

import com.meditrack.model.person.Patient;
import java.util.LinkedList;

// CHERRY 1 — The Waiting Room
// Uses LinkedList as a double-ended queue (Deque)
// NORMAL patients → added to BACK (FIFO)
// URGENT patients → added to FRONT (priority)
// WHY LinkedList? Fast O(1) add/remove at both ends
// Trade-off: more memory than ArrayList (stores node pointers)

public class WaitingRoom {

    public enum Priority { NORMAL, URGENT }

    public static class WaitingPatient {
        private final Patient patient;
        private final Priority priority;
        private final String reason;

        public WaitingPatient(Patient patient, Priority priority, String reason) {
            this.patient  = patient;
            this.priority = priority;
            this.reason   = reason;
        }

        public Patient getPatient()   { return patient; }
        public Priority getPriority() { return priority; }
        public String getReason()     { return reason; }
    }

    private final LinkedList<WaitingPatient> queue = new LinkedList<>();

    public void addPatient(Patient patient, Priority priority, String reason) {
        WaitingPatient wp = new WaitingPatient(patient, priority, reason);
        if (priority == Priority.URGENT) {
            queue.addFirst(wp);  // URGENT goes to front
        } else {
            queue.addLast(wp);   // NORMAL joins the back
        }
    }

    public WaitingPatient nextPatient() {
        if (queue.isEmpty())
            throw new IllegalStateException("Waiting room is empty");
        return queue.removeFirst();
    }

    public WaitingPatient peekNext() {
        return queue.isEmpty() ? null : queue.peekFirst();
    }

    public int size()            { return queue.size(); }
    public boolean isEmpty()     { return queue.isEmpty(); }
    public LinkedList<WaitingPatient> getQueue() { return new LinkedList<>(queue); }
}