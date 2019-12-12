package org.rug.wacc2019smartenergy.powerinfrastructureservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

public class WebsocketClientsTest {

    static WebsocketClients websocketClients;

    @BeforeAll
    public static void init() {
        websocketClients = new WebsocketClients();
    }

    @Test
    public void registerHousehold() {
        String sessionId = "SESSIONID";
        String householId = "HOUSEHOLDID";
        websocketClients.registerHousehold(sessionId, householId);

        // Test if the household is linked to the session
        assertTrue(websocketClients.getHouseholdSessions(householId).contains(sessionId));

        // Test if the session is linked to the household
        assertEquals(websocketClients.getSessionHousehold(sessionId), householId);
    }

    @Test
    public void deregisterHousehold() {
        String sessionId = "SESSIONID_2";
        String householId = "HOUSEHOLDID_2";
        websocketClients.registerHousehold(sessionId, householId);

        // Test if the household is linked to the session
        assertTrue(websocketClients.getHouseholdSessions(householId).contains(sessionId));

        // Remove link
        websocketClients.deregisterHousehold(sessionId);

        // Test if the household is not linked to the session

        assertFalse(websocketClients.getHouseholdSessions(householId).contains(sessionId));

    }


}
