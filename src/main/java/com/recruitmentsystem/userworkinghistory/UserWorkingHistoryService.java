package com.recruitmentsystem.userworkinghistory;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserWorkingHistoryService {
    private final ObjectMapper mapper;
    private final UserWorkingHistoryRepository userWorkingHistoryRepository;

    public List<UserWorkingHistory> addUserWorkingHistory(List<UserWorkingHistory> list,
                                                          UserWorkingHistoryDto dto) {
        UserWorkingHistory history = mapper.convertValue(dto, UserWorkingHistory.class);
        list.add(history);
        return list;
    }

    public List<UserWorkingHistoryDto> findByUser(Integer userId) {
        return userWorkingHistoryRepository.findUserWorkingHistory(userId)
                .stream()
                .map(h -> mapper.convertValue(h, UserWorkingHistoryDto.class))
                .collect(Collectors.toList());
    }

    public List<UserWorkingHistory> deleteUserWorkingHistory(Integer historyId, List<UserWorkingHistory> histories) {
        histories.forEach(e -> {
            if (e.getUserWorkingHistoryId() == historyId) {
                histories.remove(e);
                userWorkingHistoryRepository.deleteById(historyId);
            }
        });
        return histories;
    }

    public List<UserWorkingHistory> updateUserWorkingHistory(Integer historyId, UserWorkingHistoryDto dto, List<UserWorkingHistory> histories) {
        for (int i = 0; i < histories.size(); i++) {
            if (histories.get(i).getUserWorkingHistoryId() == historyId) {
                histories.remove(i);
                UserWorkingHistory edu = mapper.convertValue(dto, UserWorkingHistory.class);
                edu.setUserWorkingHistoryId(historyId);
                histories.add(i, edu);
            }
        }
        return histories;
    }
}
