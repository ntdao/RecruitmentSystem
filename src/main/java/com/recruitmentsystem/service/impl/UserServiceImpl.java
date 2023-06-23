//package com.recruitmentsystem.service.impl;
//
//import com.recruitmentsystem.common.exception.ResourceNotFoundException;
//import com.recruitmentsystem.model.user.UserRequestModel;
//import com.recruitmentsystem.model.user.UserDisplayModel;
//import com.recruitmentsystem.entity.User;
//import com.recruitmentsystem.mapper.UserMapper;
//import com.recruitmentsystem.repository.IUserRepository;
//import com.recruitmentsystem.service.IUserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.Instant;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class UserServiceImpl implements IUserService {
//    private final IUserRepository userRepository;
//    private final UserMapper userMapper;
//
//    @Override
//    public void addUser(UserRequestModel userRegistrationRequest) {
//        // check username
//        String username = userRegistrationRequest.username();
//        if(userRepository.existsUserByUsername(username)){
//            throw new IllegalStateException("username already taken");
//        }
//
//        // check email
//        String email = userRegistrationRequest.email();
//        if(userRepository.existsUserByEmail(email)){
//            throw new IllegalStateException("email already taken");
//        }
//
//        // add
//        User user = userMapper.userRequestModelToUser(userRegistrationRequest);
//        user.setCreatedAt(Instant.now());
//        userRepository.save(user);
//        userRepository.enableUser(user.getEmail());
//    }
//
//    @Override
//    public List<UserDisplayModel> findAllUsers() {
//        List<User> users = userRepository.findAll();
//        System.out.println("Get all users");
//        System.out.println(users);
//        return users.stream()
//                .filter(user -> !user.isDeleteFlag())
//                .map(userMapper::userToDisplayModel)
//                .collect(Collectors.toList());
//    }
//    @Override
//    public UserDisplayModel findById(Integer id) {
//        return userRepository.findById(id)
//                .filter(user -> !user.isDeleteFlag())
//                .map(userMapper::userToDisplayModel)
//                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " does not exist"));
//    }
//
//    @Override
//    public User findUserById(Integer id) {
//        return userRepository.findById(id)
//                .filter(user -> !user.isDeleteFlag())
//                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " does not exist"));
//    }
//
//    @Override
//    public User findUserByUsername(String username) {
//        return userRepository.findUserByUsername(username)
//                .filter(user -> !user.isDeleteFlag())
//                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " does not exist"));
//    }
//
//    @Override
//    public User findUserByEmail(String email) {
//        return userRepository.findTopByEmail(email)
//                .filter(user -> !user.isDeleteFlag())
//                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " does not exist"));
//    }
//    @Override
//    @Transactional
//    public void updateUser(Integer id, UserRequestModel requestModel) {
//        // tim user theo id
//        User updateUser = findUserById(id);
////        updateUser.setUpdatedAt(Instant.now());
//
//        // tao ban ghi luu thong tin cu cua user
//        User oldUser = new User(updateUser, id, true);
//        userRepository.save(oldUser);
//
//        // update user
//        updateUser = userMapper.userRequestModelToUser(requestModel);
//        updateUser.setId(id);
//        updateUser.setCreatedAt(oldUser.getCreatedAt());
//        updateUser.setCreatedBy(oldUser.getCreatedBy());
//        updateUser.setUpdatedAt(oldUser.getUpdatedAt());
////        updateUser.setUpdatedBy();
//        userRepository.save(updateUser);
//    }
//    @Override
//    public void deleteUser(Integer id) {
//        User user = findUserById(id);
//        user.setDeleteFlag(true);
//        userRepository.save(user);
//    }
//}
