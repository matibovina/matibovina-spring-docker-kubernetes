package org.mbovina.springcloud.msvc.cursos.services;

import org.mbovina.springcloud.msvc.cursos.models.Usuario;
import org.mbovina.springcloud.msvc.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    List<Curso> listAll();

    Optional<Curso> findById(Long id);

    Optional<Curso> findByIdWithUsers(Long id);

    Curso saveCurso(Curso curso);

    void deleteCurso(Long id);

    Optional<Usuario> asignUser(Usuario usuario, Long cursoId);
    Optional<Usuario> createUser(Usuario usuario, Long cursoId);
    Optional<Usuario> deleteUser(Usuario usuario, Long cursoId);

    void deleteCursoUsuarioById(Long id);
}
