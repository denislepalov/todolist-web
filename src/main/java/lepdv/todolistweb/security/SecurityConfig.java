package lepdv.todolistweb.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static lepdv.todolistweb.entity.Role.ADMIN;
import static lepdv.todolistweb.entity.Role.USER;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@PropertySource("classpath:myApp.properties")
public class SecurityConfig {

    private final String[] PUBLIC_PATHS =
            {
                    "/start-page",
                    "/authenticate/**",
                    "/error"
            };

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(PUBLIC_PATHS).permitAll()
                        .anyRequest().hasAnyAuthority(USER.getAuthority(), ADMIN.getAuthority()))
                .formLogin(formLogin -> formLogin
                        .loginPage("/authenticate/login")
                        .defaultSuccessUrl("/tasks/todo-list", true)
                        .failureUrl("/authenticate/login?error"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/authenticate/login"))
                .build();
    }


}
