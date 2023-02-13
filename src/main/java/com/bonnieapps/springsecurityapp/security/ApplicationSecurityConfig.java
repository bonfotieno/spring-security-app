package com.bonnieapps.springsecurityapp.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

import static com.bonnieapps.springsecurityapp.security.ApplicationUserPermission.*;
import static com.bonnieapps.springsecurityapp.security.ApplicationUserRole.*;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // to enable roles or permissions being reinforced on controller methods
public class ApplicationSecurityConfig {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /**
         * This method is where we define our web security rules
         * */
        http
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                .csrf().disable()
                .authorizeHttpRequests()  // authorize requests
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(ApplicationUserRole.STUDENT.name()) //role based authentication

//                 permission based Auth
//                .antMatchers( "/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRANEE.name())
//                .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())

                .anyRequest()
                .authenticated() //any request must be authenticated i.e. client must specify the username and passwd
                .and()
                .formLogin() // and the mechanism that we want to reinforce the authenticity of the client is by using FormLogin
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/courses", true)
                    .passwordParameter("password")
                    .usernameParameter("username")
                .and()
                .rememberMe()//defaults to 2 weeks
                    .tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(21))
                    .key("somethingverysecured")
                    .rememberMeParameter("remember-me")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) //to prevent CSRF if CSRF is disabled,
                    //and it means that when ever a client goes to that method with /logout url then log out
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login");

        return http.build();
    }


    @Bean
    protected UserDetailsService userDetailsService(){
        UserDetails mimi = User.builder()
                .username("mimi56")
                .password(passwordEncoder.encode("password") )
//                .roles(ApplicationUserRole.STUDENT.name())  //internally spring will store it like ROLE_STUDENT
                .authorities(STUDENT.getGrantedAuthorities())
                .build();

        UserDetails quill = User.builder()
                .username("quill")
                .password(passwordEncoder.encode("password123") )
//                .roles(ADMIN.name())  //internally spring will store it like ROLE_ADMIN
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        UserDetails tonny = User.builder()
                .username("tonny")
                .password(passwordEncoder.encode("#password123") )
//                .roles(ApplicationUserRole.ADMINTRANEE.name())  //internally spring will store it like ROLE_ADMINTRANEE
                .authorities(ADMINTRANEE.getGrantedAuthorities())
                .build();
        return new InMemoryUserDetailsManager(
                mimi,
                quill,
                tonny
        );
    }
}
