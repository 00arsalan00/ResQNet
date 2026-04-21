package com.resqnet.resqnet_backend.controller;

import com.resqnet.resqnet_backend.entity.ResourceAssignment;
import com.resqnet.resqnet_backend.service.ResourceAssignmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResourceAssignmentController.class)
class ResourceAssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResourceAssignmentService service;

    @Test
    void assignResource_success() throws Exception {

        UUID resourceId = UUID.randomUUID();
        UUID campId = UUID.randomUUID();

        Mockito.when(service.assignResource(resourceId, campId, 10))
                .thenReturn(new ResourceAssignment());

        mockMvc.perform(post("/api/resource-assignments/resources/"
                        + resourceId + "/camps/" + campId)
                        .param("quantity", "10"))
                .andExpect(status().isCreated());
    }
}