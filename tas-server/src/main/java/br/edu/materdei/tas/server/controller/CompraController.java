package br.edu.materdei.tas.server.controller;

import br.edu.materdei.tas.compra.entity.CompraEntity;
import br.edu.materdei.tas.compra.service.CompraService;
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
public class CompraController {

    @Autowired
    private CompraService service;

    @GetMapping("compras")
    public ResponseEntity<List<CompraEntity>> findAll() {
        try {

            //busca todos os registros no banco de dados
            List<CompraEntity> compras = service.findAll();

            //Retorna a lista de compras
            return new ResponseEntity(compras, HttpStatus.OK);
        } catch (Exception e) {
            //qualquer erro 
            return new ResponseEntity(new CustomErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("compras")
    public ResponseEntity create(@RequestBody CompraEntity compra) {
        try {
            //insere o compra no banco de dados
            this.service.save(compra);
            
            //retorna o compra inserido
            return new ResponseEntity(compra, HttpStatus.CREATED);

        } catch (Exception e) {
            //qualquer erro
            return new ResponseEntity(new CustomErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("compras/{id}")
    public ResponseEntity findByID(@PathVariable("id") Integer id
    ) {
        try {
            // verifica se existe um compra com o ID passado por parametro
            CompraEntity compra = this.service.findById(id);
            // retorna o compra com id do parametro
            return new ResponseEntity(compra, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            //erro de compra não encontrado
            return new ResponseEntity(
                    new CustomErrorResponse("Não existe um compra com esse código"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            //qualquer outro erro
            return new ResponseEntity(new CustomErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("compras/{id}")
    public ResponseEntity update(@PathVariable("id") Integer id,
            @RequestBody CompraEntity compra
    ) {
        try {
            //verifica se existe um compra com id passado por parametro
            CompraEntity found = this.service.findById(id);

            //força que o novo objeto tenha o mesmo ID do objeto localizado
            compra.setId(found.getId());

            //salvar o novo objeto no banco
            this.service.save(compra);

            //retorna o objeto que foi atualizado
            return new ResponseEntity(compra, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            //erro de compra não encontrado
            return new ResponseEntity(
                    new CustomErrorResponse("Não existe um compra com esse código"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            //qualquer outro erro
            return new ResponseEntity(new CustomErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("compras/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id
    ) {
        try {
            //verifica se existe um compra com id passado por parametro
            CompraEntity found = this.service.findById(id);

            //exclui o item localizado
            this.service.delete(id);

            //Como não há o que retornar, retorna-se apenas um estatus sem conteudo
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(
                    // erro não encontrado
                    new CustomErrorResponse("Não existe um compra com esse código"),
                    HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            //qualquer outro erro
            return new ResponseEntity(new CustomErrorResponse(e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
