package com.recruitmentsystem.service;

import com.recruitmentsystem.dto.UserDTO;
import com.recruitmentsystem.entity.User;
import com.recruitmentsystem.mapper.UserDTOMapper;
import com.recruitmentsystem.repository.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final UserDTOMapper userDTOMapper;

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
                .orElseThrow(() -> new IllegalStateException("User with id " + id + " does not exists"));
    }
    @Override
    @Transactional
    public UserDTO updateUser(Integer id, User user) {
//        User _user = user;
//        _user.setId(userRepository.countById() + 1);
//        _user.setOldId(user.getId());
        return null;
    }
    @Override
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User with id " + id + " does not exists"));
        user.setDeleteFlag(true);
        userRepository.save(user);
    }
}
