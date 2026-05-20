
package com.balmis.proyecto.service;

import com.balmis.proyecto.model.Caja;
import com.balmis.proyecto.model.dtos.CajaExpedicionDetailDTO;
import com.balmis.proyecto.model.dtos.TerminalCajaDTO;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.balmis.proyecto.repository.CajaRepository;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class CajaService {
    
    @Autowired
    public CajaRepository cajaRepository;
    
    // ************************
    // CONSULTAS
    // ************************  
    @Transactional(readOnly = true) 
    public List<Caja> findAll() {
        return cajaRepository.findSqlAll();
    }
    
    @Transactional(readOnly = true) 
    public Caja findById(int cajaId) {
        return cajaRepository.findSqlById(cajaId);
    }

    @Transactional(readOnly = true) 
    public Long count() {
        return cajaRepository.count();
    }    
    
    @Transactional(readOnly = true) 
    public List<Caja> findByIdGrThan(int cajaId) {
        return cajaRepository.findSqlByIdGreaterThan(cajaId);
    }
    
    @Transactional(readOnly = true)
    public CajaExpedicionDetailDTO findCajaExpedicionDetailByEtiqueta(String etiqueta) {
    Caja caja = cajaRepository.findByEtiquetaWithTerminales(etiqueta);

    if (caja == null) {
        return null;
    }

    List<TerminalCajaDTO> terminales = caja.getTerminales()
            .stream()
            .map(t -> new TerminalCajaDTO(
                    t.getModelo(),
                    t.getMarca(),
                    t.getEstado(),
                    t.getNumeroSerie()
            ))
            .toList();

    return new CajaExpedicionDetailDTO(
            caja.getId(),
            caja.getEtiqueta(),
            caja.getModeloProducto(),
            (long) terminales.size(),
            terminales
    );
}
    
    // ************************
    // ACTUALIZACIONES
    // ************************  

    @Transactional
    public Caja save(Caja caja) {
        return cajaRepository.save(caja);
    }
    
    @Transactional
    public Caja update(int id, Caja cajaUpdate) {
        Caja caja = cajaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Caja no encontrado"));
        
        if (cajaUpdate.getEtiqueta() != null) {
            caja.setEtiqueta(cajaUpdate.getEtiqueta());
        }
        if (cajaUpdate.getModeloProducto() != null) {
            caja.setModeloProducto(cajaUpdate.getModeloProducto());
        }
        
        if (cajaUpdate.getPalet() != null) {
            caja.setPalet(cajaUpdate.getPalet());
        }
        
        return cajaRepository.save(caja);
    }
    
    @Transactional
    public void deleteById(int id) {
        if (!cajaRepository.existsById(id)) {
            throw new RuntimeException("Caja no encontrado");
        }
        cajaRepository.deleteById(id);
    }        
}
