package br.edu.materdei.tas.server.controller;

import br.edu.materdei.tas.compra.entity.FornecedorEntity;
import br.edu.materdei.tas.compra.service.FornecedorService;
import br.edu.materdei.tas.core.exception.ResourceNotFoundException;
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
public class FornecedorController {

    @Autowired
    private FornecedorService service;

    @GetMapping("forncedores")
    public ResponseEntity<List<FornecedorEntity>> findAll() {
        try {

            //busca todos os registros no banco de dados
            List<FornecedorEntity> forncedores = service.findAll();

            //Retorna a lista de forncedores
            return new ResponseEntity(forncedores, HttpStatus.OK);
        } catch (Exception e) {
            //qualquer erro 
            return new ResponseEntity(new CustomErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("forncedores")
    public ResponseEntity create(@RequestBody FornecedorEntity forncedor) {
        try {
            //insere o forncedor no banco de dados
            this.service.save(forncedor);
            
            //retorna o forncedor inserido
            return new ResponseEntity(forncedor, HttpStatus.CREATED);

        } catch (Exception e) {
            //qualquer erro
            return new ResponseEntity(new CustomErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("forncedores/{id}")
    public ResponseEntity findByID(@PathVariable("id") Integer id
    ) {
        try {
            // verifica se existe um forncedor com o ID passado por parametro
            FornecedorEntity forncedor = this.service.findById(id);
            // retorna o forncedor com id do parametro
            return new ResponseEntity(forncedor, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            //erro de forncedor não encontrado
            return new ResponseEntity(
                    new CustomErrorResponse("Não existe um forncedor com esse código"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            //qualquer outro erro
            return new ResponseEntity(new CustomErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("forncedores/{id}")
    public ResponseEntity update(@PathVariable("id") Integer id,
            @RequestBody FornecedorEntity forncedor
    ) {
        try {
            //verifica se existe um forncedor com id passado por parametro
            FornecedorEntity found = this.service.findById(id);

            //força que o novo objeto tenha o mesmo ID do objeto localizado
            forncedor.setId(found.getId());

            //salvar o novo objeto no banco
            this.service.save(forncedor);

            //retorna o objeto que foi atualizado
            return new ResponseEntity(forncedor, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            //erro de forncedor não encontrado
            return new ResponseEntity(
                    new CustomErrorResponse("Não existe um forncedor com esse código"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            //qualquer outro erro
            return new ResponseEntity(new CustomErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("forncedores/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id
    ) {
        try {
            //verifica se existe um forncedor com id passado por parametro
            FornecedorEntity found = this.service.findById(id);

            //exclui o item localizado
            this.service.delete(id);

            //Como não há o que retornar, retorna-se apenas um estatus sem conteudo
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(
                    // erro não encontrado
                    new CustomErrorResponse("Não existe um forncedor com esse código"),
                    HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            //qualquer outro erro
            return new ResponseEntity(new CustomErrorResponse(e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
