package com.balmis.proyecto.service;

import com.balmis.proyecto.model.Palet;
import com.balmis.proyecto.repository.PaletRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaletService {

    @Autowired
    public PaletRepository paletRepository;

    // ************************
    // CONSULTAS
    // ************************
    @Transactional(readOnly = true)
    public List<Palet> findAll() {
        return paletRepository.findSqlAll();
    }

    @Transactional(readOnly = true)
    public Palet findById(int paletId) {
        return paletRepository.findSqlById(paletId);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return paletRepository.count();
    }

    @Transactional(readOnly = true)
    public List<Palet> findByIdGrThan(int paletId) {
        return paletRepository.findSqlByIdGreaterThan(paletId);
    }

    // ************************
    // ACTUALIZACIONES
    // ************************
    @Transactional
    public Palet save(Palet palet) {
        return paletRepository.save(palet);
    }

    @Transactional
    public Palet update(int id, Palet paletUpdate) {
        Palet palet = paletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Palet no encontrado"));

        if (paletUpdate.getDescripcion() != null) {
            palet.setDescripcion(paletUpdate.getDescripcion());
        }
        if (paletUpdate.getMaterial() != null) {
            palet.setMaterial(paletUpdate.getMaterial());
        }
        if (paletUpdate.getTipo() != null) {
            palet.setTipo(paletUpdate.getTipo());
        }
        if (paletUpdate.getCapacidadMaxCajas() >= 0) {
            palet.setCapacidadMaxCajas(paletUpdate.getCapacidadMaxCajas());
        }
        if (paletUpdate.getCodigoMarca() != null) {
            palet.setCodigoMarca(paletUpdate.getCodigoMarca());
        }

        palet.setUbicacionAlmacen(paletUpdate.getUbicacionAlmacen());

        return paletRepository.save(palet);
    }

    @Transactional
    public void deleteById(int id) {
        if (!paletRepository.existsById(id)) {
            throw new RuntimeException("Palet no encontrado");
        }
        paletRepository.deleteById(id);
    }
}
