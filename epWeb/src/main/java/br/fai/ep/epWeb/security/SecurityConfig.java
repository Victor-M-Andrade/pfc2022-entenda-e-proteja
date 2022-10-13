package br.fai.ep.epWeb.security;

import br.fai.ep.epEntities.Usuario.ROLE_NAME;
import br.fai.ep.epWeb.security.provider.EpAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    EpAuthenticationProvider authenticationProvider = new EpAuthenticationProvider();

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // resources and images
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/images/news/**").permitAll()
                .antMatchers("/images/partners/**").permitAll()
                .antMatchers("/images/users/**").hasRole(ROLE_NAME.USER)
                // home
                .antMatchers("/").permitAll()
                .antMatchers("/not-found").permitAll()
                .antMatchers("/about-project").permitAll()
                .antMatchers("/contact-us").permitAll()
                .antMatchers("/send-email").permitAll()
                .antMatchers("/privacy-policy").permitAll()
                .antMatchers("/terms-conditions").permitAll()

                // account
                .antMatchers("/account/register").permitAll()
                .antMatchers("/account/create").permitAll()
                .antMatchers("/account/forgot-my-password").permitAll()
                .antMatchers("/account/request-password-change").permitAll()
                .antMatchers("/account/check-exists-user/**").permitAll()
                .antMatchers("/account/change-user-password/**").permitAll()
                .antMatchers("/account/update-user-password").permitAll()
                .antMatchers("/account/confirm-delete-account/**").hasRole(ROLE_NAME.USER)
                .antMatchers("/account/request-use-data/**").hasRole(ROLE_NAME.USER)
                .antMatchers("/account/confirm-delete-my-account/**").hasRole(ROLE_NAME.USER)
                .antMatchers("/account/anonymize-my-account/**").hasRole(ROLE_NAME.USER)

                // Partner
                .antMatchers("/partner/list").permitAll()
                .antMatchers("/partner/detail/**").permitAll()
                .antMatchers("/partner/register/**").hasAnyRole(ROLE_NAME.USER, ROLE_NAME.PARTNER)
                .antMatchers("/partner/request-register").hasAnyRole(ROLE_NAME.USER, ROLE_NAME.PARTNER)
                .antMatchers("/partner/my-data-as-partner/**").hasAnyRole(ROLE_NAME.USER, ROLE_NAME.PARTNER)
                .antMatchers("/partner/edit-my-data-as-partner/**").hasAnyRole(ROLE_NAME.USER, ROLE_NAME.PARTNER)
                .antMatchers("/partner/update-my-data-as-partner").hasAnyRole(ROLE_NAME.USER, ROLE_NAME.PARTNER)
                .antMatchers("/partner/delete-my-data-as-partner/**").hasAnyRole(ROLE_NAME.USER, ROLE_NAME.PARTNER)
                .antMatchers("/partner/new-registration-requestl/**").hasAnyRole(ROLE_NAME.USER, ROLE_NAME.PARTNER)

                // news
                .antMatchers("/news/categories").permitAll()
                .antMatchers("/news/news-by-category/**").permitAll()
                .antMatchers("/news/news-list").permitAll()
                .antMatchers("/news/new/**").permitAll()
                .antMatchers("/news/create-new-news/**").hasAnyRole(ROLE_NAME.USER, ROLE_NAME.AUTHOR)
                .antMatchers("/news/request-news-publication").hasAnyRole(ROLE_NAME.USER, ROLE_NAME.AUTHOR)
                .antMatchers("/news/user-news-list/**").hasAnyRole(ROLE_NAME.USER, ROLE_NAME.AUTHOR)
                .antMatchers("/news/user-news-detail/**").hasAnyRole(ROLE_NAME.USER, ROLE_NAME.AUTHOR)
                .antMatchers("/news/delete-user-news/**").hasAnyRole(ROLE_NAME.USER, ROLE_NAME.AUTHOR)
                .antMatchers("/news/new-publication-request/**").hasAnyRole(ROLE_NAME.USER, ROLE_NAME.AUTHOR)
                .antMatchers("/news/user-news-edit/**").hasAnyRole(ROLE_NAME.USER, ROLE_NAME.AUTHOR)
                .antMatchers("/news/update-user-news/**").hasAnyRole(ROLE_NAME.USER, ROLE_NAME.AUTHOR)

                // question
                .antMatchers("/question/list").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/question/register").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/question/create-quest").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/question/detail/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/question/edit-question/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/question/update-question").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/question/delete-question/**").hasRole(ROLE_NAME.ADMINISTRATOR)

                // knowledge-test
                .antMatchers("/knowledge-test/select-level").permitAll()
                .antMatchers("/knowledge-test/test/**").permitAll()

                // administrator-user-area
                .antMatchers("/user/administrator-area").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/user/read-all").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/user/admin-profile/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/user/admin-edit/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/user/admin-update-user-data").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/user/admin-delete/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/user/admin-anonymize-user/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/user/remove-user-anonymization/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/user/give-admin-permission/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/user/remove-admin-permission/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                // administrator-partner-area
                .antMatchers("/partner/admin-list").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/partner/admin-detail/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/partner/edit-partner-data/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/partner/update-partner-data").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/partner/deactivate-partner/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/partner/reactivate-partner/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/partner/delte-partner/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/partner/request-register-list").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/partner/evaluate-registration-request/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/partner/approve-registration-request/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/partner/reprove-registration-request/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/partner/reproved-register-list").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/partner/new-evaluate-registration-request/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/partner/new-approve-registration-request/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                // administrator-news-area
                .antMatchers("/news/publication-request-list").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/news/rejected-news-list").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/news/evaluate-news/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/news/new-evaluate-news/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/news/approve-news-publication/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/news/reject-news-publication/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/news/new-approve-news-publication/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/news/admin-list").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/news/admin-news-detail/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/news/deactivate-news/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/news/activate-news/**").hasRole(ROLE_NAME.ADMINISTRATOR)
                .antMatchers("/news/admin-user-news-lists/**").hasRole(ROLE_NAME.ADMINISTRATOR)

                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/account/login")
                .loginProcessingUrl("/account/authenticate").permitAll()
                .defaultSuccessUrl("/account/get-id-authenticated-user")
                .failureUrl("/account/login-error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("account/log-out"))
                .logoutSuccessUrl("/").and()
                .exceptionHandling().accessDeniedPage("/not-found");
    }
}