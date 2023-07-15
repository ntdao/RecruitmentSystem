package com.recruitmentsystem.service;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.entity.User;
import com.recruitmentsystem.mapper.UserMapper;
import com.recruitmentsystem.model.user.UserDisplayModel;
import com.recruitmentsystem.model.user.UserPagination;
import com.recruitmentsystem.model.user.UserRequestModel;
import com.recruitmentsystem.repository.IUserRepository;
import com.recruitmentsystem.security.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;

    public void addUser(UserRequestModel request) {
        // check username
        String username = request.username();
        if (userRepository.existsUserByUsername(username)) {
            throw new ResourceAlreadyExistsException("username already taken");
        }

        // check email
        String email = request.email();
        if (userRepository.existsUserByEmail(email)) {
            throw new ResourceAlreadyExistsException("email already taken");
        }

        // add
        User user = userMapper.userRequestModelToUser(request);
        user.setCreatedAt(Instant.now());
//        user.setCreatedBy(findUserByToken(request.token()).getId());
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

    public List<UserDisplayModel> getAllUsers(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<User> pagedResult = userRepository.findAll(paging);

        List<UserDisplayModel> list = pagedResult.getContent()
                .stream()
                .filter(u -> !u.isDeleteFlag())
                .map(userMapper::userToDisplayModel)
                .collect(Collectors.toList());

        if (pagedResult.hasContent()) {
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    public UserPagination getAllUsers1(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<User> pagedResult = userRepository.findAll(paging);

        List<UserDisplayModel> list = pagedResult.getContent()
                .stream()
                .filter(u -> !u.isDeleteFlag())
                .map(userMapper::userToDisplayModel)
                .collect(Collectors.toList());

        UserPagination pagination = UserPagination.builder()
                .total(pagedResult.getTotalElements())
                .totalPage(pagedResult.getTotalPages())
                .pageSize(pageSize)
                .pageNo(pageNo)
                .list(list)
                .build();
        return pagination;
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

    private List<UserDisplayModel> findUserByUsername(String name) {
        return userRepository.findAll()
                .stream()
                .filter(user -> (!user.isDeleteFlag() &&
                        user.getUsername().contains(name)))
                .map(userMapper::userToDisplayModel)
                .collect(Collectors.toList());
    }
    private List<UserDisplayModel> findUserByFirstName(String name) {
        return userRepository.findAll()
                .stream()
                .filter(user -> (!user.isDeleteFlag() &&
                        user.getFirstName().contains(name)))
                .map(userMapper::userToDisplayModel)
                .collect(Collectors.toList());
    }
    private List<UserDisplayModel> findUserByLastName(String name) {
        return userRepository.findAll()
                .stream()
                .filter(user -> (!user.isDeleteFlag() &&
                        user.getLastName().contains(name)))
                .map(userMapper::userToDisplayModel)
                .collect(Collectors.toList());
    }
    public List<UserDisplayModel> findUserByName(String name) {
        List<UserDisplayModel> list = new ArrayList<>();
        list.addAll(findUserByUsername(name));
        list.addAll(findUserByFirstName(name));
        list.addAll(findUserByLastName(name));
        return list;
    }
    public User findUserByToken(String token) {
        String email = jwtTokenUtil.extractEmail(token);
        User user = null;
        try {
            user = findUserByEmail(email);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("User with token " + token + " does not exist");
        }
        return user;
    }
    public UserDisplayModel findUserDisplayByToken(String token) {
        String email = jwtTokenUtil.extractEmail(token);
        return userRepository.findTopByEmail(email)
                .filter(user -> !user.isDeleteFlag())
                .map(userMapper::userToDisplayModel)
                .orElseThrow(() -> new ResourceNotFoundException("User with token " + token + " does not exist"));
    }
    public User findUserByEmail(String email) {
        return userRepository.findTopByEmail(email)
                .filter(user -> !user.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " does not exist"));
    }
    @Transactional
    public void updateUser(Integer id, UserRequestModel request) {
        User updateUser;
        // tim user theo id
        updateUser = findUserById(id);
//        updateUser.setUpdatedAt(Instant.now());
        updateUser(updateUser, request);
    }

    private void updateUser(User updateUser, UserRequestModel request) {
        int id = updateUser.getId();

        // tao ban ghi luu thong tin cu cua user
        User oldUser = new User(updateUser, true);
        userRepository.save(oldUser);

        // update user
        updateUser = userMapper.userRequestModelToUser(request);
        updateUser.setId(id);
        updateUser.setCreatedAt(oldUser.getCreatedAt());
        updateUser.setCreatedBy(oldUser.getCreatedBy());
        updateUser.setUpdatedAt(oldUser.getUpdatedAt());
//        updateUser.setUpdatedBy(findUserByToken(request.token()).getId());

//        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
////        user.setPhotos(fileName);
//        updateUser.setImgUrl(fileName);
//        String uploadDir = "user-photos/" + id;
//        FileService.saveFile(uploadDir, fileName, multipartFile);
        userRepository.save(updateUser);
    }

    @Transactional
    public void updateUser(String token, UserRequestModel request) {
        User updateUser = findUserByToken(token);
        updateUser(updateUser, request);
    }
    public void deleteUser(Integer id) {
        User user;
        try {
            user = findUserById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("User with id " + id + " does not exist");
        }
        user.setDeleteFlag(true);
        userRepository.save(user);
    }
}
