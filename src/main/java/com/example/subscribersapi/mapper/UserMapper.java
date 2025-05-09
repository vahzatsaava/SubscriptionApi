package com.example.subscribersapi.mapper;

import com.example.subscribersapi.entity.User;
import com.example.subscribersapi.model.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    User toUser(UserDto userDto);
}
