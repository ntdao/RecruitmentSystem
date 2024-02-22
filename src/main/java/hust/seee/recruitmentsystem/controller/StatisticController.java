//package hust.seee.recruitmentsystem.controller;
//
//import hust.seee.recruitmentsystem.dto.StatisticDTO;
//import hust.seee.recruitmentsystem.service.StatisticService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/v1/statistic")
//@RequiredArgsConstructor
//public class StatisticController {
//    private final StatisticService service;
//
//    @GetMapping("/candidate")
//    public StatisticDTO candidateQuantity() {
//        return service.getCandidate();
//    }
//
//    @GetMapping("/company")
//    public StatisticDTO companyQuantity() {
//        return service.getCompany();
//    }
//
//    @GetMapping("/job")
//    public StatisticDTO jobQuantity() {
//        return service.getJob();
//    }
//
//    @GetMapping("/system")
//    public Map<String, Object> jobQuantityByCompany() {
//        return service.getAll();
//    }
//
//    @GetMapping("/{companyId}")
//    public Map<String, Object> jobQuantityByCompany(@PathVariable("companyId") Integer companyId) {
//        return service.getByCompany(companyId);
//    }
//
//    @GetMapping("/recruitment")
//    public StatisticDTO recruitmentQuantity() {
//        return null;
//    }
//
//    @GetMapping("/pass")
//    public Integer passQuantity() {
//        return null;
//    }
//
//    @GetMapping("/fail")
//    public Integer failQuantity() {
//        return null;
//    }
//}
