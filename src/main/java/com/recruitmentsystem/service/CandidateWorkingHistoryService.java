package com.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.dto.CandidateWorkingHistoryDto;
import com.recruitmentsystem.entity.CandidateWorkingHistory;
import com.recruitmentsystem.repository.CandidateWorkingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateWorkingHistoryService {
    private final ObjectMapper mapper;
    private final CandidateWorkingHistoryRepository candidateWorkingHistoryRepository;

    public List<CandidateWorkingHistory> addCandidateWorkingHistory(List<CandidateWorkingHistory> list,
                                                               CandidateWorkingHistoryDto dto) {
        CandidateWorkingHistory history = mapper.convertValue(dto, CandidateWorkingHistory.class);
        list.add(history);
        return list;
    }

    public List<CandidateWorkingHistoryDto> findByCandidate(Integer candidateId) {
        return candidateWorkingHistoryRepository.findCandidateWorkingHistory(candidateId)
                .stream()
                .map(h -> mapper.convertValue(h, CandidateWorkingHistoryDto.class))
                .collect(Collectors.toList());
    }

    public List<CandidateWorkingHistory> deleteCandidateWorkingHistory(Integer historyId, List<CandidateWorkingHistory> histories) {
        histories.forEach(e -> {
            if (e.getCandidateWorkingHistoryId() == historyId) {
                histories.remove(e);
                candidateWorkingHistoryRepository.deleteById(historyId);
            }
        });
        return histories;
    }

    public List<CandidateWorkingHistory> updateCandidateWorkingHistory(Integer historyId, CandidateWorkingHistoryDto dto, List<CandidateWorkingHistory> histories) {
        for (int i = 0; i < histories.size(); i++) {
            if (histories.get(i).getCandidateWorkingHistoryId() == historyId) {
                histories.remove(i);
                CandidateWorkingHistory edu = mapper.convertValue(dto, CandidateWorkingHistory.class);
                edu.setCandidateWorkingHistoryId(historyId);
                histories.add(i, edu);
            }
        }
        return histories;
    }
}
