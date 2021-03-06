package br.edu.materdei.tas.server.controller;

import br.edu.materdei.tas.core.entity.GrupoEntity;
import br.edu.materdei.tas.core.exception.ResourceNotFoundException;
import br.edu.materdei.tas.core.service.GrupoService;
import br.edu.materdei.tas.server.utils.CustomErrorResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GrupoController {

    @Autowired
    private GrupoService service;

    @GetMapping("grupos")
    public ResponseEntity<List<GrupoEntity>> findAll() {
        try {

            //busca todos os registros no banco de dados
            List<GrupoEntity> grupos = service.findAll();

            //Retorna a lista de grupos
            return new ResponseEntity(grupos, HttpStatus.OK);
        } catch (Exception e) {
            //qualquer erro 
            return new ResponseEntity(new CustomErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("grupos")
    public ResponseEntity create(@RequestBody GrupoEntity grupo) {
        try {
            //insere o grupo no banco de dados
            this.service.save(grupo);
            
            //retorna o grupo inserido
            return new ResponseEntity(grupo, HttpStatus.CREATED);

        } catch (Exception e) {
            //qualquer erro
            return new ResponseEntity(new CustomErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("grupos/{id}")
    public ResponseEntity findByID(@PathVariable("id") Integer id
    ) {
        try {
            // verifica se existe um grupo com o ID passado por parametro
            GrupoEntity grupo = this.service.findById(id);
            // retorna o grupo com id do parametro
            return new ResponseEntity(grupo, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            //erro de grupo não encontrado
            return new ResponseEntity(
                    new CustomErrorResponse("Não existe um grupo com esse código"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            //qualquer outro erro
            return new ResponseEntity(new CustomErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("grupos/{id}")
    public ResponseEntity update(@PathVariable("id") Integer id,
            @RequestBody GrupoEntity grupo
    ) {
        try {
            //verifica se existe um grupo com id passado por parametro
            GrupoEntity found = this.service.findById(id);

            //força que o novo objeto tenha o mesmo ID do objeto localizado
            grupo.setId(found.getId());

            //salvar o novo objeto no banco
            this.service.save(grupo);

            //retorna o objeto que foi atualizado
            return new ResponseEntity(grupo, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            //erro de grupo não encontrado
            return new ResponseEntity(
                    new CustomErrorResponse("Não existe um grupo com esse código"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            //qualquer outro erro
            return new ResponseEntity(new CustomErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("grupos/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id
    ) {
        try {
            //verifica se existe um grupo com id passado por parametro
            GrupoEntity found = this.service.findById(id);

            //exclui o item localizado
            this.service.delete(id);

            //Como não há o que retornar, retorna-se apenas um estatus sem conteudo
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(
                    // erro não encontrado
                    new CustomErrorResponse("Não existe um grupo com esse código"),
                    HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            //qualquer outro erro
            return new ResponseEntity(new CustomErrorResponse(e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
