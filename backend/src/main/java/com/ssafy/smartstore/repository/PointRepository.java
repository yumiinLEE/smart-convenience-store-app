package com.ssafy.smartstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.smartstore.dto.Point;

public interface PointRepository extends JpaRepository<Point, String> {
}
