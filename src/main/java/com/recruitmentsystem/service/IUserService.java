package com.recruitmentsystem.service;

import com.recruitmentsystem.entity.User;
import com.recruitmentsystem.model.user.UserRequestModel;
import com.recruitmentsystem.model.user.UserDisplayModel;

import java.util.List;

public interface IUserService {
//    AuthenticationResponse register(UserRequestModel request);
//    String confirmToken(String token);
//    AuthenticationResponse login(AuthenticationRequest request);
//    void updatePassword(String token, String newPassword);
    void addUser(UserRequestModel request);
    List<UserDisplayModel> findAllUsers();
    UserDisplayModel findById(Integer id);
    void updateUser(Integer id, UserRequestModel request);
    void deleteUser(Integer id);
//    AuthenticationResponse forgotPassword(String email);

    User findUserById(Integer id);

    User findUserByUsername(String username);

    User findUserByEmail(String userEmail);
}
