package com.balmis.proyecto.service;

import com.balmis.proyecto.model.Caja;
import com.balmis.proyecto.model.Terminal;
import com.balmis.proyecto.model.dtos.CajaTerminalActualDTO;
import com.balmis.proyecto.model.dtos.TerminalCreateRequestDTO;
import com.balmis.proyecto.model.dtos.TerminalCreateResponseDTO;
import com.balmis.proyecto.model.dtos.TerminalEditResponseDTO;
import com.balmis.proyecto.model.dtos.TerminalMarcaModeloDTO;
import com.balmis.proyecto.model.dtos.TerminalUpdateRequestDTO;
import com.balmis.proyecto.repository.CajaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.balmis.proyecto.repository.TerminalRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TerminalService {

    @Autowired
    public TerminalRepository terminalRepository;

    @Autowired
    public CajaRepository cajaRepository;

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
    public List<TerminalMarcaModeloDTO> findDistinctMarcasModelos() {
        return terminalRepository.findDistinctMarcasModelos();
    }

    // ************************
    // ACTUALIZACIONES
    // ************************  
    @Transactional
    public Terminal save(Terminal terminal) {
        return terminalRepository.save(terminal);
    }

    @Transactional
    public TerminalCreateResponseDTO createTerminal(TerminalCreateRequestDTO request) {
        if (request == null) {
            throw new RuntimeException("Los datos del terminal son obligatorios");
        }

        Terminal terminal = new Terminal();

        terminal.setNumeroSerie(generarNumeroSerie());
        terminal.setMarca(request.getMarca().trim());
        terminal.setModelo(request.getModelo().trim());
        terminal.setEstado(request.getEstado());
        terminal.setNotas(request.getNotas());

        terminal.setFechaIngreso(LocalDateTime.now());
        terminal.setFechaCreacion(LocalDateTime.now());

        terminalRepository.save(terminal);

        return new TerminalCreateResponseDTO(
                "Terminal creado correctamente",
                terminal.getNumeroSerie()
        );
    }

    private String generarNumeroSerie() {
        String ultimoNumeroSerie = terminalRepository.findLastNumeroSerie();

        if (ultimoNumeroSerie == null || ultimoNumeroSerie.trim().isEmpty()) {
            return "SN10001";
        }

        String numeroTexto = ultimoNumeroSerie.substring(2);
        int ultimoNumero = Integer.parseInt(numeroTexto);

        int siguienteNumero = ultimoNumero + 1;

        return "SN" + siguienteNumero;
    }

    @Transactional
    public Terminal updateTerminal(String numeroSerie, TerminalUpdateRequestDTO request) {
        if (numeroSerie == null || numeroSerie.trim().isEmpty()) {
            throw new RuntimeException("El número de serie es obligatorio");
        }

        if (request == null) {
            throw new RuntimeException("Los datos del terminal son obligatorios");
        }

        Terminal terminal = terminalRepository.findByNumeroSerie(numeroSerie.trim())
                .orElseThrow(() -> new RuntimeException("Terminal no encontrado"));

        terminal.setEstado(request.getEstado());
        terminal.setNotas(request.getNotas());

        return terminalRepository.save(terminal);
    }

    @Transactional(readOnly = true)
    public TerminalEditResponseDTO findTerminalForEditByNumeroSerie(String numeroSerie) {
        if (numeroSerie == null || numeroSerie.trim().isEmpty()) {
            throw new RuntimeException("El número de serie es obligatorio");
        }

        Terminal terminal = terminalRepository.findByNumeroSerieWithCaja(numeroSerie.trim())
                .orElse(null);

        if (terminal == null) {
            return null;
        }

        CajaTerminalActualDTO cajaDTO = null;

        if (terminal.getCaja() != null) {
            cajaDTO = new CajaTerminalActualDTO(
                    terminal.getCaja().getId(),
                    terminal.getCaja().getEtiqueta(),
                    terminal.getCaja().getModeloProducto()
            );
        }

        return new TerminalEditResponseDTO(
                terminal.getId(),
                terminal.getNumeroSerie(),
                terminal.getMarca(),
                terminal.getModelo(),
                terminal.getEstado(),
                terminal.getNotas(),
                cajaDTO
        );
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
