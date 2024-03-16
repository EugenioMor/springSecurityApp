package io.github.kenomor.authapi.dtos;

import io.github.kenomor.authapi.enums.RoleEnum;

public record UsuarioDto(
        String nome,
        String login,
        String senha,
        RoleEnum role
) {
}
