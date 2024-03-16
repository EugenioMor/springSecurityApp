package io.github.kenomor.authapi.config;

import io.github.kenomor.authapi.models.Usuario;
import io.github.kenomor.authapi.repositories.IUsuarioRepository;
import io.github.kenomor.authapi.services.IAutenticacaoService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private IAutenticacaoService autenticacaoService;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) //Cada petición va a pasar primero por este filtro
            throws ServletException, IOException {

        String token = extraiTokenHeader(request);

        if (token != null) {
            String login = autenticacaoService.validaTokenJwt(token);
            Usuario usuario = usuarioRepository.findByLogin(login);

            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);

    }

    public String extraiTokenHeader(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");

        if (authHeader == null) {
            return null;
        }

        //Bearer tokenxxxxxxxx. Se extrae el "Bearer" para quedarse solo con el token. Posicion 0 tiene el Bearer, en la 1 el token

        if (!authHeader.split(" ")[0].equals("Bearer")) {
            return null;
        }

        return authHeader.split(" ")[1];
    }
}
