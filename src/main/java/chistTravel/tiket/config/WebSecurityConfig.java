package chistTravel.tiket.config;

import chistTravel.tiket.service.UserJPAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private UserJPAService userJPAService;
    @Autowired
    public void setUserService(UserJPAService userJPAService) {
        this.userJPAService = userJPAService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                                .requestMatchers("/", "/login", "/webjars/**", "/ava_bus.png", "/bus.svg", "signin.css", "style.css", "/registration", "form-validation.js", "/order/*").permitAll()
                                .requestMatchers("/users/details/**").hasRole("DISPATCHER")
                                .requestMatchers("/users/**").hasRole("ADMIN")
                                .requestMatchers("/bus/**").hasRole("ADMIN")
                                .requestMatchers("/drivers/accountDriver").hasRole("DRIVER")
                                .requestMatchers("/drivers/**").hasRole("DISPATCHER")
                                .requestMatchers("/dispatcher/**").hasRole("DISPATCHER")
                                .requestMatchers("/travels/**").hasRole("DRIVER")
                                .requestMatchers("/travels/**").hasRole("DISPATCHER")
                                .anyRequest().authenticated()

                        // разрешить доступ всем
//                        .anyRequest().permitAll()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userJPAService);
        return authenticationProvider;
    }
}