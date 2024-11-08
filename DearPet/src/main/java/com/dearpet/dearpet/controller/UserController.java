package com.dearpet.dearpet.controller;

import com.dearpet.dearpet.dto.VerificationRequestDTO;
import com.dearpet.dearpet.service.AddressService;
import com.dearpet.dearpet.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dearpet.dearpet.dto.UserDTO;
import com.dearpet.dearpet.dto.AddressDTO;
import com.dearpet.dearpet.dto.LoginRequestDTO;
import com.dearpet.dearpet.service.UserService;
import com.dearpet.dearpet.security.JwtTokenProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * User Controller
 * @Author ghpark
 * @Since 2024.10.28
 */

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    private final AddressService addressService;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, AddressService addressService, JwtTokenProvider jwtTokenProvider, EmailService emailService) {
        this.userService = userService;
        this.addressService = addressService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
    }

    // 회원가입
    @PostMapping("/auth/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    // 아이디 중복 확인 엔드포인트
    @GetMapping("/auth/check-username")
    public ResponseEntity<Boolean> checkUsernameAvailability(@RequestParam("username") String username) {
        boolean isAvailable = userService.isUsernameAvailable(username);
        return ResponseEntity.ok(isAvailable);
    }

    // 로그인
    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest) {
        String token = userService.login(loginRequest);
        return ResponseEntity.ok(token);
    }

    // 이메일 인증번호 발송 엔드포인트
    @PostMapping("/auth/send-verification-code")
    public ResponseEntity<Map<String, String>> sendVerificationCode(@RequestBody VerificationRequestDTO request) {
        String email = request.getEmail();
        String verificationCode = emailService.generateVerificationCode(); // 인증 코드 생성 메서드
        emailService.sendEmail(email, "CarePet 이메일 인증 코드", verificationCode);

        Map<String, String> response = new HashMap<>();
        response.put("message", "인증번호가 발송되었습니다.");
        response.put("verificationCode", verificationCode);

        return ResponseEntity.ok(response);
    }

    // 인증번호 확인 엔드포인트
    @PostMapping("/auth/verify-code")
    public ResponseEntity<Boolean> verifyCode(@RequestBody VerificationRequestDTO verificationRequestDTO) {
        boolean isValid = userService.verifyCode(verificationRequestDTO.getEmail(), verificationRequestDTO.getVerificationCode());
        return ResponseEntity.ok(isValid);
    }

    // 기존 비밀번호 확인 엔드포인트 추가
    @PostMapping("/auth/verify-password")
    public ResponseEntity<Boolean> verifyPassword(@RequestHeader("Authorization") String token, @RequestBody String currentPassword) {
        String username = jwtTokenProvider.getUsername(token);
        boolean isPasswordValid = userService.verifyCurrentPassword(username, currentPassword);
        return ResponseEntity.ok(isPasswordValid);
    }

    // 로그아웃
    @GetMapping("/auth/logout")
    public ResponseEntity<String> logout() {
        userService.logout();
        return ResponseEntity.ok("Logged out successfully");
    }

    // 사용자 정보 조회 (토큰 기반)
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(@RequestHeader("Authorization") String token) {
        String username = jwtTokenProvider.getUsername(token);
        UserDTO userProfile = userService.getUserByUsername(username);
        return ResponseEntity.ok(userProfile);
    }

    // 사용자 정보 수정 (토큰 기반)
    @PatchMapping("/profile")
    public ResponseEntity<UserDTO> updateUserProfile(@RequestHeader("Authorization") String token, @RequestBody UserDTO userDTO) {
        String username = jwtTokenProvider.getUsername(token);
        UserDTO updatedUser = userService.updateUserByUsername(username, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // 회원 탈퇴 (토큰 기반)
    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteUser(@RequestHeader("Authorization") String token) {
        String username = jwtTokenProvider.getUsername(token);
        userService.deleteUserByUsername(username);
        return ResponseEntity.noContent().build();
    }

    // Refresh Token Endpoint
    @PostMapping("/auth/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String token) {
        try {
            String newToken = userService.refreshToken(token);
            return ResponseEntity.ok(newToken);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
    }

    // 사용자 배송지 목록 조회 (토큰 기반)
    @GetMapping("/profile/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddresses(@RequestHeader("Authorization") String token) {
        String username = jwtTokenProvider.getUsername(token);
        List<AddressDTO> addresses = addressService.getUserAddresses(username);
        return ResponseEntity.ok(addresses);
    }

    // 배송지 추가 (토큰 기반)
    @PostMapping("/profile/addresses")
    public ResponseEntity<AddressDTO> addUserAddress(@RequestHeader("Authorization") String token, @RequestBody AddressDTO addressDTO) {
        String username = jwtTokenProvider.getUsername(token);
        AddressDTO newAddress = addressService.addUserAddress(username, addressDTO);
        return ResponseEntity.ok(newAddress);
    }

    // 배송지 정보 수정 (토큰 기반)
    @PatchMapping("/profile/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateUserAddress(@RequestHeader("Authorization") String token, @PathVariable("addressId") Long addressId, @RequestBody AddressDTO addressDTO) {
        String username = jwtTokenProvider.getUsername(token);
        AddressDTO updatedAddress = addressService.updateUserAddress(username, addressId, addressDTO);
        return ResponseEntity.ok(updatedAddress);
    }

    // 배송지 삭제 (토큰 기반)
    @DeleteMapping("/profile/addresses/{addressId}")
    public ResponseEntity<Void> deleteUserAddress(@RequestHeader("Authorization") String token, @PathVariable("addressId") Long addressId) {
        String username = jwtTokenProvider.getUsername(token);
        addressService.deleteUserAddress(username, addressId);
        return ResponseEntity.noContent().build();
    }
}