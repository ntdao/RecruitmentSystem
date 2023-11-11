package com.recruitmentsystem.industry;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IndustryService {
    private final IndustryMapper industryMapper;
    private final IndustryRepository industryRepository;
    public void addIndustry(IndustryRequestModel request) {
        checkDuplicatedIndustryName(request.nameEN(), request.nameVI());
        Industry industry = industryMapper.industryRequestModelToIndustry(request);
        industryRepository.save(industry);
    }
    private void checkDuplicatedIndustryName(String nameEN, String nameVI) {
        if (industryRepository.existsIndustryByIndustryName(nameEN)) {
            throw new ResourceAlreadyExistsException("Industry EN name already taken");
        }
        if (industryRepository.existsIndustryByIndustryNameVI(nameVI)) {
            throw new ResourceAlreadyExistsException("Industry VI name already taken");
        }
    }
    public void deleteIndustry(Integer id) {
        Industry industry = findById(id);
        industryRepository.delete(industry);
    }
    public List<Industry> findAll() {
        return industryRepository.findAll();
    }
    public List<IndustryResponseModel> findAllIndustryResponseModel() {
        return industryRepository.findAll()
                .stream()
                .map(industryMapper::industryToResponseModel)
                .collect(Collectors.toList());
    }
    public Industry findById(Integer id) {
        return industryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Industry with id " + id + " does not exist"));
    }
    public Industry findByName(String name) {
        return industryRepository.findByIndustryNameVI(name)
                .orElseThrow(() -> new ResourceNotFoundException("Industry with name " + name + " does not exist"));
    }
    public IndustryResponseModel findIndustryResponseModelById(Integer id) {
        return industryMapper.industryToResponseModel(findById(id));
    }
    public IndustryResponseModel findIndustryResponseModelByName(String name) {
        return industryMapper.industryToResponseModel(findByName(name));
    }
    @Transactional
    public void updateIndustry(Integer id, IndustryRequestModel request) {
        Industry updateIndustry = findById(id);
        Industry industryRequest = industryMapper.industryRequestModelToIndustry(request);
        industryRequest.setIndustryId(id);
        industryRepository.save(updateIndustry);
    }
}


