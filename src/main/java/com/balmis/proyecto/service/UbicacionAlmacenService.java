/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Service.java to edit this template
 */
package com.balmis.proyecto.service;

import com.balmis.proyecto.model.UbicacionAlmacen;
import com.balmis.proyecto.repository.UbicacionAlmacenRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UbicacionAlmacenService {
    @Autowired
    public UbicacionAlmacenRepository ubicacionAlmacenRepository;

    @Transactional(readOnly = true)
    public List<UbicacionAlmacen> findAll() {
        return ubicacionAlmacenRepository.findSqlAll();
    }

    @Transactional(readOnly = true)
    public UbicacionAlmacen findById(int id) {
        return ubicacionAlmacenRepository.findSqlById(id);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return ubicacionAlmacenRepository.count();
    }

    @Transactional(readOnly = true)
    public List<UbicacionAlmacen> findByIdGrThan(int id) {
        return ubicacionAlmacenRepository.findSqlByIdGreaterThan(id);
    }

    @Transactional
    public UbicacionAlmacen save(UbicacionAlmacen ubicacionAlmacen) {
        return ubicacionAlmacenRepository.save(ubicacionAlmacen);
    }

    @Transactional
    public UbicacionAlmacen update(int id, UbicacionAlmacen ubicacionUpdate) {
        UbicacionAlmacen ubicacion = ubicacionAlmacenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicacion no encontrada"));

        if (ubicacionUpdate.getReferencia() != null) {
            ubicacion.setReferencia(ubicacionUpdate.getReferencia());
        }
        if (ubicacionUpdate.getEstanteria() != null) {
            ubicacion.setEstanteria(ubicacionUpdate.getEstanteria());
        }
        if (ubicacionUpdate.getNivel() > 0) {
            ubicacion.setNivel(ubicacionUpdate.getNivel());
        }

        return ubicacionAlmacenRepository.save(ubicacion);
    }

    @Transactional
    public void deleteById(int id) {
        if (!ubicacionAlmacenRepository.existsById(id)) {
            throw new RuntimeException("Ubicacion no encontrada");
        }
        ubicacionAlmacenRepository.deleteById(id);
    }
}
