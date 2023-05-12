package com.recruitmentsystem.service.impl;

import com.recruitmentsystem.registration.UserRegistrationRequest;
import com.recruitmentsystem.dto.UserDTO;
import com.recruitmentsystem.entity.User;
import com.recruitmentsystem.mapper.UserDTOMapper;
import com.recruitmentsystem.repository.IUserRepository;
import com.recruitmentsystem.service.IRoleService;
import com.recruitmentsystem.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final IRoleService roleService;
    private final UserDTOMapper userDTOMapper;


    @Override
    public void addUser(UserRegistrationRequest userRegistrationRequest) {
//        // check username
//        String username = userRegistrationRequest.username();
//        if(userRepository.existsUserWithUsername()){
//            throw new IllegalStateException("username already taken");
//        }

//        // check email
//        String email = userRegistrationRequest.email();
//        if(userRepository.existsUserWithEmail()){
//            throw new IllegalStateException("email already taken");
//        }

        // add
        User user = new User(
                userRegistrationRequest.username(),
                userRegistrationRequest.password(),
                userRegistrationRequest.email(),
                userRegistrationRequest.firstName(),
                userRegistrationRequest.lastName(),
                userRegistrationRequest.phoneNumber(),
                userRegistrationRequest.address(),
                userRegistrationRequest.gender(),
                userRegistrationRequest.imgUrl(),
                userRegistrationRequest.birthday(),
                roleService.findRoleByRoleName(userRegistrationRequest.roleName())
        );
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOList = users.stream()
                .filter(user -> !user.isDeleteFlag())
                .map(userDTOMapper)
                .collect(Collectors.toList());
        return userDTOList;
    }
    @Override
    public UserDTO findUserById(Integer id) {
        return userRepository.findById(id)
                .filter(user -> !user.isDeleteFlag())
                .map(userDTOMapper)
                .orElseThrow(() -> new IllegalStateException("User with id " + id + " does not exist"));
    }
    @Override
    @Transactional
    public UserDTO updateUser(User user) {
        return null;
    }
    @Override
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .filter(u -> !u.isDeleteFlag())
                .orElseThrow(() -> new IllegalStateException("User with id " + id + " does not exist"));
        user.setDeleteFlag(true);
        userRepository.save(user);
    }
}
