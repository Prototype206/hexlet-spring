package io.hexlet.spring.controller;

import net.datafaker.Faker;
import tools.jackson.databind.ObjectMapper;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import io.hexlet.spring.model.User;
import io.hexlet.spring.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Faker faker;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testCreateUser() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("firstName", "John");
        userData.put("lastName", "Doe");
        userData.put("email", "john@example.com");
        userData.put("birthday", "1990-01-01");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/users"));

        User savedUser = userRepository.findAll().get(0);
        assertThat(savedUser.getFirstName()).isEqualTo("John");
        assertThat(savedUser.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testGetAllUsers() throws Exception {
        User user1 = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();

        User user2 = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();

        userRepository.save(user1);
        userRepository.save(user2);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetUserById() throws Exception {
        User user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
        userRepository.save(user);

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void testUpdateUser() throws Exception {
        User user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
        userRepository.save(user);

        Map<String, String> updateData = new HashMap<>();
        updateData.put("firstName", "UpdatedName");
        updateData.put("lastName", "UpdatedLastName");

        mockMvc.perform(put("/api/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("UpdatedName"))
                .andExpect(jsonPath("$.lastName").value("UpdatedLastName"));

        User updatedUser = userRepository.findById(user.getId()).get();
        assertThat(updatedUser.getFirstName()).isEqualTo("UpdatedName");
    }

    @Test
    void testDeleteUser() throws Exception {
        User user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
        userRepository.save(user);

        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isNoContent());

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }
}