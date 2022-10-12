package br.fai.ep.epWeb.security.provider;

import br.fai.ep.epEntities.Usuario;
import br.fai.ep.epWeb.security.CustomUserDetail;
import br.fai.ep.epWeb.service.impl.UserWebServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class EpAuthenticationProvider implements AuthenticationProvider {
    private final UserWebServiceImpl userWebService = new UserWebServiceImpl();

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String userEmail = authentication.getName();
        final String password = authentication.getCredentials().toString();
        System.out.println("Useremail: " + userEmail + " | Password: " + password);

        final Usuario user = userWebService.authentication(userEmail, password);
        if (user == null) {
            return null;
        }

        final List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + Usuario.ROLE_NAME.USER));
        if (user.isAdministrador()) {
            grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + Usuario.ROLE_NAME.ADMINISTRATOR));
        }
        if (user.isAutor()) {
            grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + Usuario.ROLE_NAME.AUTHOR));
        }
        if (user.isParceiro()) {
            grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + Usuario.ROLE_NAME.PARTNER));
        }

        final UserDetails principal = new CustomUserDetail(userEmail, password, grantedAuthorityList, user);
        return new UsernamePasswordAuthenticationToken(principal, password, grantedAuthorityList);
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    public Usuario getAuthenticatedUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final CustomUserDetail userDatails = (CustomUserDetail) authentication.getPrincipal();

        return userDatails.getUsuario();

    }
}