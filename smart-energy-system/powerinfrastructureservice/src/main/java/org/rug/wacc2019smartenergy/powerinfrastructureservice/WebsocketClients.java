package org.rug.wacc2019smartenergy.powerinfrastructureservice;

import java.util.ArrayList;
import java.util.HashMap;

public class WebsocketClients {

    private HashMap<String, String> sessionIdHouseholdId;
    private HashMap<String, ArrayList<String>> householdIdSessionId;

    public WebsocketClients() {
        this.sessionIdHouseholdId = new HashMap<>();
        this.householdIdSessionId = new HashMap<>();
    }

    public void registerHousehold(String sessionId, String householdId) {
        System.out.println("[Register] " + sessionId + " with " + householdId);
        // First set the session to household hashmap
        String oldHouseholdId = null;
        if (this.sessionIdHouseholdId.containsKey(sessionId)) {
            oldHouseholdId = this.sessionIdHouseholdId.get(sessionId);
            this.sessionIdHouseholdId.replace(sessionId, householdId);
        } else {
            this.sessionIdHouseholdId.put(sessionId, householdId);
        }

        // Then set the household to session hashmap
        ArrayList<String> sessions;
        if (oldHouseholdId != null) {
            sessions = this.householdIdSessionId.get(oldHouseholdId);
            sessions.remove(sessionId);
            this.householdIdSessionId.replace(oldHouseholdId, sessions);
        }
        if (!this.householdIdSessionId.containsKey(householdId)) {
            this.householdIdSessionId.put(householdId, new ArrayList<>());
        }
        sessions = this.householdIdSessionId.get(householdId);
        if (!sessions.contains(sessionId)) {
            sessions.add(sessionId);
            this.householdIdSessionId.replace(householdId, sessions);
        }
    }

    public void deregisterHousehold(String sessionId) {
        if (this.sessionIdHouseholdId.containsKey(sessionId)) {
            String householdId = this.getSessionHousehold(sessionId);
            System.out.println("[Deregister] " + sessionId + " with " + householdId);
            ArrayList<String> sessions = this.householdIdSessionId.get(householdId);
            if (sessions.contains(sessionId)) {
                sessions.remove(sessionId);
                this.householdIdSessionId.replace(householdId, sessions);
            }
            this.sessionIdHouseholdId.remove(sessionId);
        }
    }

    public ArrayList<String> getHouseholdSessions(String householdId) {
        if (this.householdIdSessionId.containsKey(householdId)) {
            return this.householdIdSessionId.get(householdId);
        }
        return new ArrayList<>();
    }

    public String getSessionHousehold(String sessionId) {
        return this.sessionIdHouseholdId.get(sessionId);
    }
}
