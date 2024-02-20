package com.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.dto.RoleDTO;
import com.recruitmentsystem.entity.Role;
import com.recruitmentsystem.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.repository.RoleRepository;
import com.recruitmentsystem.utils.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RoleRepository roleRepository;

    public void save(RoleDTO role) {
        if (Objects.isNull(role.getId())) {
            create(role);
        } else {
            update(role);
        }
    }

    public void create(RoleDTO role) {
        roleRepository.save(checkValid(role));
    }

    private Role checkValid(RoleDTO dto) {
        if (roleRepository.countByNameAndCode(
                dto.getId(),
                DataFormat.lower(dto.getName()),
                DataFormat.lower(dto.getCode())) > 0) {
            throw new ResourceAlreadyExistsException("Tên quyền/mã quyền đã tồn tại");
        }
        return objectMapper.convertValue(dto, Role.class);
    }

    public void update(RoleDTO _role) {
        Optional<Role> op = roleRepository.findById(_role.getId());
        Role role = checkValid(_role);
        if (op.isPresent()) {
            Role r = op.get();
            r.setName(role.getName());
            r.setCode(role.getCode());
            roleRepository.save(r);
        } else {
            throw new ResourceNotFoundException("Không tìm thấy quyền!");
        }
    }

    public Page<RoleDTO> findAll(RoleDTO role) {
        Pageable pageable = PageRequest.of(role.getPage() - 1, role.getSize());
        return roleRepository.findAll(DataFormat.lower(role.getName()), pageable)
                .map(r -> objectMapper.convertValue(r, RoleDTO.class));
    }

    public RoleDTO findDTOById(Integer id) {
        Optional<Role> op = roleRepository.findById(id);
        if (op.isPresent()) {
            return objectMapper.convertValue(op.get(), RoleDTO.class);
        } else {
            throw new ResourceNotFoundException("Không tìm thấy quyền!");
        }
    }

    public Role findById(Integer id) {
        Optional<Role> op = roleRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        } else {
            throw new ResourceNotFoundException("Không tìm thấy quyền!");
        }
    }

    @Transactional
    public void delete(Integer id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy quyền!"));
        roleRepository.delete(role);
    }

    public Role findByCode(String code) {
        return roleRepository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy quyền!"));
    }

    public Role findByName(String name) {
        return (Role) roleRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy quyền!"));
    }
}

