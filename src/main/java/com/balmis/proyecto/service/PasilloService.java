package com.balmis.proyecto.service;

import com.balmis.proyecto.model.Pasillo;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.balmis.proyecto.repository.PasilloRepository;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class PasilloService {
    
    @Autowired
    public PasilloRepository pasilloRepository;
    
    // ************************
    // CONSULTAS
    // ************************  
    @Transactional(readOnly = true) 
    public List<Pasillo> findAll() {
        return pasilloRepository.findSqlAll();
    }
    
    @Transactional(readOnly = true) 
    public Pasillo findById(int pasilloId) {
        return pasilloRepository.findSqlById(pasilloId);
    }

    @Transactional(readOnly = true) 
    public Long count() {
        return pasilloRepository.count();
    }    
    
    @Transactional(readOnly = true) 
    public List<Pasillo> findByIdGrThan(int pasilloId) {
        return pasilloRepository.findSqlByIdGreaterThan(pasilloId);
    }
    
    // ************************
    // ACTUALIZACIONES
    // ************************  

    @Transactional
    public Pasillo save(Pasillo pasillo) {
        return pasilloRepository.save(pasillo);
    }
    
    @Transactional
    public Pasillo update(int id, Pasillo pasilloUpdate) {
        Pasillo pasillo = pasilloRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("pasillo no encontrado"));
        
        if (pasilloUpdate.getNumeroPasillo() != null) {
            pasillo.setNumeroPasillo(pasilloUpdate.getNumeroPasillo());
        }
        
        return pasilloRepository.save(pasillo);
    }
    
    @Transactional
    public void deleteById(int id) {
        if (!pasilloRepository.existsById(id)) {
            throw new RuntimeException("Pasillo no encontrado");
        }
        pasilloRepository.deleteById(id);
    }        
}
