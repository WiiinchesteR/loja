package meu.projeto.loja.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import meu.projeto.loja.domain.usuario.UsuarioDTO;
import meu.projeto.loja.domain.usuario.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(@RequestBody @Valid UsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.cadastrarUsuario(dto));
    }
}
