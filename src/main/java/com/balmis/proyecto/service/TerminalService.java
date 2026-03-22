
package com.balmis.proyecto.service;

import com.balmis.proyecto.model.Terminal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.balmis.proyecto.repository.TerminalRepository;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class TerminalService {
    
    @Autowired
    public TerminalRepository terminalRepository;
    
    // ************************
    // CONSULTAS
    // ************************  
    @Transactional(readOnly = true) 
    public List<Terminal> findAll() {
        return terminalRepository.findSqlAll();
    }
    
    @Transactional(readOnly = true) 
    public Terminal findById(int terminalId) {
        return terminalRepository.findSqlById(terminalId);
    }

    @Transactional(readOnly = true)
    public Terminal findByNumeroSerie(String numeroSerie) {
        return terminalRepository.findSqlByNumeroSerie(numeroSerie);
    }

    @Transactional(readOnly = true) 
    public Long count() {
        return terminalRepository.count();
    }    
    
    @Transactional(readOnly = true) 
    public List<Terminal> findByIdGrThan(int terminalId) {
        return terminalRepository.findSqlByIdGreaterThan(terminalId);
    }
    
    // ************************
    // ACTUALIZACIONES
    // ************************  

    @Transactional
    public Terminal save(Terminal terminal) {
        return terminalRepository.save(terminal);
    }
    
    @Transactional
    public Terminal update(int id, Terminal terminalUpdate) {
        Terminal terminal = terminalRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Terminal no encontrado"));
        
        if (terminalUpdate.getNumeroSerie() != null) {
            terminal.setNumeroSerie(terminalUpdate.getNumeroSerie());
        }
        if (terminalUpdate.getModelo() != null) {
            terminal.setModelo(terminalUpdate.getModelo());
        }
        if (terminalUpdate.getMarca() != null) {
            terminal.setMarca(terminalUpdate.getMarca());
        }
        if (terminalUpdate.getEstado() != null) {
            terminal.setEstado(terminalUpdate.getEstado());
        }
        if (terminalUpdate.getNotas() != null) {
            terminal.setNotas(terminalUpdate.getNotas());
        }
         if (terminalUpdate.getFechaIngreso() != null) {
            terminal.setFechaIngreso(terminalUpdate.getFechaIngreso());
        }
        if (terminalUpdate.getFechaCreacion() != null) {
            terminal.setFechaCreacion(terminalUpdate.getFechaCreacion());
        }
      
        return terminalRepository.save(terminal);
    }

    @Transactional
    public Terminal updateByNumeroSerie(String numeroSerie, Terminal terminalUpdate) {
        Terminal terminal = terminalRepository.findSqlByNumeroSerie(numeroSerie);
        if (terminal == null) {
            throw new RuntimeException("Terminal no encontrado");
        }

        if (terminalUpdate.getNumeroSerie() != null) {
            terminal.setNumeroSerie(terminalUpdate.getNumeroSerie());
        }
        if (terminalUpdate.getModelo() != null) {
            terminal.setModelo(terminalUpdate.getModelo());
        }
        if (terminalUpdate.getMarca() != null) {
            terminal.setMarca(terminalUpdate.getMarca());
        }
        if (terminalUpdate.getEstado() != null) {
            terminal.setEstado(terminalUpdate.getEstado());
        }
        if (terminalUpdate.getNotas() != null) {
            terminal.setNotas(terminalUpdate.getNotas());
        }
        if (terminalUpdate.getFechaIngreso() != null) {
            terminal.setFechaIngreso(terminalUpdate.getFechaIngreso());
        }
        if (terminalUpdate.getFechaCreacion() != null) {
            terminal.setFechaCreacion(terminalUpdate.getFechaCreacion());
        }

        return terminalRepository.save(terminal);
    }

    @Transactional
    public Terminal patch(int id, Terminal terminalPatch) {
        Terminal terminal = terminalRepository.findSqlById(id);
        if (terminal == null) {
            throw new RuntimeException("Terminal no encontrado");
        }

        if (terminalPatch.getNumeroSerie() != null) {
            terminal.setNumeroSerie(terminalPatch.getNumeroSerie());
        }
        if (terminalPatch.getModelo() != null) {
            terminal.setModelo(terminalPatch.getModelo());
        }
        if (terminalPatch.getMarca() != null) {
            terminal.setMarca(terminalPatch.getMarca());
        }
        if (terminalPatch.getEstado() != null) {
            terminal.setEstado(terminalPatch.getEstado());
        }
        if (terminalPatch.getNotas() != null) {
            terminal.setNotas(terminalPatch.getNotas());
        }
        if (terminalPatch.getFechaIngreso() != null) {
            terminal.setFechaIngreso(terminalPatch.getFechaIngreso());
        }
        if (terminalPatch.getFechaCreacion() != null) {
            terminal.setFechaCreacion(terminalPatch.getFechaCreacion());
        }

        return terminalRepository.save(terminal);
    }

    @Transactional
    public Terminal patchByNumeroSerie(String numeroSerie, Terminal terminalPatch) {
        Terminal terminal = terminalRepository.findSqlByNumeroSerie(numeroSerie);
        if (terminal == null) {
            throw new RuntimeException("Terminal no encontrado");
        }

        if (terminalPatch.getNumeroSerie() != null) {
            terminal.setNumeroSerie(terminalPatch.getNumeroSerie());
        }
        if (terminalPatch.getModelo() != null) {
            terminal.setModelo(terminalPatch.getModelo());
        }
        if (terminalPatch.getMarca() != null) {
            terminal.setMarca(terminalPatch.getMarca());
        }
        if (terminalPatch.getEstado() != null) {
            terminal.setEstado(terminalPatch.getEstado());
        }
        if (terminalPatch.getNotas() != null) {
            terminal.setNotas(terminalPatch.getNotas());
        }
        if (terminalPatch.getFechaIngreso() != null) {
            terminal.setFechaIngreso(terminalPatch.getFechaIngreso());
        }
        if (terminalPatch.getFechaCreacion() != null) {
            terminal.setFechaCreacion(terminalPatch.getFechaCreacion());
        }

        return terminalRepository.save(terminal);
    }
    
    @Transactional
    public void deleteById(int id) {
        if (!terminalRepository.existsById(id)) {
            throw new RuntimeException("Terminal no encontrado");
        }
        terminalRepository.deleteById(id);
    }

    @Transactional
    public void deleteByNumeroSerie(String numeroSerie) {
        Terminal terminal = terminalRepository.findSqlByNumeroSerie(numeroSerie);
        if (terminal == null) {
            throw new RuntimeException("Terminal no encontrado");
        }
        terminalRepository.delete(terminal);
    }
}
