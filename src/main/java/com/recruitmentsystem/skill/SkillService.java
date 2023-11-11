package com.recruitmentsystem.skill;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillService {
    private final SkillMapper skillMapper;
    private final SkillRepository skillRepository;
    public void addSkill(SkillRequestModel request) {
        checkDuplicatedSkillName(request.nameEN(), request.nameVI());
        Skill skill = skillMapper.skillRequestModelToSkill(request);
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
    public List<SkillResponseModel> findAllSkillResponseModel() {
        return skillRepository.findAll()
                .stream()
                .map(skillMapper::skillToResponseModel)
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
    public SkillResponseModel findSkillResponseModelById(Integer id) {
        return skillMapper.skillToResponseModel(findById(id));
    }
    public SkillResponseModel findSkillResponseModelByName(String name) {
        return skillMapper.skillToResponseModel(findByName(name));
    }
    @Transactional
    public void updateSkill(Integer id, SkillRequestModel request) {
        Skill updateSkill = findById(id);
        Skill skillRequest = skillMapper.skillRequestModelToSkill(request);
        skillRequest.setSkillId(id);
        skillRepository.save(updateSkill);
    }
}
