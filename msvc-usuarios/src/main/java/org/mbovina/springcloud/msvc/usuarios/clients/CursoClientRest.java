package org.mbovina.springcloud.msvc.usuarios.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-cursos", url = "${msvc.cursos.url}")
public interface CursoClientRest {

    @DeleteMapping("/eliminar-usuario-curso/{id}")
    void deleteCursoUsuarioById(@PathVariable Long id);

}
