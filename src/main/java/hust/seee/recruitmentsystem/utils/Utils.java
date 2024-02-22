package hust.seee.recruitmentsystem.utils;

import hust.seee.recruitmentsystem.dto.StatisticDTO;
import hust.seee.recruitmentsystem.dto.StatisticDetailDTO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {
    public static String calculateTimeAgo(LocalDateTime createDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createDate, now);

        if (duration.toMinutes() < 1) {
            return "Vừa xong";
        } else if (duration.toHours() < 1) {
            long minutes = duration.toMinutes();
            return minutes + " phút trước";
        } else if (duration.toDays() < 1) {
            long hours = duration.toHours();
            return hours + " giờ trước";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return createDate.format(formatter);
        }
    }

    public static StatisticDTO getStatistic(List<Map<String, Object>> map) {
        List<StatisticDetailDTO> dto = new ArrayList<>();
        final long[] total = {0};
        map.forEach(m -> {
                    dto.add(new StatisticDetailDTO(
                            m.get("month").toString(),
                            m.get("year").toString(),
                            m.get("quantity").toString())
                    );
                    total[0] += (Long) m.get("quantity");
                }
        );
        return StatisticDTO.builder()
                .dto(dto)
                .total(total[0])
                .build();
    }
}
