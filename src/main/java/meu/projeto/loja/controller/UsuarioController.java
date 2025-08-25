package meu.projeto.loja.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import meu.projeto.loja.domain.usuario.UsuarioDTO;
import meu.projeto.loja.domain.usuario.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(@RequestBody @Valid UsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.cadastrarUsuario(dto));
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<UsuarioDTO>> listarUsuarios(
            @PageableDefault(size = 20, sort = "id") Pageable paginacao
    ) {
        return ResponseEntity.ok(usuarioService.listarUsuarios(paginacao));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioDTO dto
    ) {
        return ResponseEntity.ok(usuarioService.atualizarUsuario(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.ok("Usuário deletado com sucesso!");
    }
}
