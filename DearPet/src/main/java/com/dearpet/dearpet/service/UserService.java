package com.dearpet.dearpet.service;

import com.dearpet.dearpet.dto.LoginRequestDTO;
import com.dearpet.dearpet.dto.UserDTO;
import com.dearpet.dearpet.entity.Cart;
import com.dearpet.dearpet.entity.Role;
import com.dearpet.dearpet.entity.User;
import com.dearpet.dearpet.repository.CartRepository;
import com.dearpet.dearpet.repository.UserRepository;
import com.dearpet.dearpet.repository.RoleRepository;
import com.dearpet.dearpet.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final Map<String, String> verificationCodes = new HashMap<>();
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, CartRepository cartRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
    }

    // 아이디 중복 확인 메서드 추가
    public boolean isUsernameAvailable(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    // 로컬 로그인 처리
    public String login(LoginRequestDTO loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 로컬 사용자일 경우 비밀번호를 검증, 소셜 로그인 사용자는 비밀번호 검증 생략
        if (user.getPassword() != null && !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // JWT 토큰 생성
        return jwtTokenProvider.createToken(user.getUsername(), user.getRole().getRoleName(), user.getUserId());
    }

    // 로그아웃 처리 (JWT 삭제 방식은 클라이언트에서 처리)
    public void logout() {
        // 클라이언트에서 토큰을 삭제하는 방식으로 처리
    }

    // Token refresh method
    public String refreshToken(String token) {
        if (jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsername(token);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Create new JWT token
            return jwtTokenProvider.createToken(user.getUsername(), user.getRole().getRoleName(), user.getUserId());
        } else {
            throw new RuntimeException("Invalid or expired token");
        }
    }

    // 사용자 정보 조회 (username으로 조회)
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found."));
        return new UserDTO(user.getUserId(), user.getUsername(), user.getNickname(), user.getEmail(), user.getRole().getRoleName(), user.getOauth());
    }

    // 사용자 생성 (소셜 로그인과 로컬 로그인 사용자 모두 처리)
    public UserDTO createUser(UserDTO userDTO) {
        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setNickname(userDTO.getNickname());

        // 이메일 설정
        if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
            newUser.setEmail(userDTO.getUsername() + "@carepet.com");  // 임시 이메일 설정
        } else {
            newUser.setEmail(userDTO.getEmail());
        }

        // 소셜 로그인 사용자는 비밀번호가 없을 수 있으므로 비밀번호 설정 조건 추가
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        // 역할 설정 (소셜 로그인과 로컬 로그인 모두 기본 역할 MEMBER 부여)
        String roleName = (userDTO.getRoleName() != null && !userDTO.getRoleName().isEmpty()) ? userDTO.getRoleName() : "member";
        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        newUser.setRole(role);

        // OAuth 여부 설정
        if (userDTO.getOauth() != null) {
            newUser.setOauth(userDTO.getOauth());  // 소셜 로그인 (KAKAO 또는 GOOGLE)
        } else {
            newUser.setOauth(User.OAuthType.NONE);  // 로컬 로그인 사용자는 NONE으로 설정
        }

        newUser.setCreatedAt(LocalDateTime.now());

        // 사용자 저장
        User savedUser = userRepository.save(newUser);


        // 새로운 사용자의 기본 장바구니 생성
        Cart newCart = new Cart();
        newCart.setUser(savedUser); // 저장된 사용자와 장바구니 연결
        newCart.setPrice(BigDecimal.ZERO); // 초기 장바구니 금액
        newCart.setStatus(Cart.CartStatus.OPEN); // 초기 장바구니 상태 설정 (기본값 OPEN)

        cartRepository.save(newCart); // 장바구니 저장

        return new UserDTO(newUser.getUserId(), newUser.getUsername(), newUser.getNickname(), newUser.getEmail(), newUser.getRole().getRoleName(), newUser.getOauth(), newUser.getPassword());
    }

    // 현재 비밀번호 확인 메서드 추가
    public boolean verifyCurrentPassword(String username, String currentPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }

    // 사용자 정보 수정 (username 기반으로)
    public UserDTO updateUserByUsername(String username, UserDTO userDTO) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // 사용자 정보 업데이트
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setNickname(userDTO.getNickname());
        existingUser.setEmail(userDTO.getEmail());

        // 비밀번호 업데이트 처리
        if (userDTO.getCurrentPassword() != null && !userDTO.getCurrentPassword().isEmpty()
                && userDTO.getNewPassword() != null && !userDTO.getNewPassword().isEmpty()) {

            if (passwordEncoder.matches(userDTO.getCurrentPassword(), existingUser.getPassword())) {
                existingUser.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
            } else {
                throw new RuntimeException("현재 비밀번호가 올바르지 않습니다.");
            }
        }

        // 사용자 정보 저장
        userRepository.save(existingUser);

        return new UserDTO(existingUser.getUserId(), existingUser.getUsername(), existingUser.getNickname(), existingUser.getEmail(), existingUser.getRole().getRoleName(), existingUser.getOauth(), existingUser.getPassword());
    }

    // username을 기준으로 사용자 삭제
    @Transactional
    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        userRepository.delete(user);
    }

    // 인증번호 발송 메서드
    public String sendVerificationCode(String email) {
        String code = generateVerificationCode();
        verificationCodes.put(email, code);
        emailService.sendEmail(email, "이메일 인증번호", "인증번호는 " + code + "입니다.");
        return code;
    }

    // 인증번호 검증 메서드
    public boolean verifyCode(String email, String inputCode) {
        String storedCode = verificationCodes.get(email);
        return storedCode != null && storedCode.equals(inputCode);
    }

    // 인증번호 생성
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}