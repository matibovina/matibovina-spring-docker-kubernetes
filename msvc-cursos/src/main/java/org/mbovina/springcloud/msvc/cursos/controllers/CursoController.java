package org.mbovina.springcloud.msvc.cursos.controllers;

import feign.FeignException;
import org.mbovina.springcloud.msvc.cursos.models.Usuario;
import org.mbovina.springcloud.msvc.cursos.models.entity.Curso;
import org.mbovina.springcloud.msvc.cursos.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> listAll() {

        return ResponseEntity.ok(cursoService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detailCurso(@PathVariable Long id) {
        Optional<Curso> o = cursoService.findByIdWithUsers(id); //cursoService.findById(id);
        if (o.isPresent()) {
            return ResponseEntity.ok(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> createCurso(@Valid @RequestBody Curso curso, BindingResult result) {

        if (result.hasErrors()) {
            return validar(result);
        }

        Curso cursoDb = cursoService.saveCurso(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editCurso(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id) {

        if (result.hasErrors()) {
            return validar(result);
        }

        Optional<Curso> o = cursoService.findById(id);
        if (o.isPresent()) {
            Curso cursoDb = o.get();
            cursoDb.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.saveCurso(cursoDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCurso(@PathVariable Long id) {
        Optional<Curso> o = cursoService.findById(id);
        if (o.isPresent()) {
            cursoService.deleteCurso(o.get().getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignUser(@RequestBody Usuario usuario, @PathVariable Long cursoId){

        Optional<Usuario> o = null;

        try {
             o = cursoService.asignUser(usuario, cursoId);
        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                     .body(Collections
                     .singletonMap("mensaje", "No existe el usuario por el id o error en la comunicacion" +
                     e.getMessage()));
        }
        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> creeateUser(@RequestBody Usuario usuario, @PathVariable Long cursoId){

        Optional<Usuario> o = null;

        try {
            o = cursoService.createUser(usuario, cursoId);
        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections
                    .singletonMap("mensaje", "Error al crear usuario o error en la comunicacion" +
                    e.getMessage()));
        }
        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> deleteUser(@RequestBody Usuario usuario, @PathVariable Long cursoId){

        Optional<Usuario> o = null;

        try {
            o = cursoService.deleteUser(usuario, cursoId);
        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections
                    .singletonMap("mensaje", "No se pudo eliminarel usuario o error en la comunicacion" +
                    e.getMessage()));
        }
        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario-curso/{id}")
    public ResponseEntity<?> deleteCursoUsuarioById(@PathVariable Long id){
        cursoService.deleteCursoUsuarioById(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Object> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo" + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
