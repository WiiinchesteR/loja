package meu.projeto.loja.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import meu.projeto.loja.domain.usuario.dto.UsuarioRequestDTO;
import meu.projeto.loja.domain.usuario.dto.UsuarioResponseDTO;
import meu.projeto.loja.domain.usuario.dto.UsuarioUpdateDTO;
import meu.projeto.loja.domain.usuario.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@RequestBody @Valid UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.cadastrarUsuario(dto));
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<UsuarioResponseDTO>> listarUsuarios(
            @PageableDefault(size = 20, sort = "id") Pageable paginacao
    ) {
        return ResponseEntity.ok(usuarioService.listarUsuarios(paginacao));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioUpdateDTO dto
    ) {
        return ResponseEntity.ok(usuarioService.atualizarUsuario(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
