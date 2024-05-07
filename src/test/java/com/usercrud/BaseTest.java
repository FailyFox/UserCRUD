package com.usercrud;

import com.usercrud.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseTest {
    protected static final int ID_DEFAULT = 1;
    protected static final int ID_ZERO = 0;
    protected static final int ID_SECOND = 2;
    protected static final LocalDate VALID_BIRTH_DATE = LocalDate.of(2000, 1, 1);
    protected static final LocalDate INVALID_BIRTH_DATE = LocalDate.of(2020, 1, 1);
    protected static final LocalDate VALID_FROM_DATE = LocalDate.of(2000,1,1);
    protected static final LocalDate INVALID_FROM_DATE = LocalDate.of(2020,1,1);
    protected static final LocalDate TO_DATE = LocalDate.of(2002,1,1);

    protected static User createValidUser() {
        return User.builder()
                .email("email@email.com")
                .firstName("Test")
                .lastName("Name")
                .birthDate(VALID_BIRTH_DATE)
                .build();
    }
    protected static User createUserUnder18() {
        return User.builder()
                .email("email@email.com")
                .firstName("Test")
                .lastName("Name")
                .birthDate(INVALID_BIRTH_DATE)
                .build();
    }
    protected static User createInvalidUser() {
        return User.builder()
                .email("email")
                .firstName("Name")
                .lastName("Last")
                .birthDate(VALID_BIRTH_DATE)
                .build();
    }
    protected static User createUpdatedUser() {
        return User.builder()
                .email("email@email.com")
                .firstName("First")
                .lastName("Last")
                .birthDate(VALID_BIRTH_DATE)
                .build();
    }
    protected static Map<String, Object> createValidUserFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("firstName", "First");
        fields.put("lastName", "Last");
        return fields;
    }
    protected static Map<String, Object> createInvalidUserFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("first", "First");
        fields.put("lastName", "Last");
        return fields;
    }
    protected static User createValidUserUpdate() {
        return User.builder()
                .id(ID_DEFAULT)
                .email("email@email.com")
                .firstName("First")
                .lastName("Last")
                .birthDate(VALID_BIRTH_DATE)
                .address("Address")
                .build();
    }
    protected static User createUserUpdate() {
        return User.builder()
                .id(ID_DEFAULT)
                .email("email@email.com")
                .firstName("First")
                .lastName("Last")
                .birthDate(VALID_BIRTH_DATE)
                .address("Address")
                .build();
    }
    protected static User createInvalidUserUpdate() {
        return User.builder()
                .id(ID_DEFAULT)
                .email("email@gmail.com")
                .firstName("First")
                .lastName("Last")
                .birthDate(INVALID_BIRTH_DATE)
                .address("Address")
                .build();
    }
    protected static List<User> createListOfUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(User.builder()
                        .email("email@email.com")
                        .firstName("First")
                        .lastName("Last")
                        .birthDate(VALID_BIRTH_DATE)
                .build());
        return userList;
    }
}