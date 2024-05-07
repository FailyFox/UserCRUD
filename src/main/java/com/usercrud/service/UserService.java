package com.usercrud.service;

import com.usercrud.dto.UserRequestDto;
import com.usercrud.exception.UserNotFoundException;
import com.usercrud.exception.WrongFieldException;
import com.usercrud.mapper.UserRequestMapper;
import com.usercrud.model.User;
import com.usercrud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserRequestMapper userRequestMapper;

    @Transactional
    public User createUser(UserRequestDto user) {
        return userRepository.save(userRequestMapper.toUser(user));
    }
    public List<User> searchUsersByBirthDate(LocalDate fromDate, LocalDate toDate) {
        return userRepository.findByBirthDateBetween(fromDate, toDate);
    }
    public User updateUserFields(int id, Map<String, Object> fields) {
        Optional<User> user = Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
        if (user.isPresent()) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(User.class, key);
                if (field == null) {
                    throw new WrongFieldException();
                }
                field.setAccessible(true);
                ReflectionUtils.setField(field, user.get(), value);
            });
            return userRepository.save(user.get());
        }
        return null;
    }
    public User updateUser(int id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setEmail(userRequestDto.getEmail());
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setBirthDate(userRequestDto.getBirthDate());
        user.setAddress(userRequestDto.getAddress());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        return userRepository.save(user);
    }
    public void deleteUser(int id) {
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.deleteById(id);
    }
}