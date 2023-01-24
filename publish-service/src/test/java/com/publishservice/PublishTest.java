package com.publishservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.publishservice.dto.CVRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class PublishTest {

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    /// XXX: org.junit.jupiter.api.Test;
    @Test
    public void test() throws Exception {
        CVRequest requestDto = getCvRequest();
        String request = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cv")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated());
    }

    private CVRequest getCvRequest() {
        return CVRequest.builder().name("ax").salary(BigDecimal.valueOf(16000)).vacancy("prog").build();
    }

}
