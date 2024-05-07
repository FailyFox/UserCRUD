package com.usercrud.mapper;

import com.usercrud.dto.UserRequestDto;
import com.usercrud.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {
    UserRequestDto toDto(User user);
    User toUser(UserRequestDto userRequestDto);
}