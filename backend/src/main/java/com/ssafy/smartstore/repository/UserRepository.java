package com.ssafy.smartstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssafy.smartstore.dto.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.point = u.point + :amount WHERE u.id = :userId")
	int addPointToUser(@Param("userId") String userId, @Param("amount") int amount);

	
}