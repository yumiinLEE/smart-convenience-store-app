package com.ssafy.smartstore.service;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ssafy.smartstore.dto.Attendance;
import com.ssafy.smartstore.repository.AttendanceRepository;

@Service
public class AttendanceService {
	
	private static final Logger log = LoggerFactory.getLogger(AttendanceService.class);

    private final AttendanceRepository attendanceRepository;
    private final PointService pointService;
    
    public AttendanceService(AttendanceRepository attendanceRepository, PointService pointService) {
        this.attendanceRepository = attendanceRepository;
        this.pointService = pointService;
    }

    public List<String> getAttendanceHistory(String userId) {
    	log.debug("출석 내역");
        return attendanceRepository.findByUserId(userId)
                .stream()
                .map(a -> a.getDate().toString())
                .collect(Collectors.toList());
    }

    public String checkTodayAttendance(String userId) {
        log.debug("출석 체크 요청: userId={}", userId);
        LocalDate today = LocalDate.now();
        boolean alreadyChecked = attendanceRepository.existsByUserIdAndDate(userId, today);

        if (alreadyChecked) {
            return "이미 출석했습니다.";
        }

        // 출석 저장
        Attendance attendance = new Attendance(userId, today);
        attendanceRepository.save(attendance);

        // 포인트 적립 (출석 시 10포인트)
        pointService.savePoint(userId, 10);

        return "출석 완료";
    }

}
