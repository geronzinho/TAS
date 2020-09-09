package br.edu.materdei.tas.core.service;

import br.edu.materdei.tas.core.entity.GrupoEntity;
import br.edu.materdei.tas.core.exception.ResourceNotFoundException;
import br.edu.materdei.tas.core.repository.GrupoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GrupoService implements IBaseService<GrupoEntity>{

    @Autowired
    private GrupoRepository repository;
    
    @Override
    @Transactional
    public List<GrupoEntity> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public GrupoEntity findById(Integer id) throws ResourceNotFoundException {
        return repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id));
    }

    @Override
    @Transactional
    public GrupoEntity save(GrupoEntity entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public void delete(Integer id) throws ResourceNotFoundException {
        repository.deleteById(id);
    }
    
}
