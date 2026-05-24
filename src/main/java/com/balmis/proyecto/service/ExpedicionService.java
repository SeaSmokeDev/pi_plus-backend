package com.balmis.proyecto.service;

import com.balmis.proyecto.model.Caja;
import com.balmis.proyecto.model.EstadoExpedicion;
import com.balmis.proyecto.model.EstadoTerminal;
import com.balmis.proyecto.model.Usuario;
import com.balmis.proyecto.model.Expedicion;
import com.balmis.proyecto.model.Terminal;
import com.balmis.proyecto.model.dtos.ExpedicionGroupListDTO;
import com.balmis.proyecto.model.dtos.ExpedicionListDTO;
import com.balmis.proyecto.model.dtos.ExpedicionLoteRequestDTO;
import com.balmis.proyecto.repository.CajaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.balmis.proyecto.repository.ExpedicionRepository;
import com.balmis.proyecto.repository.TerminalRepository;
import com.balmis.proyecto.repository.UsuarioRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class ExpedicionService {

    @Autowired
    public ExpedicionRepository expedicionRepository;

    @Autowired
    public UsuarioRepository usuarioRepository;

    @Autowired
    public CajaRepository cajaRepository;

    @Autowired
    public TerminalRepository terminalRepository;

    // ************************
    // CONSULTAS
    // ************************  
    @Transactional(readOnly = true)
    public List<Expedicion> findAll() {
        return expedicionRepository.findSqlAll();
    }

    @Transactional(readOnly = true)
    public Expedicion findById(int empleadoId) {
        return expedicionRepository.findSqlById(empleadoId);
    }

    @Transactional(readOnly = true)
    public List<Expedicion> findLikeNombre(String nombreUsuario) {
        return expedicionRepository.findSqlByNombreUsuario(nombreUsuario);
    }

    @Transactional(readOnly = true)
    public List<Expedicion> findLikeDireccion(String direccion) {
        return expedicionRepository.findSqlLikeDireccion(direccion);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return expedicionRepository.count();
    }

    @Transactional(readOnly = true)
    public List<Expedicion> findAllToday() {
        return expedicionRepository.findSqlAllToday();
    }

    @Transactional(readOnly = true)
    public List<Expedicion> search(
            LocalDateTime fechaCreacionDesde,
            LocalDateTime fechaCreacionHasta,
            LocalDateTime fechaRecepcionDesde,
            LocalDateTime fechaRecepcionHasta,
            Integer usuarioId,
            String destino,
            EstadoExpedicion estado
    ) {
        if (destino != null && destino.trim().isEmpty()) {
            destino = null;
        }

        return expedicionRepository.search(
                fechaCreacionDesde,
                fechaCreacionHasta,
                fechaRecepcionDesde,
                fechaRecepcionHasta,
                usuarioId,
                destino,
                estado
        );
    }

    @Transactional(readOnly = true)
    public List<ExpedicionListDTO> searchForList(
            LocalDateTime fechaCreacionDesde,
            LocalDateTime fechaCreacionHasta,
            LocalDateTime fechaRecepcionDesde,
            LocalDateTime fechaRecepcionHasta,
            LocalDateTime fechaEnvioInicioDia,
            LocalDateTime fechaEnvioFinDia,
            Integer usuarioId,
            String destino,
            String referenciaExpedicion,
            EstadoExpedicion estado
    ) {
        if (destino != null && destino.trim().isEmpty()) {
            destino = null;
        }

        if (referenciaExpedicion != null && referenciaExpedicion.trim().isEmpty()) {
            referenciaExpedicion = null;
        }

        return expedicionRepository.searchForList(
                fechaCreacionDesde,
                fechaCreacionHasta,
                fechaRecepcionDesde,
                fechaRecepcionHasta,
                fechaEnvioInicioDia,
                fechaEnvioFinDia,
                usuarioId,
                destino,
                referenciaExpedicion,
                estado
        );
    }

    @Transactional(readOnly = true)
    public List<ExpedicionListDTO> findAllForList() {
        return expedicionRepository.findAllForList();
    }

    @Transactional(readOnly = true)
    public List<ExpedicionListDTO> findTodayForList() {
//        LocalDate today = LocalDate.now();
//        LocalDateTime inicioDia = today.atStartOfDay();
//        LocalDateTime finDia = today.plusDays(1).atStartOfDay();

        return expedicionRepository.findTodayForList();
    }

    @Transactional(readOnly = true)
    public List<ExpedicionGroupListDTO> findTodayGroupedForList() {
        return expedicionRepository.findTodayGroupedForList();
    }

    @Transactional(readOnly = true)
    public List<ExpedicionGroupListDTO> searchGroupedForList(
            LocalDateTime fechaCreacionDesde,
            LocalDateTime fechaCreacionHasta,
            LocalDateTime fechaRecepcionDesde,
            LocalDateTime fechaRecepcionHasta,
            LocalDateTime fechaEnvioInicioDia,
            LocalDateTime fechaEnvioFinDia,
            Integer usuarioId,
            String destino,
            String referenciaExpedicion,
            EstadoExpedicion estado
    ) {
        if (destino != null && destino.trim().isEmpty()) {
            destino = null;
        }

        if (referenciaExpedicion != null && referenciaExpedicion.trim().isEmpty()) {
            referenciaExpedicion = null;
        }

        return expedicionRepository.searchGroupedForList(
                fechaCreacionDesde,
                fechaCreacionHasta,
                fechaRecepcionDesde,
                fechaRecepcionHasta,
                fechaEnvioInicioDia,
                fechaEnvioFinDia,
                usuarioId,
                destino,
                referenciaExpedicion,
                estado
        );
    }

    // ************************
    // ACTUALIZACIONES
    // ************************  
    @Transactional
    public Expedicion save(Expedicion expedicion) {
        if (expedicion.getEstado() == null) {
            expedicion.setEstado(EstadoExpedicion.abierta);
        }
        if (expedicion.getFechaCreacion() == null) {
            expedicion.setFechaCreacion(LocalDateTime.now());
        }
        expedicion.setFechaModificacion(LocalDateTime.now());
        return expedicionRepository.save(expedicion);
    }

    @Transactional
    public ExpedicionGroupListDTO createLote(ExpedicionLoteRequestDTO request) {
        if (request == null) {
            throw new RuntimeException("La solicitud no puede estar vacía");
        }

        if (request.getDireccionDestino() == null || request.getDireccionDestino().trim().isEmpty()) {
            throw new RuntimeException("La dirección de destino es obligatoria");
        }

        if (request.getUsuarioId() == null) {
            throw new RuntimeException("El usuario es obligatorio");
        }

        if (request.getCajaIds() == null || request.getCajaIds().isEmpty()) {
            throw new RuntimeException("Debe indicarse al menos una caja");
        }

        Usuario usu = usuarioRepository.findSqlById(request.getUsuarioId());

        if (usu == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        LocalDateTime date = LocalDateTime.now();

//        LocalDateTime fechaEnvio = request.getFechaEnvio();
//
//        if (fechaEnvio != null && fechaEnvio.isBefore(date)) {
//            throw new RuntimeException("La fecha de envío no puede ser anterior a la fecha actual");
//        }
//        if (fechaEnvio == null) {
//            fechaEnvio = date;
//        }
//        EstadoExpedicion estadoInicial = fechaEnvio.isAfter(date)
//                ? EstadoExpedicion.abierta
//                : EstadoExpedicion.en_transito;
        String referencia = generarReferenciaExpedicion();

        List<Expedicion> expediciones = new ArrayList<>();

        for (Integer cajaId : request.getCajaIds()) {
            Caja caja = cajaRepository.findByIdWithTerminales(cajaId);

            if (caja == null) {
                throw new RuntimeException("Caja no encontrada con ID: " + cajaId);
            }

            Expedicion expedicion = new Expedicion();

            expedicion.setReferenciaExpedicion(referencia);
            expedicion.setFechaCreacion(date);
            expedicion.setFechaModificacion(date);
            expedicion.setFechaEnvio(date);
            expedicion.setFechaRecepcion(null);

            expedicion.setDireccionDestino(request.getDireccionDestino().trim());
            expedicion.setPaquetes(request.getPaquetes() >= 0 ? request.getPaquetes() : 0);
            expedicion.setPeso(request.getPeso() >= 0 ? request.getPeso() : 0);
            expedicion.setNotas(request.getNotas());

            expedicion.setEstado(EstadoExpedicion.en_transito);
            expedicion.setUsuario(usu);
            expedicion.setCaja(caja);

            expediciones.add(expedicion);

            marcarCajaEnTransito(caja);

        }

        List<Expedicion> expedicionesGuardadas = expedicionRepository.saveAll(expediciones);

        return new ExpedicionGroupListDTO(
                referencia,
                date,
                null,
                date,
                date,
                request.getDireccionDestino().trim(),
                usu.getUsuarioSecurity() != null ? usu.getUsuarioSecurity().getUsername() : null,
                EstadoExpedicion.en_transito,
                (long) expedicionesGuardadas.size()
        );

//        return expedicionRepository.saveAll(expediciones);
    }

    private String generarReferenciaExpedicion() {

        String fecha = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        String prefijo = "EXP-" + fecha;

        String ultimaReferencia = expedicionRepository.findLastReferenciaByPrefijo(prefijo);

        int siguienteNumero = 1;

        if (ultimaReferencia != null) {
            String ultimoNumeroTexto = ultimaReferencia.substring(ultimaReferencia.length() - 3);
            int ultimoNumero = Integer.parseInt(ultimoNumeroTexto);
            siguienteNumero = ultimoNumero + 1;
        }

        return prefijo + "-" + String.format("%03d", siguienteNumero);
    }

    private void marcarCajaEnTransito(Caja caja) {
        for (Terminal terminal : caja.getTerminales()) {
            terminal.setEstado(EstadoTerminal.en_transito);
        }

        caja.setPalet(null);

        terminalRepository.saveAll(caja.getTerminales());
        cajaRepository.save(caja);
    }

    @Transactional
    public Expedicion update(int id, Expedicion expedicionDetails) {
        Expedicion expedicion = expedicionRepository.findSqlById(id);
        if (expedicion == null) {
            throw new RuntimeException("Expedicion no encontrada");
        }

        if (expedicionDetails.getFechaRecepcion() != null) {
            expedicion.setFechaRecepcion(expedicionDetails.getFechaRecepcion());
        }

        if (expedicionDetails.getDireccionDestino() != null) {
            expedicion.setDireccionDestino(expedicionDetails.getDireccionDestino());
        }

        if (expedicionDetails.getPaquetes() >= 0) {
            expedicion.setPaquetes(expedicionDetails.getPaquetes());
        }

        if (expedicionDetails.getPeso() >= 0) {
            expedicion.setPeso(expedicionDetails.getPeso());
        }

        if (expedicionDetails.getNotas() != null) {
            expedicion.setNotas(expedicionDetails.getNotas());
        }

        if (expedicionDetails.getEstado() != null) {
            expedicion.setEstado(expedicionDetails.getEstado());
        }

        return expedicionRepository.save(expedicion);
    }

    @Transactional
    public void deleteById(int id) {
        if (!expedicionRepository.existsById(id)) {
            throw new RuntimeException("Expedicion no encontrado");
        }
        expedicionRepository.deleteById(id);
    }

    @Transactional
    public Expedicion reasignarUsuario(int usuarioId, int expedicionId) {
        Expedicion exp = expedicionRepository.findSqlById(expedicionId);

        Usuario usuario = usuarioRepository.findSqlById(usuarioId);

        if ((exp != null) && (usuario != null)) {
            exp.setUsuario(usuario);
            return expedicionRepository.save(exp);
        } else {
            return null;
        }
    }

    @Transactional
    public Expedicion marcarEnTransito(int expId) {
        Expedicion exp = expedicionRepository.findSqlById(expId);
        if (exp == null) {
            throw new RuntimeException("Expedición no encontrada");
        }

        if (exp.getEstado() != EstadoExpedicion.abierta) {
            throw new RuntimeException("Solo se puede cambiar el estado a en_transito desde abierta");
        }

        exp.setEstado(EstadoExpedicion.en_transito);
        exp.setFechaModificacion(java.time.LocalDateTime.now());
        return expedicionRepository.save(exp);
    }

    @Transactional
    public Expedicion marcarRecibida(int expId) {
        Expedicion exp = expedicionRepository.findSqlById(expId);
        if (exp == null) {
            throw new RuntimeException("Expedición no encontrada");
        }

        if (exp.getEstado() != EstadoExpedicion.en_transito) {
            throw new RuntimeException("Solo se puede cambiar el estado a recibida desde en_transito");
        }

        var now = java.time.LocalDateTime.now();
        exp.setEstado(EstadoExpedicion.recibida);
        exp.setFechaModificacion(now);
        exp.setFechaRecepcion(now);
        return expedicionRepository.save(exp);
    }

    /*@Transactional
    public Expedicion desasignarUsuario(int expId) {
        Expedicion exp = expedicionRepository.findSqlById(expId);

        if (exp!=null) {
            exp.setUsuario(null);
            return expedicionRepository.save(exp);
        } else{
            return null;
        }
    } */
}
