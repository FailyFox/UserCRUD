package com.usercrud.controller;

import com.usercrud.BaseTest;
import com.usercrud.dto.UserRequestDto;
import com.usercrud.model.User;
import com.usercrud.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest extends BaseTest {
    @Mock
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;

    private User validUser;
    private User userUnder18;
    private User invalidUser;
    private User updatedUser;
    private User validUserUpdate;
    private User userUpdate;
    private User invalidUserUpdate;
    private List<User> users;
    private Map<String, Object> userValidFields;
    private Map<String, Object> userInvalidFields;

    @BeforeEach
    public void setup() {
        validUser = createValidUser();
        userUnder18 = createUserUnder18();
        invalidUser = createInvalidUser();
        updatedUser = createUpdatedUser();
        userValidFields = createValidUserFields();
        userInvalidFields = createInvalidUserFields();
        validUserUpdate = createValidUserUpdate();
        userUpdate = createUserUpdate();
        invalidUserUpdate = createInvalidUserUpdate();
        users = createListOfUsers();
    }

    @Test
    public void createUser_thenReturnCreatedUser() throws Exception {
        when(userService.createUser(any(UserRequestDto.class))).thenReturn(validUser);
        mockMvc.perform(post("/users")
                        .content(new ObjectMapper().registerModule(new JavaTimeModule())
                                .writeValueAsString(validUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(validUser.getEmail()))
                .andExpect(jsonPath("$.firstName").value(validUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(validUser.getLastName()))
                .andExpect(jsonPath("$.birthDate").value(validUser.getBirthDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
    }
    @Test
    public void createUser_thenReturnUserUnder18() throws Exception {
        when(userService.createUser(any(UserRequestDto.class))).thenReturn(validUser);
        mockMvc.perform(post("/users")
                        .content(new ObjectMapper().registerModule(new JavaTimeModule())
                                .writeValueAsString(userUnder18))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"message\":\"User must be at least 18 years old!\"}"));
    }
    @Test
    public void createUser_thenReturnInvalidFields() throws Exception {
        when(userService.createUser(any(UserRequestDto.class))).thenReturn(validUser);
        mockMvc.perform(post("/users")
                        .content(new ObjectMapper().registerModule(new JavaTimeModule())
                                .writeValueAsString(invalidUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string("{\"message\":\"Email is not valid; \"}"));
    }
    @Test
    public void searchUsersByBirthDate_thenReturnListOfUsers() throws Exception {
        when(userService.searchUsersByBirthDate(any(LocalDate.class), any(LocalDate.class))).thenReturn(users);
        mockMvc.perform(get("/users/search")
                        .param("fromDate", VALID_FROM_DATE.toString())
                        .param("toDate", TO_DATE.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(users.get(ID_ZERO).getEmail()))
                .andExpect(jsonPath("$[0].firstName").value(users.get(ID_ZERO).getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(users.get(ID_ZERO).getLastName()))
                .andExpect(jsonPath("$[0].birthDate").value(users.get(ID_ZERO).getBirthDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

    }
    @Test
    public void searchUsersByBirthDate_thenReturnInvalidDateRange() throws Exception {
        when(userService.searchUsersByBirthDate(any(LocalDate.class), any(LocalDate.class))).thenReturn(users);
        mockMvc.perform(get("/users/search")
                        .param("fromDate", INVALID_FROM_DATE.toString())
                        .param("toDate", TO_DATE.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"message\":\"Invalid date range. " +
                        "'From' date must be earlier than 'To' date!\"}"));

    }
    @Test
    public void updateUserField_thenReturnUpdatedUser() throws Exception {
        when(userService.updateUserFields(anyInt(), eq(userValidFields))).thenReturn(updatedUser);
        mockMvc.perform(patch("/users/{id}", ID_DEFAULT)
                        .content(new ObjectMapper().writeValueAsString(userValidFields))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(updatedUser.getFirstName()));
    }
    @Test
    public void updateUserField_thenReturnWrongField() throws Exception {
        when(userService.updateUserFields(anyInt(), eq(userInvalidFields))).thenReturn(updatedUser);
        mockMvc.perform(patch("/users/{id}", ID_DEFAULT)
                        .content(new ObjectMapper().writeValueAsString(userInvalidFields))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string("{\"message\":\"Please check that the fields are correct!\"}"));
    }
    @Test
    public void updateUser_thenReturnUserUpdate() throws Exception {
        when(userService.updateUser(anyInt(), any(UserRequestDto.class))).thenReturn(userUpdate);
        mockMvc.perform(put("/users/{id}", ID_DEFAULT)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule())
                                .writeValueAsString(validUserUpdate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userUpdate.getId()))
                .andExpect(jsonPath("$.email").value(userUpdate.getEmail()))
                .andExpect(jsonPath("$.firstName").value(userUpdate.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userUpdate.getLastName()))
                .andExpect(jsonPath("$.birthDate").value(userUpdate.getBirthDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.address").value(userUpdate.getAddress()));
    }
    @Test
    public void updateUser_thenReturnUserNotFound() throws Exception {
        when(userService.updateUser(anyInt(), any(UserRequestDto.class))).thenReturn(userUpdate);
        mockMvc.perform(put("/users/{id}", ID_ZERO)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule())
                                .writeValueAsString(validUserUpdate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"message\":\"There is no user with id 0\"}"));
    }
    @Test
    public void updateUser_thenReturnInvalidUpdate() throws Exception {
        when(userService.updateUser(anyInt(), any(UserRequestDto.class))).thenReturn(userUpdate);
        mockMvc.perform(put("/users/{id}", ID_DEFAULT)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule())
                                .writeValueAsString(invalidUserUpdate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string("{\"message\":\"User must be at least 18 years old!\"}"));
    }
    @Test
    public void deleteUser_thenReturnOk() throws Exception {
        mockMvc.perform(delete("/users/{id}", ID_SECOND)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void deleteUser_thenReturnUserNotFound() throws Exception {
        mockMvc.perform(delete("/users/{id}", ID_ZERO)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"message\":\"There is no user with id 0\"}"));
    }
}