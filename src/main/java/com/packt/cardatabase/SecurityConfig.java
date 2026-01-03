package com.packt.cardatabase;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;

import com.packt.cardatabase.service.UserDetailsServiceImpl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//CORS 관련
import java.util.Arrays;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import static org.springframework.security.config.Customizer.withDefaults;

/*
 ------------------------------------------------------------------------------------------------------------------------
 * 스프링 시큐리티의 작동 방식을 구성하기 위한 구성 클래스, 
 * 클래스 이름은 상관없으나 @EnableWebSecurity, @Configuration, @Bean으로 등록된 SecurityFilterChain 메서드들(보안필터들)이 있어야 함
 * SecurityFilterChain: ***HTTP response 가 들어올때*** 적용되는 보안 필터와 규칙들을 정의한 chain (보안 필터들의 묶음)
 * 보안 필터들: 메서드들
 * 여러 보안 필터들이 차례대로 실행 ==> 인증/인가 ==> Controller에 도달
 *
 ------------------------------------------------------------------------------------------------------------------------
 */

// 보안구성파일에 꼭 필요한 3가지
// Required #1: 이 클래스는 설정 클래스임을 나타내는 annotation
@Configuration
// Required #2: 스프링 시큐리티 활성화하는 annotation
@EnableWebSecurity
public class SecurityConfig {
    /* Required #3: SecurityFilterChain methods
     ------------------------------------------------------------------------------------------------------------------
     * 스프링 시큐리티 설정 클래스에서 SecurityFilterChain에 있는 메서드들은 @Bean으로 등록 (메서드이기 때문)
     * 아래와 같이 userDetailsService()나 passwordEncoder()등은 우리가 만들지만 그 내부는 이미 스프링 시큐리티 라이브러리에서 제공하는
     * 메서드들 (위에 import 참고하면 알수 있음, import org.springframework.security.*). 따라서 빈(@Bean)으로 이용해서
     * 메서드들이 return하는 object를 빈으로 등록
     ------------------------------------------------------------------------------------------------------------------
    */

    /* InMemoryUserDetailsManager: 개발에만 거의 쓰이는 인 메모리 사용자를 추가하는 메서드(실제로는 user DB 사용)
     * ==> user DB를 위한 user Entity class 와 repository class를 작성해야함.
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        // 인 메모리 UserDetails 객체 생성
        UserDetails user = User.builder().username("user") // username은 user
                .password(passwordEncoder().encode("password")) // "password"가 암호, 그리고 bcrypt로 encoding
                .roles("USER").build(); // "USER" role 부여
        return new InMemoryUserDetailsManager(user);
    }
    */

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationFilter authenticationFilter;
    private final AuthEntryPoint exceptionHandler;

    private static final String[] SWAGGER_PATHS = {"/api-docs/**", "/swagger-ui"};

    public SecurityConfig(UserDetailsServiceImpl userDetailsService,  AuthenticationFilter authenticationFilter,
                          AuthEntryPoint exceptionHandler) {
        this.userDetailsService = userDetailsService;
        this.authenticationFilter = authenticationFilter;
        this.exceptionHandler = exceptionHandler;
    }

    public void configureGlobal (AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    // Bcrypt algorithm to encode password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    // filter chain 메소드를 추가
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable())
                //
                .cors(withDefaults())
                //
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                                .requestMatchers(SWAGGER_PATHS).permitAll()
                                .anyRequest().authenticated())
                /* 역할 기반 보안을 추가할 경우 ADMIN, USER, MANAGER와 같은 계층 구조
                 * hasRole은 user가 지정된 role을 가지고 있으면 true를 return
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests.requestMatchers("/admin/**").hasRole("ADMIN")
                                             .requestMatchers("/user/**").hasRole("USER")
                                             .anyRequest().authenticated();
                */
                //
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                //
                .exceptionHandling((exceptionHandling) -> exceptionHandling.
                        authenticationEntryPoint(exceptionHandler));

        return http.build();
    }

    // 클래스 내에 전역 CORS 필터 추가
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(false);
        config.applyPermitDefaultValues();

        // 출처를 명시적으로 정의할려면,
        // localhost:3000 허용
        //config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));8

        source.registerCorsConfiguration("/**", config);
        return source;
    }




}
