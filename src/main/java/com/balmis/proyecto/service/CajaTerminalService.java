package com.balmis.proyecto.service;

import com.balmis.proyecto.model.Caja;
import com.balmis.proyecto.model.EstadoTerminal;
import com.balmis.proyecto.model.Terminal;
import com.balmis.proyecto.model.dtos.AsociarTerminalesResponse;
import com.balmis.proyecto.model.dtos.CajaValidacionDto;
import com.balmis.proyecto.model.dtos.ErrorTerminalDto;
import com.balmis.proyecto.model.dtos.MotivoValidacionTerminal;
import com.balmis.proyecto.model.dtos.TerminalAsociadoDto;
import com.balmis.proyecto.model.dtos.TerminalValidacionDto;
import com.balmis.proyecto.model.dtos.ValidarTerminalResponse;
import com.balmis.proyecto.repository.CajaRepository;
import com.balmis.proyecto.repository.TerminalRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CajaTerminalService {

    @Autowired
    private CajaRepository cajaRepository;

    @Autowired
    private TerminalRepository terminalRepository;

    @Transactional(readOnly = true)
    public ValidarTerminalResponse validarTerminalParaCaja(Integer cajaId, String sn) {
        if (sn == null || sn.trim().isEmpty()) {
            return new ValidarTerminalResponse(false, MotivoValidacionTerminal.SN_VACIO, null, buildCajaDto(cajaId));
        }

        Caja caja = cajaRepository.findSqlById(cajaId);
        if (caja == null) {
            return new ValidarTerminalResponse(false, MotivoValidacionTerminal.CAJA_NO_EXISTE, null, buildCajaDto(cajaId));
        }

        Terminal terminal = terminalRepository.findSqlByNumeroSerie(sn.trim());
        if (terminal == null) {
            return new ValidarTerminalResponse(false, MotivoValidacionTerminal.TERMINAL_NO_EXISTE, null, buildCajaDto(caja));
        }

        if (terminal.getCaja() != null && !terminal.getCaja().getId().equals(caja.getId())) {
            return new ValidarTerminalResponse(false, MotivoValidacionTerminal.TERMINAL_YA_ASOCIADO, buildTerminalDto(terminal), buildCajaDto(caja));
        }

        if (!esModeloCompatible(terminal, caja)) {
            return new ValidarTerminalResponse(false, MotivoValidacionTerminal.MODELO_NO_COMPATIBLE, buildTerminalDto(terminal), buildCajaDto(caja));
        }

        if (!esEstadoValidoParaAsociar(terminal)) {
            return new ValidarTerminalResponse(false, MotivoValidacionTerminal.ESTADO_NO_VALIDO, buildTerminalDto(terminal), buildCajaDto(caja));
        }

        return new ValidarTerminalResponse(true, null, buildTerminalDto(terminal), buildCajaDto(caja));
    }

    @Transactional
    public AsociarTerminalesResponse asociarTerminalesACaja(Integer cajaId, List<String> sns) {
        Caja caja = cajaRepository.findSqlById(cajaId);
        if (caja == null) {
            return new AsociarTerminalesResponse(false, MotivoValidacionTerminal.CAJA_NO_EXISTE, cajaId, null, List.of(new ErrorTerminalDto(null, MotivoValidacionTerminal.CAJA_NO_EXISTE)));
        }

        if (sns == null || sns.isEmpty()) {
            return new AsociarTerminalesResponse(false, MotivoValidacionTerminal.LISTA_SNS_VACIA, cajaId, null, List.of(new ErrorTerminalDto(null, MotivoValidacionTerminal.LISTA_SNS_VACIA)));
        }

        List<String> snsLimpios = sns.stream()
                .map(v -> v == null ? "" : v.trim())
                .filter(v -> !v.isEmpty())
                .toList();

        if (snsLimpios.isEmpty()) {
            return new AsociarTerminalesResponse(false, MotivoValidacionTerminal.LISTA_SNS_VACIA, cajaId, null, List.of(new ErrorTerminalDto(null, MotivoValidacionTerminal.LISTA_SNS_VACIA)));
        }

        Set<String> seen = new HashSet<>();
        for (String sn : snsLimpios) {
            String normalized = normalizarTexto(sn);
            if (!seen.add(normalized)) {
                return new AsociarTerminalesResponse(false, MotivoValidacionTerminal.SN_DUPLICADO, cajaId, null, List.of(new ErrorTerminalDto(sn, MotivoValidacionTerminal.SN_DUPLICADO)));
            }
        }

        List<Terminal> terminales = terminalRepository.findByNumeroSerieIn(snsLimpios);
        Map<String, Terminal> bySn = terminales.stream()
                .collect(Collectors.toMap(t -> normalizarTexto(t.getNumeroSerie()), Function.identity()));

        List<ErrorTerminalDto> errores = new ArrayList<>();

        for (String sn : snsLimpios) {
            Terminal terminal = bySn.get(normalizarTexto(sn));
            if (terminal == null) {
                errores.add(new ErrorTerminalDto(sn, MotivoValidacionTerminal.TERMINAL_NO_EXISTE));
                continue;
            }

            if (terminal.getCaja() != null && !terminal.getCaja().getId().equals(caja.getId())) {
                errores.add(new ErrorTerminalDto(terminal.getNumeroSerie(), MotivoValidacionTerminal.TERMINAL_YA_ASOCIADO));
                continue;
            }

            if (!esModeloCompatible(terminal, caja)) {
                errores.add(new ErrorTerminalDto(terminal.getNumeroSerie(), MotivoValidacionTerminal.MODELO_NO_COMPATIBLE));
                continue;
            }

            if (!esEstadoValidoParaAsociar(terminal)) {
                errores.add(new ErrorTerminalDto(terminal.getNumeroSerie(), MotivoValidacionTerminal.ESTADO_NO_VALIDO));
            }
        }

        if (!errores.isEmpty()) {
            return new AsociarTerminalesResponse(false, MotivoValidacionTerminal.ALGUNOS_TERMINALES_INVALIDOS, cajaId, null, errores);
        }

        for (Terminal terminal : terminales) {
            terminal.setCaja(caja);
        }
        terminalRepository.saveAll(terminales);

        List<TerminalAsociadoDto> asociados = terminales.stream()
                .map(t -> new TerminalAsociadoDto(t.getNumeroSerie(), "ASOCIADO"))
                .toList();

        return new AsociarTerminalesResponse(true, null, cajaId, asociados, List.of());
    }

    @Transactional
    public Map<String, Object> desasignarTerminalDeCaja(Integer cajaId, String sn) {
        if (sn == null || sn.trim().isEmpty()) {
            return Map.of(
                    "success", false,
                    "motivo", MotivoValidacionTerminal.SN_VACIO,
                    "error", "El parámetro 'sn' es obligatorio"
            );
        }

        Caja caja = cajaRepository.findSqlById(cajaId);
        if (caja == null) {
            return Map.of(
                    "success", false,
                    "motivo", MotivoValidacionTerminal.CAJA_NO_EXISTE,
                    "error", "La caja no existe"
            );
        }

        Terminal terminal = terminalRepository.findSqlByNumeroSerie(sn.trim());
        if (terminal == null) {
            return Map.of(
                    "success", false,
                    "motivo", MotivoValidacionTerminal.TERMINAL_NO_EXISTE,
                    "error", "El terminal no existe"
            );
        }

        if (terminal.getCaja() == null) {
            return Map.of(
                    "success", false,
                    "motivo", MotivoValidacionTerminal.TERMINAL_NO_EXISTE,
                    "error", "El terminal no está asociado a ninguna caja"
            );
        }

        if (!terminal.getCaja().getId().equals(cajaId)) {
            return Map.of(
                    "success", false,
                    "motivo", MotivoValidacionTerminal.TERMINAL_YA_ASOCIADO,
                    "error", "El terminal está asociado a otra caja",
                    "cajaIdActual", terminal.getCaja().getId()
            );
        }

        terminal.setCaja(null);
        terminalRepository.save(terminal);

        return Map.of(
                "success", true,
                "mensaje", "Terminal desasignado con éxito",
                "sn", terminal.getNumeroSerie(),
                "cajaId", cajaId
        );
    }

    private TerminalValidacionDto buildTerminalDto(Terminal terminal) {
        if (terminal == null) {
            return null;
        }
        return new TerminalValidacionDto(
                terminal.getNumeroSerie(),
                terminal.getMarca(),
                terminal.getModelo(),
                terminal.getEstado()
        );
    }

    private CajaValidacionDto buildCajaDto(Integer cajaId) {
        if (cajaId == null) {
            return null;
        }
        return new CajaValidacionDto(cajaId, null);
    }

    private CajaValidacionDto buildCajaDto(Caja caja) {
        if (caja == null) {
            return null;
        }
        return new CajaValidacionDto(caja.getId(), caja.getModeloProducto());
    }

    private boolean esModeloCompatible(Terminal terminal, Caja caja) {
        String cajaModelo = normalizarTexto(caja.getModeloProducto());
        String terminalModelo = normalizarTexto(terminal.getModelo());
        String terminalMarcaModelo = normalizarTexto(
                (terminal.getMarca() == null ? "" : terminal.getMarca()) + " " + (terminal.getModelo() == null ? "" : terminal.getModelo())
        );

        return !cajaModelo.isEmpty()
                && (cajaModelo.equals(terminalModelo) || cajaModelo.equals(terminalMarcaModelo));
    }

    private boolean esEstadoValidoParaAsociar(Terminal terminal) {
        return terminal.getEstado() != null
                && terminal.getEstado() != EstadoTerminal.en_transito
                && terminal.getEstado() != EstadoTerminal.nivel_1;
    }

    private String normalizarTexto(String valor) {
        return valor == null
                ? ""
                : valor.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }
}
