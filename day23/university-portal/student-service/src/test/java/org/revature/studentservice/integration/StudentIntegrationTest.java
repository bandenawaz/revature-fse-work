package org.revature.studentservice.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.revature.studentservice.dto.StudentDTO;
import org.revature.studentservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest  //Loads the ENTIRE Spring context(real beans)
@AutoConfigureMockMvc  //Auto-Configures MockMvc for full stack testing
@ActiveProfiles("test")
public class StudentIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void cleanUp(   ) {
        studentRepository.deleteAll();  //Fresh Db before each test
    }

    @Test
    void fullCrudFlow_ShouldWorkEndToEnd() throws Exception {

        /**
         * Step 1: CREATE
         */
        StudentDTO  studentDTO = new StudentDTO();
        studentDTO.setStudentName("Arjun Sharma");
        studentDTO.setStudentEmail("arjun@uni.edu");
        studentDTO.setStudentDepartment("CSE");
        studentDTO.setAcademicYear(2);

        String responseBody = mockMvc.perform(post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").exists())
                .andExpect(jsonPath("$.studentName").value("Arjun Sharma"))
                .andReturn()
                .getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(responseBody);
        //Extract the id for subsequent tests
        Long createdId = json.get("studentId").asLong();        // matches REST response
        String name = json.get("studentName").asText();
        String email = json.get("studentEmail").asText();
        String department = json.get("studentDepartment").asText();
        int year = json.get("academicYear").asInt();


       // Long createdId = objectMapper.readTree(responseBody).get("id").asLong();

        /**
         * Step 2: READ ALL
         */
        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)));

        /**
         * STEP 3: READ ONE
         */
        mockMvc.perform(get("/api/v1/students/" + createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentEmail").value("arjun@uni.edu"));

        /**
         * STEP 4: UPDATE
         *
         */
        studentDTO.setAcademicYear(3);  //Arjun moved to 3
        mockMvc.perform(put("/api/v1/students/" + createdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.academicYear").value(3));

        /**
         * STEP 5: DELETE
         *
         */
        mockMvc.perform(delete("/api/v1/students/" + createdId))
                        .andExpect(status().isNoContent());

        /**
         * STEP 6: VERIFY DELETE
         */
        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }


}
