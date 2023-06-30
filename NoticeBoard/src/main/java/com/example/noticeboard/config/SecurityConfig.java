package com.example.noticeboard.config;

import com.example.noticeboard.dto.security.BoardPrincipal;
import com.example.noticeboard.dto.security.KakaoOAuth2Response;
import com.example.noticeboard.service.UserAccountService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http.authorizeRequests(auth -> auth.anyRequest().permitAll())
//                .formLogin().and()
//                .build();
//    }

    //todo 이 기능이 어떤 역할을 하는지 공부
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService
    ) throws Exception {
        return http.authorizeRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .mvcMatchers(
                                HttpMethod.GET,
                                "/",
                                "/articles",
                                "/articles/search-hashtag"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults())
                .logout(it -> it.logoutSuccessUrl("/"))
                .oauth2Login(auth -> auth
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                        )
                )
                .build();
    }


    //todo 이 기능이 어떤 역할을 하는지 공부
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        //static resource css,js 등 제외하기 위한 로직
//        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }
    //todo 이 기능이 어떤 역할을 하는지 공부

    @Bean
    public UserDetailsService userDetailsService(UserAccountService service) {
        return username -> service
                .searchUser(username)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다. - username : " + username));
    }
    //todo 이 기능이 어떤 역할을 하는지 공부

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(
            UserAccountService service,
            PasswordEncoder encoder
    ) {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        return userRequest -> {
            OAuth2User user = delegate.loadUser(userRequest);
            KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(user.getAttributes());
            String registrationId = userRequest.getClientRegistration().getRegistrationId(); //kakao
            String providerId = String.valueOf(kakaoResponse.id());
            String username = registrationId + "_" + providerId;
            String dummyPassword = encoder.encode("{bcrypt}" + UUID.randomUUID());
            return service.searchUser(username)
                    .map(BoardPrincipal::from)
                    .orElseGet(() -> BoardPrincipal.from(
                            service.saveUser(
                                    username,
                                    dummyPassword,
                                    kakaoResponse.email(),
                                    kakaoResponse.nickname(),
                                    null
                            )
                    ));
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
