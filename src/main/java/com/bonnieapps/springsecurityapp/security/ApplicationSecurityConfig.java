package com.bonnieapps.springsecurityapp.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()  // authorize requests
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(ApplicationUserRole.STUDENT.name()) //role based authentication
                .anyRequest()
                .authenticated() //any request must be authenticated i.e. client must specify the username and passwd
                .and()
                .httpBasic(); // and the mechanism that we want to reinforce the authenticity of the client is by using BasicAuth
        return http.build();
    }

    @Bean
    protected UserDetailsService userDetailsService(){
        UserDetails mimi = User.builder()
                .username("mimi56")
                .password(passwordEncoder.encode("password") )
                .roles(ApplicationUserRole.STUDENT.name())  //internally spring will store it like ROLE_STUDENT
                .build();

        UserDetails quill = User.builder()
                .username("quill")
                .password(passwordEncoder.encode("password123") )
                .roles(ApplicationUserRole.ADMIN.name())  //internally spring will store it like ROLE_ADMIN
                .build();
        return new InMemoryUserDetailsManager(
                mimi,
                quill
        );
    }
}
