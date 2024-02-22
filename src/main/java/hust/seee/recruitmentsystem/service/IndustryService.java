package hust.seee.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hust.seee.recruitmentsystem.dto.IndustryDTO;
import hust.seee.recruitmentsystem.entity.Industry;
import hust.seee.recruitmentsystem.exception.ResourceAlreadyExistsException;
import hust.seee.recruitmentsystem.exception.ResourceNotFoundException;
import hust.seee.recruitmentsystem.repository.IndustryRepository;
import hust.seee.recruitmentsystem.utils.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class IndustryService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private IndustryRepository industryRepository;

    public void save(IndustryDTO industry) {
        if (Objects.isNull(industry.getId())) {
            create(industry);
        } else {
            update(industry);
        }
    }

    public void create(IndustryDTO industry) {
        industryRepository.save(checkValid(industry));
    }

    private Industry checkValid(IndustryDTO dto) {
        if (industryRepository.countByNameAndCode(
                dto.getId(),
                DataFormat.lower(dto.getName()),
                DataFormat.lower(dto.getCode())) > 0) {
            throw new ResourceAlreadyExistsException("Tên lĩnh vực hoạt động/mã lĩnh vực hoạt động đã tồn tại");
        }
        return objectMapper.convertValue(dto, Industry.class);
    }

    public void update(IndustryDTO _industry) {
        Optional<Industry> op = industryRepository.findById(_industry.getId());
        Industry industry = checkValid(_industry);
        if (op.isPresent()) {
            Industry r = op.get();
            r.setName(industry.getName());
            r.setCode(industry.getCode());
            industryRepository.save(r);
        } else {
            throw new ResourceNotFoundException("Không tìm thấy lĩnh vực hoạt động!");
        }
    }

    public Page<IndustryDTO> findAll(IndustryDTO industry) {
        Pageable pageable = PageRequest.of(industry.getPage() - 1, industry.getSize());
        return industryRepository.findAll(DataFormat.lower(industry.getName()), pageable)
                .map(r -> objectMapper.convertValue(r, IndustryDTO.class));
    }

    public IndustryDTO findDTOById(Integer id) {
        Optional<Industry> op = industryRepository.findById(id);
        if (op.isPresent()) {
            return objectMapper.convertValue(op.get(), IndustryDTO.class);
        } else {
            throw new ResourceNotFoundException("Không tìm thấy lĩnh vực hoạt động!");
        }
    }

    public Industry findById(Integer id) {
        Optional<Industry> op = industryRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        } else {
            throw new ResourceNotFoundException("Không tìm thấy lĩnh vực hoạt động!");
        }
    }

    @Transactional
    public void delete(Integer id) {
        Industry industry = industryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lĩnh vực hoạt động!"));
        industryRepository.delete(industry);
    }

    public Industry findByName(String name) {
        return industryRepository.findByName(name).get();
    }

    public List<IndustryDTO> findAll() {
        return industryRepository.findAll().stream().map(x -> objectMapper.convertValue(x, IndustryDTO.class)).toList();
    }
}

