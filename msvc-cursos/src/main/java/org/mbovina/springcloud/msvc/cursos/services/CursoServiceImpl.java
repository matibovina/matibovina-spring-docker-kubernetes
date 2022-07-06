package org.mbovina.springcloud.msvc.cursos.services;

import org.mbovina.springcloud.msvc.cursos.clients.UsuarioClientRest;
import org.mbovina.springcloud.msvc.cursos.models.Usuario;
import org.mbovina.springcloud.msvc.cursos.models.entity.Curso;
import org.mbovina.springcloud.msvc.cursos.models.entity.CursoUsuario;
import org.mbovina.springcloud.msvc.cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    UsuarioClientRest client;

    @Autowired
    private CursoRepository cursoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listAll() {

        return (List<Curso>) cursoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> findById(Long id) {

        return cursoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> findByIdWithUsers(Long id) {
        Optional<Curso> o = cursoRepository.findById(id);
        if(o.isPresent()){
            Curso curso = o.get();
            if(!curso.getCursoUsuarios().isEmpty()){
                List<Long> ids = curso.getCursoUsuarios().stream()
                        .map(cursoUsuario -> cursoUsuario.getUsuarioId())
                        .collect(Collectors.toList());

                List<Usuario> usuarios = client.alumnosPorCurso(ids);
                curso.setUsuarios(usuarios);
            }
            return Optional.of(curso);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Curso saveCurso(Curso curso) {

        return cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public void deleteCurso(Long id) {

        cursoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Usuario> asignUser(Usuario usuario, Long cursoId) {

        Optional<Curso> o = cursoRepository.findById(cursoId);
        if(o.isPresent()){
            Usuario usuarioMsvc = client.detalle(usuario.getId());
            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
            curso.addCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);

            return Optional.of(usuarioMsvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> createUser(Usuario usuario, Long cursoId) {
        Optional<Curso> o = cursoRepository.findById(cursoId);
        if(o.isPresent()){
            Usuario usuarioMsvcCreated = client.createUser(usuario);
            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvcCreated.getId());
            curso.addCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);

            return Optional.of(usuarioMsvcCreated);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> deleteUser(Usuario usuario, Long cursoId) {
        Optional<Curso> o = cursoRepository.findById(cursoId);
        if(o.isPresent()){
            Usuario usuarioMsvc = client.detalle(usuario.getId());
            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
            curso.removeCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);

            return Optional.of(usuarioMsvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteCursoUsuarioById(Long id) {
        cursoRepository.deleteCursoUsuarioById(id);

    }
}
