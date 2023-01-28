package com.burakkolay.credit.config;


import com.burakkolay.credit.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
    }

    protected void configure(HttpSecurity httpSecurity)throws Exception{
        httpSecurity.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/api/v1/product/showAllProducts").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/api/v1/product/categoryList").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/api/v1/product/showAllProductsByUsername").hasAuthority("ROLE_USER")
                        .and()
                                .formLogin(form -> form.defaultSuccessUrl("/api/v1/applicant/showList")
                                        .loginPage("/login")
                                       );


        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
    }

}
