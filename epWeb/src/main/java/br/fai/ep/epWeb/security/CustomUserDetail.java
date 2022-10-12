package br.fai.ep.epWeb.security;

import br.fai.ep.epEntities.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetail extends User {

    public CustomUserDetail(final String username, final String password,
                            final Collection<? extends GrantedAuthority> authorities, final Usuario user) {

        super(username, password, true, true, true, true, authorities);
        this.user = user;
    }

    private final Usuario user;

    public Usuario getUsuario() {
        return user;
    }
}