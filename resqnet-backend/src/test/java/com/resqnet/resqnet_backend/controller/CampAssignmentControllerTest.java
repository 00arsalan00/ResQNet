package com.resqnet.resqnet_backend.controller;

import com.resqnet.resqnet_backend.service.CampAssignmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CampAssignmentController.class)
class CampAssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampAssignmentService service;

    @Test
    void assignPeople_success() throws Exception {
        UUID campId = UUID.randomUUID();

        mockMvc.perform(post("/api/camp-assignments/" + campId + "/assign")
                        .param("count", "10"))
                .andExpect(status().isOk());

        Mockito.verify(service).assignPeople(campId, 10);
    }

    @Test
    void releasePeople_success() throws Exception {
        UUID campId = UUID.randomUUID();

        mockMvc.perform(post("/api/camp-assignments/" + campId + "/release")
                        .param("count", "5"))
                .andExpect(status().isOk());

        Mockito.verify(service).releasePeople(campId, 5);
    }

    @Test
    void assignCampToIncident_success() throws Exception {
        UUID incidentId = UUID.randomUUID();
        UUID campId = UUID.randomUUID();

        mockMvc.perform(post("/api/camp-assignments/admin/incidents/" + incidentId + "/camps/" + campId))
                .andExpect(status().isOk());

        Mockito.verify(service).assignCampToIncident(incidentId, campId);
    }
}