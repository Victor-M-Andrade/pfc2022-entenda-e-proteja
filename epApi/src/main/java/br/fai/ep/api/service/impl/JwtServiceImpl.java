package br.fai.ep.api.service.impl;

import br.fai.ep.api.helper.JwtHelper;
import br.fai.ep.api.service.JwtService;
import br.fai.ep.epEntities.Usuario;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {

    @Override
    public String getJWTToken(final Usuario user) {
        try {
            final List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
            if (user.isAdministrador()) {
                grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + Usuario.ROLE_NAME.ADMINISTRATOR));
            }
            if (user.isAutor()) {
                grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + Usuario.ROLE_NAME.AUTHOR));
            }
            if (user.isParceiro()) {
                grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + Usuario.ROLE_NAME.PARTNER));
            }

            final String token = Jwts.builder()
                    .setId(JwtHelper.ID_NAME_TOKE)
                    .setSubject(user.getNome())
                    .claim(JwtHelper.CLAIMS_AUTHORITIES_KEY, grantedAuthorityList.stream().map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                    .signWith(JwtHelper.KEY_GENERATE_TOKEN).compact();

            return token;
        } catch (final Exception e) {
            System.err.println(e.getMessage());
            return JwtHelper.INVALID_TOKEN;
        }
    }

}