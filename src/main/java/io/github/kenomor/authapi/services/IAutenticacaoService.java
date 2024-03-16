package io.github.kenomor.authapi.services;

import io.github.kenomor.authapi.dtos.AuthDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IAutenticacaoService extends UserDetailsService {

    public String obterToken(AuthDto authDto);

    public String validaTokenJwt(String token);
}
