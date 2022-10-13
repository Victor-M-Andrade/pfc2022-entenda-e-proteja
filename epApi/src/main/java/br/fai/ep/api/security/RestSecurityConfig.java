package br.fai.ep.api.security;

import br.fai.ep.api.security.filter.JWTAuthorizationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.
                csrf().disable()
                .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                // user
                .antMatchers(HttpMethod.GET, "/api/user/read-by-id-for-update-password/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/user/update-forgotten-user-password").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/authentication").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/create").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/forgot-password").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/send-mail").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/update").permitAll()

                // news
                .antMatchers(HttpMethod.POST, "/api/news/last-news-by-dto-criteria-with-limit/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/news/read-by-dto-criteria").permitAll()
                .antMatchers(HttpMethod.GET, "/api/news/read-by-dto-id/**").permitAll()

                // question
                .antMatchers(HttpMethod.POST, "/api/question/read-by-dto-criteria").permitAll()

                // partner
                .antMatchers(HttpMethod.POST, "/api/partner/read-by-criteria").permitAll()
                .anyRequest().authenticated();
    }
}