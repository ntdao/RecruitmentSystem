package com.recruitmentsystem.industry;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;
    private final IndustryRepository industryRepository;

    @Transactional
    public void addIndustry(IndustryRequestModel request) {
        checkDuplicatedIndustryName(request.industryName(), request.industryNameVI());
        Industry industry = objectMapper.convertValue(request, Industry.class);
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
                .map(i -> objectMapper.convertValue(i, IndustryResponseModel.class))
                .collect(Collectors.toList());
    }

    public Industry findById(Integer id) {
        return industryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Industry with id " + id + " does not exist"));
    }

    public Industry findIndustryByName(String name) {
        return industryRepository.findByIndustryNameVI(name)
                .orElseThrow(() -> new ResourceNotFoundException("Industry with name " + name + " does not exist"));
    }

    public List<Industry> findByName(String name) {
        return industryRepository.findByIndustryNameVIContains(name);
    }

    public IndustryResponseModel findIndustryResponseModelById(Integer id) {
        return objectMapper.convertValue(findById(id), IndustryResponseModel.class);
    }

    public IndustryResponseModel findIndustryResponseModelByName(String name) {
        return objectMapper.convertValue(findIndustryByName(name), IndustryResponseModel.class);
    }

    @Transactional
    public void updateIndustry(Integer id, IndustryRequestModel request) {
        Industry updateIndustry = findById(id);
        Industry industryRequest = objectMapper.convertValue(request, Industry.class);
        industryRequest.setIndustryId(id);
        industryRepository.save(updateIndustry);
    }
}


