package org.mbovina.springcloud.msvc.usuarios.controllers;


import org.mbovina.springcloud.msvc.usuarios.models.entity.Usuario;
import org.mbovina.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
//@RequestMapping("api")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public Map<String, List<Usuario>> listAllUsers() {

        return  Collections.singletonMap("usuarios", usuarioService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findUser(@PathVariable Long id) {

        Optional<Usuario> usuarioOptional = usuarioService.findById(id);
        if (usuarioOptional.isPresent()) {
            return ResponseEntity.ok().body(usuarioOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        if (!usuario.getEmail().isEmpty() && usuarioService.byEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("mensaje", "Ya existe un usuario con ese email."));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.saveUser(usuario));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editUser(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validar(result);
        }
        Optional<Usuario> o = usuarioService.findById(id);
        if (o.isPresent()) {
            Usuario usuarioDb = o.get();

            if (!usuario.getEmail().isEmpty() && !usuario.getEmail().equalsIgnoreCase(usuario.getEmail()) && usuarioService.byEmail(usuario.getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("mensaje", "Ya existe un usuario con ese email."));
            }
            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.saveUser(usuarioDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<Usuario> o = usuarioService.findById(id);
        if (o.isPresent()) {
            usuarioService.deleteUser(id);
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("usuarios-por-curso")
    public ResponseEntity<?> alumnosPorCurso(@RequestParam List<Long> ids){
        return ResponseEntity.ok(usuarioService.listarPorIds(ids));
    }

    private ResponseEntity<Object> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo" + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }

}
