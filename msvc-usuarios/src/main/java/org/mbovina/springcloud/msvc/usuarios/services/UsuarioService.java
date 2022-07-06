package org.mbovina.springcloud.msvc.usuarios.services;

import org.mbovina.springcloud.msvc.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;


public interface UsuarioService {

    List <Usuario> listAll();
    Optional<Usuario> findById(Long id);
    Usuario saveUser(Usuario usuario);
    void deleteUser(Long id);
    Optional<Usuario> byEmail(String email);

    List<Usuario> listarPorIds(Iterable<Long> ids);
}
