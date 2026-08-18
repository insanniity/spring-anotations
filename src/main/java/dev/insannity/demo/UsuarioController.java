package dev.insannity.demo;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public Iterable<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @PostMapping
    public Usuario criar(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @PutMapping("/{id}")
    public Usuario atualizar(@PathVariable String id, @RequestBody Usuario usuario) {
        usuario.setId(id);
        return usuarioRepository.save(usuario);
    }

}
