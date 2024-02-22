package hust.seee.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hust.seee.recruitmentsystem.dto.HistoryDTO;
import hust.seee.recruitmentsystem.entity.Candidate;
import hust.seee.recruitmentsystem.entity.CandidateWorkingHistory;
import hust.seee.recruitmentsystem.exception.ResourceNotFoundException;
import hust.seee.recruitmentsystem.repository.CandidateWorkingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateWorkingHistoryService {
    private final ObjectMapper objectMapper;
    private final CandidateWorkingHistoryRepository historyRepository;

//    public List<CandidateWorkingHistory> addCandidateWorkingHistory(List<CandidateWorkingHistory> list,
//                                                               CandidateWorkingHistoryDto dto) {
//        CandidateWorkingHistory history = mapper.convertValue(dto, CandidateWorkingHistory.class);
//        list.add(history);
//        return list;
//    }

    public Set<HistoryDTO> findByCandidate(Integer candidateId) {
        return historyRepository.findCandidateWorkingHistory(candidateId)
                .stream()
                .map(h -> objectMapper.convertValue(h, HistoryDTO.class))
                .collect(Collectors.toSet());
    }

    //    public List<CandidateWorkingHistory> deleteCandidateWorkingHistory(Integer historyId, List<CandidateWorkingHistory> histories) {
//        histories.forEach(e -> {
//            if (e.getCandidateWorkingHistoryId() == historyId) {
//                histories.remove(e);
//                candidateWorkingHistoryRepository.deleteById(historyId);
//            }
//        });
//        return histories;
//    }
//
//    public List<CandidateWorkingHistory> updateCandidateWorkingHistory(Integer historyId, CandidateWorkingHistoryDto dto, List<CandidateWorkingHistory> histories) {
//        for (int i = 0; i < histories.size(); i++) {
//            if (histories.get(i).getCandidateWorkingHistoryId() == historyId) {
//                histories.remove(i);
//                CandidateWorkingHistory edu = mapper.convertValue(dto, CandidateWorkingHistory.class);
//                edu.setCandidateWorkingHistoryId(historyId);
//                histories.add(i, edu);
//            }
//        }
//        return histories;
//    }
    public void delete(Integer historyId) {
        historyRepository.deleteById(historyId);
    }

    public void save(Candidate candidate, HistoryDTO dto) {
        if (Objects.isNull(dto.getId())) {
            create(candidate, dto);
        } else {
            update(candidate, dto);
        }
    }

    public void create(Candidate candidate, HistoryDTO dto) {
        historyRepository.save(checkValid(candidate, dto));
    }

    private CandidateWorkingHistory checkValid(Candidate candidate, HistoryDTO dto) {
        CandidateWorkingHistory c = objectMapper.convertValue(dto, CandidateWorkingHistory.class);
        c.setCandidate(candidate);
        return c;
    }

    public void update(Candidate candidate, HistoryDTO _dto) {
        Optional<CandidateWorkingHistory> op = historyRepository.findById(_dto.getId());
        CandidateWorkingHistory history = checkValid(candidate, _dto);
        if (op.isPresent()) {
            CandidateWorkingHistory r = op.get();
            r.setCompanyName(history.getCompanyName());
            r.setJobName(history.getJobName());
            r.setStartDate(history.getStartDate());
            r.setEndDate(history.getEndDate());
            r.setDescription(history.getDescription());
            historyRepository.save(r);
        } else {
            throw new ResourceNotFoundException("Không tìm thấy kinh nghiệm!");
        }
    }
}
