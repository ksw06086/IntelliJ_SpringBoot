package com.suncloth.suncloth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;

// security 적용 및 설정을 위한 클래스
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    // 데이터베이스 연동 클래스 생성
    @Autowired
    private DataSource dataSource;

    /*@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        //허용할 url 설정
        configuration.addAllowedOrigin("https://api.iamport.kr/users/getToken");
        //허용할 헤더 설정
        configuration.addAllowedHeader("*");
        //허용할 http method
        configuration.addAllowedMethod("*");
        //사용자 자격 증명이 지원되는지 여부
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }*/

    // 페이지별 권한 부여 메소드 : requestMatchers에 표시된 페이지 및 경로는 모든 사용자 사용 가능 그 이외는 로그인된 사용자만 사용 가능
    // formLogin - 로그인 페이지 지정해주는 것
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // http.cors().configurationSource(corsConfigurationSource());
        http
                .csrf().disable() // 이거 하면 보안에 취약하나 테스트 원활히 가능, 이거 없으면 추가,수정이 어려움
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/main", "/upload**","/css/**", "/js/**", "/account/register", "/account/register_finish", "/api/**", "/smsApi/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/account/login")
                        .defaultSuccessUrl("/account/loginComplete", true)
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // 데이터베이스 내 데이터와 시큐리티 연결하는 메소드
    // usersByUsernameQuery : 로그인 정보 가져오는 부분
    // authoritiesByUsernameQuery : 페이지랑 연결시키는 위해 해당 로그인의 이름과 권한명을 가져오는 부분
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled "
                        + "from user_tbl "
                        + "where username = ?")
                .authoritiesByUsernameQuery("select u.username, r.name "
                        + "from user_role ur inner join user_tbl u on ur.user_id = u.id "
                        + "inner join role_tbl r on ur.role_id = r.id "
                        + "where u.username = ?");
    }
    // Authentication : 로그인 처리
    // Authorization : 권한
    //// 조인 ////
    // @OneToOne
    // ex) user - user_detail
    // @OneToMany
    // ex) user - board
    // @ManyToOne
    // ex) board - user(여러개 보드 하나의 사용자, 하나의 사용자 여러개 보드)
    // @ManyToMany
    // user - role(하나의 사용자는 여러개 권한, 하나의 권한은 여러개 사용자)

}

