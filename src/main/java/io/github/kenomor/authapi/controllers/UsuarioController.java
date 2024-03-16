package io.github.kenomor.authapi.controllers;

import io.github.kenomor.authapi.dtos.UsuarioDto;
import io.github.kenomor.authapi.services.IUsuarioService;
import io.github.kenomor.authapi.services.impl.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @PostMapping
    private UsuarioDto salvar(@RequestBody UsuarioDto usuarioDto) {
        return usuarioService.salvar(usuarioDto);
    }

    @GetMapping("/admin")
    private String getAdmin(){
        return "Permisao de administrador";
    }

    @GetMapping("/user")
    private String getUser(){
        return "Permisao de usuario";
    }
}
