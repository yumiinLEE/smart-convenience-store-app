package com.ssafy.smartstore.controller;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.smartstore.dto.User;
import com.ssafy.smartstore.dto.UserRequest;
import com.ssafy.smartstore.repository.UserRepository;
import com.ssafy.smartstore.service.UserService;

@RestController
@RequestMapping("api/users")
public class UserController {
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    // 회원 가입
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequest userRequest) {
    	log.debug("회원가입");

        if (userRepository.existsById(userRequest.getId())) {
            return ResponseEntity.badRequest().body("이미 존재하는 ID입니다.");
        }
        
        User user = new User();
        user.setId(userRequest.getId());
        user.setPass(userRequest.getPass());
        user.setName(userRequest.getName());
        user.setPoint(0);
        user.setPayment(0);

        userRepository.save(user);
        return ResponseEntity.ok("회원가입 성공");
    }
    
    // 로그인
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserRequest userRequest) {
        log.debug("로그인");

        Optional<User> user = userRepository.findById(userRequest.getId());

        if (user.isPresent() && user.get().getPass().equals(userRequest.getPass())) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // id로 회원찾기
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") String id) {
    	log.debug("회원 정보");
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 닉네임 수정
    @PutMapping("/{id}/name")
    public ResponseEntity<User> updateUserName(@PathVariable("id") String id, @RequestBody Map<String, String> request) {
        String newName = request.get("name");
        if (newName == null || newName.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        User updatedUser = userService.updateUserName(id, newName);
        return ResponseEntity.ok(updatedUser);
    }

}
