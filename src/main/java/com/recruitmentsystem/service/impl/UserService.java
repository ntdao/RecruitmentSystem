package com.recruitmentsystem.service.impl;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.entity.User;
import com.recruitmentsystem.mapper.UserMapper;
import com.recruitmentsystem.model.user.UserDisplayModel;
import com.recruitmentsystem.model.user.UserRequestModel;
import com.recruitmentsystem.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final UserMapper userMapper;

    public void addUser(UserRequestModel userRegistrationRequest) {
        // check username
        String username = userRegistrationRequest.username();
        if(userRepository.existsUserByUsername(username)){
            throw new ResourceAlreadyExistsException("username already taken");
        }

        // check email
        String email = userRegistrationRequest.email();
        if(userRepository.existsUserByEmail(email)){
            throw new ResourceAlreadyExistsException("email already taken");
        }

        // add
        User user = userMapper.userRequestModelToUser(userRegistrationRequest);
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
        userRepository.enableUser(user.getEmail());
    }

    public List<UserDisplayModel> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(user -> !user.isDeleteFlag())
                .map(userMapper::userToDisplayModel)
                .collect(Collectors.toList());
    }
    public UserDisplayModel findById(Integer id) {
        return userRepository.findById(id)
                .filter(user -> !user.isDeleteFlag())
                .map(userMapper::userToDisplayModel)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " does not exist"));
    }

    public User findUserById(Integer id) {
        return userRepository.findById(id)
                .filter(user -> !user.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " does not exist"));
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .filter(user -> !user.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " does not exist"));
    }

    public User findUserByEmail(String email) {
        return userRepository.findTopByEmail(email)
                .filter(user -> !user.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " does not exist"));
    }
    @Transactional
    public void updateUser(Integer id, UserRequestModel requestModel) {
        User updateUser;
        // tim user theo id
        try {
            updateUser = findUserById(id);
        }
        catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("User with id " + id + " does not exist");
        }
//        updateUser.setUpdatedAt(Instant.now());
        // tao ban ghi luu thong tin cu cua user
        User oldUser = new User(updateUser, id, true);
        userRepository.save(oldUser);

        // update user
        updateUser = userMapper.userRequestModelToUser(requestModel);
        updateUser.setId(id);
        updateUser.setCreatedAt(oldUser.getCreatedAt());
        updateUser.setCreatedBy(oldUser.getCreatedBy());
        updateUser.setUpdatedAt(oldUser.getUpdatedAt());
//        updateUser.setUpdatedBy();
        userRepository.save(updateUser);
    }
    public void deleteUser(Integer id) {
        User user;
        try {
            user = findUserById(id);
        }
        catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("User with id " + id + " does not exist");
        }
        user.setDeleteFlag(true);
        userRepository.save(user);
    }
}
