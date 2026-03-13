/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Service.java to edit this template
 */
package com.balmis.proyecto.service;

import com.balmis.proyecto.model.Estanteria;
import com.balmis.proyecto.repository.EstanteriaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class EstanteriaService {
    @Autowired
    public EstanteriaRepository estanteriaRepository;

    @Transactional(readOnly = true)
    public List<Estanteria> findAll() {
        return estanteriaRepository.findSqlAll();
    }

    @Transactional(readOnly = true)
    public Estanteria findById(int id) {
        return estanteriaRepository.findSqlById(id);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return estanteriaRepository.count();
    }

    @Transactional(readOnly = true)
    public List<Estanteria> findByIdGrThan(int id) {
        return estanteriaRepository.findSqlByIdGreaterThan(id);
    }

    @Transactional
    public Estanteria save(Estanteria estanteria) {
        return estanteriaRepository.save(estanteria);
    }

    @Transactional
    public Estanteria update(int id, Estanteria estanteriaUpdate) {
        Estanteria estanteria = estanteriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estanteria no encontrada"));

        if (estanteriaUpdate.getCodigo() != null) {
            estanteria.setCodigo(estanteriaUpdate.getCodigo());
        }
        if (estanteriaUpdate.getNivelesMaximos() > 0) {
            estanteria.setNivelesMaximos(estanteriaUpdate.getNivelesMaximos());
        }
        if (estanteriaUpdate.getCapacidadNivel() != null && estanteriaUpdate.getCapacidadNivel() > 0) {
            estanteria.setCapacidadNivel(estanteriaUpdate.getCapacidadNivel());
        }
        if (estanteriaUpdate.getPasillo() != null) {
            estanteria.setPasillo(estanteriaUpdate.getPasillo());
        }

        return estanteriaRepository.save(estanteria);
    }

    @Transactional
    public void deleteById(int id) {
        if (!estanteriaRepository.existsById(id)) {
            throw new RuntimeException("Estanteria no encontrada");
        }
        estanteriaRepository.deleteById(id);
    }
}
