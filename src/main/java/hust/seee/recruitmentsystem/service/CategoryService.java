package hust.seee.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hust.seee.recruitmentsystem.dto.CategoryDTO;
import hust.seee.recruitmentsystem.entity.Category;
import hust.seee.recruitmentsystem.exception.ResourceAlreadyExistsException;
import hust.seee.recruitmentsystem.exception.ResourceNotFoundException;
import hust.seee.recruitmentsystem.pagination.PageDto;
import hust.seee.recruitmentsystem.repository.CategoryRepository;
import hust.seee.recruitmentsystem.utils.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    public void save(CategoryDTO category) {
        if (Objects.isNull(category.getId())) {
            create(category);
        } else {
            update(category);
        }
    }

    public void create(CategoryDTO category) {
        categoryRepository.save(checkValid(category));
    }

    private Category checkValid(CategoryDTO dto) {
        if (categoryRepository.countByNameAndCode(
                dto.getId(),
                DataFormat.lower(dto.getName()),
                DataFormat.lower(dto.getCode())) > 0) {
            throw new ResourceAlreadyExistsException("Tên ngành nghề/mã ngành nghề đã tồn tại");
        }
        return objectMapper.convertValue(dto, Category.class);
    }

    public void update(CategoryDTO _category) {
        Optional<Category> op = categoryRepository.findById(_category.getId());
        Category category = checkValid(_category);
        if (op.isPresent()) {
            Category r = op.get();
            r.setName(category.getName());
            r.setCode(category.getCode());
            categoryRepository.save(r);
        } else {
            throw new ResourceNotFoundException("Không tìm thấy ngành nghề!");
        }
    }

    @Transactional
    public void delete(Integer id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ngành nghề!"));
        categoryRepository.delete(category);
    }

    public Page<CategoryDTO> findAll(CategoryDTO category) {
        Pageable pageable = PageRequest.of(category.getPage() - 1, category.getSize());
        return categoryRepository.findAll(DataFormat.lower(category.getName()), pageable)
                .map(r -> objectMapper.convertValue(r, CategoryDTO.class));
    }

    public CategoryDTO findDTOById(Integer id) {
        Optional<Category> op = categoryRepository.findById(id);
        if (op.isPresent()) {
            return objectMapper.convertValue(op.get(), CategoryDTO.class);
        } else {
            throw new ResourceNotFoundException("Không tìm thấy ngành nghề!");
        }
    }

    public Category findById(Integer id) {
        Optional<Category> op = categoryRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        } else {
            throw new ResourceNotFoundException("Không tìm thấy ngành nghề!");
        }
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name).get();
    }

    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> objectMapper.convertValue(c, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    public List<CategoryDTO> findPaging(PageDto pageDto) {
        Pageable paging = PageRequest.of(
                pageDto.getPageNo(),
                pageDto.getPageSize(),
                Sort.Direction.fromString(pageDto.getSortDir()),
                pageDto.getSortBy());

        Page<Category> pagedResult = categoryRepository.findAll(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent()
                    .stream()
                    .map(c -> objectMapper.convertValue(c, CategoryDTO.class))
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }
}
