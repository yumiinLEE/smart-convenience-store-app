package com.ssafy.smartstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.smartstore.dto.Attendance;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByUserId(String userId);
    boolean existsByUserIdAndDate(String userId, LocalDate date);
}