package com.ssafy.smartstore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssafy.smartstore.service.AttendanceService;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // 출석 내역 조회
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<String>> getHistory(@PathVariable("userId") String userId) {
        List<String> dates = attendanceService.getAttendanceHistory(userId);
        return ResponseEntity.ok(dates);
    }

    // 오늘 출석 체크
    @PostMapping("/check/{userId}")
    public ResponseEntity<String> checkToday(@PathVariable("userId") String userId) {
        String result = attendanceService.checkTodayAttendance(userId);
        return ResponseEntity.ok(result);
    }
}