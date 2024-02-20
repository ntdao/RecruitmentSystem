package com.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.dto.DegreeDTO;
import com.recruitmentsystem.entity.Degree;
import com.recruitmentsystem.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.repository.DegreeRepository;
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
public class DegreeService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DegreeRepository degreeRepository;

    public void save(DegreeDTO degree) {
        if (Objects.isNull(degree.getId())) {
            create(degree);
        } else {
            update(degree);
        }
    }

    public void create(DegreeDTO degree) {
        degreeRepository.save(checkValid(degree));
    }

    private Degree checkValid(DegreeDTO dto) {
        if (degreeRepository.countByNameAndCode(
                dto.getId(),
                DataFormat.lower(dto.getName()),
                DataFormat.lower(dto.getCode())) > 0) {
            throw new ResourceAlreadyExistsException("Tên bằng cấp/mã bằng cấp đã tồn tại");
        }
        return objectMapper.convertValue(dto, Degree.class);
    }

    public void update(DegreeDTO _degree) {
        Optional<Degree> op = degreeRepository.findById(_degree.getId());
        Degree degree = checkValid(_degree);
        if (op.isPresent()) {
            Degree r = op.get();
            r.setName(degree.getName());
            r.setCode(degree.getCode());
            degreeRepository.save(r);
        } else {
            throw new ResourceNotFoundException("Không tìm thấy bằng cấp!");
        }
    }

    public Page<DegreeDTO> findAll(DegreeDTO degree) {
        Pageable pageable = PageRequest.of(degree.getPage() - 1, degree.getSize());
        return degreeRepository.findAll(DataFormat.lower(degree.getName()), pageable)
                .map(r -> objectMapper.convertValue(r, DegreeDTO.class));
    }

    public DegreeDTO findDTOById(Integer id) {
        Optional<Degree> op = degreeRepository.findById(id);
        if (op.isPresent()) {
            return objectMapper.convertValue(op.get(), DegreeDTO.class);
        } else {
            throw new ResourceNotFoundException("Không tìm thấy bằng cấp!");
        }
    }

    public Degree findById(Integer id) {
        Optional<Degree> op = degreeRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        } else {
            throw new ResourceNotFoundException("Không tìm thấy bằng cấp!");
        }
    }

    @Transactional
    public void delete(Integer id) {
        Degree degree = degreeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bằng cấp!"));
        degreeRepository.delete(degree);
    }

    public Degree findByName(String name) {
        return degreeRepository.findByName(name).get();
    }
}

