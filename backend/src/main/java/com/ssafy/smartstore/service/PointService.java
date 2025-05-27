package com.ssafy.smartstore.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ssafy.smartstore.dto.Point;
import com.ssafy.smartstore.repository.PointRepository;
import com.ssafy.smartstore.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PointService {
	
	private static final Logger log = LoggerFactory.getLogger(PointService.class);

    private final PointRepository pointRepository;
    private final UserRepository userRepository;

    public PointService(PointRepository pointRepository, UserRepository userRepository) {
        this.pointRepository = pointRepository;
        this.userRepository = userRepository;
    }

    public void savePoint(String userId, int amount) {
        String generatedId = UUID.randomUUID().toString();
        log.debug("포인트 저장 시작: userId={}, point={}, id={}", userId, amount, generatedId);

        pointRepository.save(new Point(generatedId, userId, "", amount));

        // 실제로 몇 개의 row가 영향을 받았는지 확인
        int affected = userRepository.addPointToUser(userId, amount);
        log.debug("User 포인트 갱신 rows affected: {}", affected);
    }
}