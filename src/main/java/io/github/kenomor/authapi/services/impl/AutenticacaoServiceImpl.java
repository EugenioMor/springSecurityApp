package io.github.kenomor.authapi.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.github.kenomor.authapi.dtos.AuthDto;
import io.github.kenomor.authapi.models.Usuario;
import io.github.kenomor.authapi.repositories.IUsuarioRepository;
import io.github.kenomor.authapi.services.IAutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class AutenticacaoServiceImpl implements IAutenticacaoService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return usuarioRepository.findByLogin(login);
    }

    @Override
    public String obterToken(AuthDto authDto) {

      Usuario usuario = usuarioRepository.findByLogin(authDto.login());
        return geraTokenJwt(usuario);
    }

    public String geraTokenJwt(Usuario usuario) {

        try {
            Algorithm algorithm = Algorithm.HMAC256("secret-key");
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(geraDataExpiracao())
                    .sign(algorithm);

        }catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao tentar gerar o token!" + exception.getMessage());
        }
    }

    public String validaTokenJwt(String token) { //Para validar si el token es válido

        try {
            Algorithm algorithm = Algorithm.HMAC256("secret-key");

            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Instant geraDataExpiracao() { //Para generar fecha de expiración del token
        return LocalDateTime.now() //Hora actual
                .plusHours(8) // Le adiciona 8 horas
                .toInstant(ZoneOffset.of("-03:00")); //y convierte de toInstant para Timezone
    }
}
