package org.mbovina.springcloud.msvc.usuarios.services;

import org.mbovina.springcloud.msvc.usuarios.clients.CursoClientRest;
import org.mbovina.springcloud.msvc.usuarios.models.entity.Usuario;
import org.mbovina.springcloud.msvc.usuarios.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private CursoClientRest client;

    @Autowired
    private UsuarioRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listAll() {

        return (List<Usuario>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {

        return repository.findById(id);
    }

    @Override
    @Transactional
    public Usuario saveUser(Usuario usuario) {

        return repository.save(usuario);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {

        repository.deleteById(id);
        client.deleteCursoUsuarioById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> byEmail(String email) {

        return repository.searchByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarPorIds(Iterable<Long> ids) {

        return (List<Usuario>) repository.findAllById(ids);
    }


}
