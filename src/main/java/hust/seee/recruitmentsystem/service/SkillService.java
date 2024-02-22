package hust.seee.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hust.seee.recruitmentsystem.dto.SkillDTO;
import hust.seee.recruitmentsystem.entity.Skill;
import hust.seee.recruitmentsystem.exception.ResourceAlreadyExistsException;
import hust.seee.recruitmentsystem.exception.ResourceNotFoundException;
import hust.seee.recruitmentsystem.repository.SkillRepository;
import hust.seee.recruitmentsystem.utils.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class SkillService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SkillRepository skillRepository;

    public void save(SkillDTO skill) {
        if (Objects.isNull(skill.getId())) {
            create(skill);
        } else {
            update(skill);
        }
    }

    public void create(SkillDTO skill) {
        skillRepository.save(checkValid(skill));
    }

    private Skill checkValid(SkillDTO dto) {
        if (skillRepository.countByNameAndCode(
                dto.getId(),
                DataFormat.lower(dto.getName()),
                DataFormat.lower(dto.getCode())) > 0) {
            throw new ResourceAlreadyExistsException("Tên kỹ năng/mã kỹ năng đã tồn tại");
        }
        return objectMapper.convertValue(dto, Skill.class);
    }

    public void update(SkillDTO _skill) {
        Optional<Skill> op = skillRepository.findById(_skill.getId());
        Skill skill = checkValid(_skill);
        if (op.isPresent()) {
            Skill r = op.get();
            r.setName(skill.getName());
            r.setCode(skill.getCode());
            skillRepository.save(r);
        } else {
            throw new ResourceNotFoundException("Không tìm thấy kỹ năng!");
        }
    }

    public Page<SkillDTO> findAll(SkillDTO skill) {
        Pageable pageable = PageRequest.of(skill.getPage() - 1, skill.getSize());
        return skillRepository.findAll(DataFormat.lower(skill.getName()), pageable)
                .map(r -> objectMapper.convertValue(r, SkillDTO.class));
    }

    public SkillDTO findDTOById(Integer id) {
        Optional<Skill> op = skillRepository.findById(id);
        if (op.isPresent()) {
            return objectMapper.convertValue(op.get(), SkillDTO.class);
        } else {
            throw new ResourceNotFoundException("Không tìm thấy kỹ năng!");
        }
    }

    public Skill findById(Integer id) {
        Optional<Skill> op = skillRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        } else {
            throw new ResourceNotFoundException("Không tìm thấy kỹ năng!");
        }
    }

    @Transactional
    public void delete(Integer id) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy kỹ năng!"));
        skillRepository.delete(skill);
    }

    public Skill findByName(String name) {
        return skillRepository.findByName(name).get();
    }
}

