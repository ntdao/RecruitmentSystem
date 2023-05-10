package com.recruitmentsystem.service;

import com.recruitmentsystem.dto.UserDTO;
import com.recruitmentsystem.entity.User;

import java.util.List;

public interface IUserService {
    List<UserDTO> findAllUsers();
    UserDTO findUserById(Integer id);
    UserDTO updateUser(Integer id, User user);
    void deleteUser(Integer id);
}
