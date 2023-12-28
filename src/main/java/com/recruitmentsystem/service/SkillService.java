package com.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.entity.Skill;
import com.recruitmentsystem.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.pagination.PageDto;
import com.recruitmentsystem.repository.SkillRepository;
import com.recruitmentsystem.dto.SkillDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillService {
    private final ObjectMapper objectMapper;
    private final SkillRepository skillRepository;
    public void addSkill(SkillDto request) {
        checkDuplicatedSkillName(request.skillName(), request.skillNameVI());
        Skill skill = objectMapper.convertValue(request, Skill.class);
        skillRepository.save(skill);
    }
    private void checkDuplicatedSkillName(String nameEN, String nameVI) {
        if (skillRepository.existsSkillBySkillName(nameEN)) {
            throw new ResourceAlreadyExistsException("Skill EN name already taken");
        }
        if (skillRepository.existsSkillBySkillNameVI(nameVI)) {
            throw new ResourceAlreadyExistsException("Skill VI name already taken");
        }
    }
    public void deleteSkill(Integer id) {
        Skill skill = findById(id);
        skillRepository.delete(skill);
    }
    public List<Skill> findAll() {
        return skillRepository.findAll();
    }
    public List<SkillDto> findAllSkillDto() {
        return skillRepository.findAll()
                .stream()
                .map(s -> objectMapper.convertValue(s, SkillDto.class))
                .collect(Collectors.toList());
    }
    public Skill findById(Integer id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill with id " + id + " does not exist"));
    }
    public Skill findByName(String name) {
        return skillRepository.findBySkillName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Skill with name " + name + " does not exist"));
    }
    public SkillDto findSkillDtoById(Integer id) {
        return objectMapper.convertValue(findById(id), SkillDto.class);
    }
    public SkillDto findSkillDtoByName(String name) {
        return objectMapper.convertValue(findByName(name), SkillDto.class);
    }

    public List<SkillDto> findSkillsByName(PageDto pageDto) {
        Pageable paging = PageRequest.of(
                pageDto.getPageNo(),
                pageDto.getPageSize(),
                Sort.Direction.fromString(pageDto.getSortDir()),
                pageDto.getSortBy());
        Page<Skill> pagedResult = skillRepository.findSkillsByName(pageDto.getKey(), paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent()
                    .stream()
                    .map(s -> objectMapper.convertValue(s, SkillDto.class))
                    .toList();
        } else {
            return null;
        }
    }

    private List<Skill> findSkillByJob(Integer id) {
        return skillRepository.findByJobId(id);
    }
    public List<SkillDto> findSkillDtoByJob(Integer id) {
        return findSkillByJob(id).stream()
                .map(s -> objectMapper.convertValue(s, SkillDto.class))
                .collect(Collectors.toList());
    }
    @Transactional
    public void updateSkill(Integer id, SkillDto skillDto) {
        Skill updateSkill = findById(id);
        Skill skill = objectMapper.convertValue(skillDto, Skill.class);
        skill.setSkillId(id);
        skillRepository.save(skill);
    }
}
