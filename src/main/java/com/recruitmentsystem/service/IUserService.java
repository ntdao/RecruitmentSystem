package com.recruitmentsystem.service;

import com.recruitmentsystem.registration.UserRegistrationRequest;
import com.recruitmentsystem.dto.UserDTO;
import com.recruitmentsystem.entity.User;

import java.util.List;

public interface IUserService {
    void addUser(UserRegistrationRequest request);
    List<UserDTO> findAllUsers();
    UserDTO findUserById(Integer id);
    UserDTO updateUser(User user);
    void deleteUser(Integer id);
}
