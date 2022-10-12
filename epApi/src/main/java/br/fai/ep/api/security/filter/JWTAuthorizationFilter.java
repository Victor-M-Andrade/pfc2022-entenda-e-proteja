package br.fai.ep.api.security.filter;

import br.fai.ep.api.helper.JwtHelper;
import br.fai.ep.epEntities.security.HeaderPattern;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        try {
            if (!checkJwtToken(request)) {
                SecurityContextHolder.clearContext();
                return;
            }
            final Jws<Claims> claims = validateToken(request);
            if (claims == null || claims.getBody().get(JwtHelper.CLAIMS_AUTHORITIES_KEY) == null) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Sem permissão para acessar esse recurso -> validate");
                return;
            }

            setupAuthentication(claims.getBody());
        } catch (final Exception e) {
            System.out.println(e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Sem permissão para acessar esse recurso -> Exception");
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    private boolean checkJwtToken(final HttpServletRequest request) {
        final String authenticationHeader = request.getHeader(HeaderPattern.HEADER_AUTHORIZATION);
        if (authenticationHeader == null || !authenticationHeader.startsWith(HeaderPattern.HEADER_BEARER_PREFIX)) {
            return false;
        }

        return true;
    }

    private Jws<Claims> validateToken(final HttpServletRequest request) {
        final String JwtToken = request.getHeader(HeaderPattern.HEADER_AUTHORIZATION).replace(HeaderPattern.HEADER_BEARER_PREFIX, "");
        return Jwts.parserBuilder().setSigningKey(JwtHelper.KEY_GENERATE_TOKEN).build().parseClaimsJws(JwtToken);
    }

    private void setupAuthentication(final Claims claims) {
        final List<String> authorities = (List<String>) claims.get(JwtHelper.CLAIMS_AUTHORITIES_KEY);
        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(),
                null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}