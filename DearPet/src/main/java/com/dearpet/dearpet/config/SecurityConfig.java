package com.dearpet.dearpet.config;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.dearpet.dearpet.security.JwtAuthenticationFilter;
import com.dearpet.dearpet.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.dearpet.dearpet.security.OAuth2AuthenticationSuccessHandler;
import com.dearpet.dearpet.service.CustomOAuth2UserService;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider,
                          OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                          CustomOAuth2UserService customOAuth2UserService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    // 최신 SecurityFilterChain 방식
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .formLogin(form -> form
                        .loginPage("/login")  // Define custom login page for local login
                        .permitAll()           // Allow all users to access the login page
                )
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증 비활성화
                .sessionManagement(AbstractHttpConfigurer::disable) // 세션 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        // 모든 사용자에게 허가
                        .requestMatchers(HttpMethod.GET, "/", "/**").permitAll() // 모든 GET 요청 허용
                        .requestMatchers("/manifest.json", "/favicon.ico","/static/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/check-username").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll() // 상품 조회
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll() // 카테고리 조회
                        .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll() // 리뷰 조회
                        .requestMatchers(HttpMethod.POST, "/api/payments/save").permitAll() // 결제 정보 저장
                        .requestMatchers(HttpMethod.GET, "/api/payments//{impUid}").permitAll() // 결제 정보 조회
                        .requestMatchers(HttpMethod.POST, "/api/auth/send-verification-code").permitAll() // 이메일 인증번호 발송
                        .requestMatchers(HttpMethod.POST, "/api/auth/verify-email").permitAll() // 이메일 인증번호 확인

                        // 인증이 필요한 요청
                        .requestMatchers("/api/cart/**").authenticated()  // 장바구니 기능
                        .requestMatchers("/api/orders/**").authenticated()  // 주문 기능
                        .requestMatchers("/api/pets/**").authenticated()  // 마이펫 관리 기능
                        .requestMatchers("/api/profile/**").authenticated()  // 마이페이지 기능

                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // 인증 실패 시 401 반환
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)) // 사용자 정보를 처리하는 서비스 등록
                        .successHandler(oAuth2AuthenticationSuccessHandler) // OAuth2 로그인 성공 시 처리
                        .failureHandler((request, response, exception) -> {
                            System.out.println("OAuth2 login failed: " + exception.getMessage());
                            exception.printStackTrace();  // 예외 전체를 출력해서 원인 확인
                            response.sendRedirect("/login?error");  // 로그인 실패 시 리디렉션 처리
                        })
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

        return http.build();
    }

    // 비밀번호 암호화를 위한 PasswordEncoder 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager 설정
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://www.carepet.site"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); // 인증된 요청 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}