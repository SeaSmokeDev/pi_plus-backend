package com.balmis.proyecto.service;

import com.balmis.proyecto.model.Caja;
import com.balmis.proyecto.model.EstadoExpedicion;
import com.balmis.proyecto.model.EstadoTerminal;
import com.balmis.proyecto.model.Usuario;
import com.balmis.proyecto.model.Expedicion;
import com.balmis.proyecto.model.Terminal;
import com.balmis.proyecto.model.dtos.CajaExpedicionDetailDTO;
import com.balmis.proyecto.model.dtos.ExpedicionGroupListDTO;
import com.balmis.proyecto.model.dtos.ExpedicionListDTO;
import com.balmis.proyecto.model.dtos.ExpedicionLoteEditDTO;
import com.balmis.proyecto.model.dtos.ExpedicionLoteRequestDTO;
import com.balmis.proyecto.model.dtos.ExpeditionQuickViewDTO;
import com.balmis.proyecto.model.dtos.ExpeditionQuickViewPaymentDTO;
import com.balmis.proyecto.model.dtos.TerminalCajaDTO;
import com.balmis.proyecto.repository.CajaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.balmis.proyecto.repository.ExpedicionRepository;
import com.balmis.proyecto.repository.TerminalRepository;
import com.balmis.proyecto.repository.UsuarioRepository;
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
    public Long count() {
        return expedicionRepository.count();
    }

    @Transactional(readOnly = true)
    public List<Expedicion> findAllToday() {
        return expedicionRepository.findSqlAllToday();
    }

    @Transactional(readOnly = true)
    public List<ExpedicionListDTO> findAllForList() {
        return expedicionRepository.findAllForList();
    }

    @Transactional(readOnly = true)
    public List<ExpedicionListDTO> findTodayForList() {
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

    @Transactional(readOnly = true)
    public ExpedicionLoteEditDTO findLoteForEditByReferencia(String referenciaExpedicion) {
        if (referenciaExpedicion == null || referenciaExpedicion.trim().isEmpty()) {
            throw new RuntimeException("La referencia de expedición es obligatoria");
        }

        String referencia = referenciaExpedicion.trim();

        List<Expedicion> expediciones = expedicionRepository.findByReferenciaWithQuickViewData(referencia);

        if (expediciones == null || expediciones.isEmpty()) {
            return null;
        }

        validarExpedicionAbierta(expediciones);

        Expedicion primera = expediciones.get(0);

        String username = null;

        if (primera.getUsuario() != null && primera.getUsuario().getUsuarioSecurity() != null) {
            username = primera.getUsuario().getUsuarioSecurity().getUsername();
        }

        List<CajaExpedicionDetailDTO> cajas = expediciones.stream()
                .filter(expedicion -> expedicion.getCaja() != null)
                .map(expedicion -> {
                    Caja caja = expedicion.getCaja();

                    List<TerminalCajaDTO> terminales = caja.getTerminales()
                            .stream()
                            .map(terminal -> new TerminalCajaDTO(
                            terminal.getModelo(),
                            terminal.getMarca(),
                            terminal.getEstado(),
                            terminal.getNumeroSerie()
                    ))
                            .toList();

                    return new CajaExpedicionDetailDTO(
                            caja.getId(),
                            caja.getEtiqueta(),
                            caja.getModeloProducto(),
                            (long) terminales.size(),
                            terminales
                    );
                })
                .toList();

        return new ExpedicionLoteEditDTO(
                primera.getReferenciaExpedicion(),
                primera.getDireccionDestino(),
                primera.getPaquetes(),
                primera.getPeso(),
                primera.getNotas(),
                primera.getUsuario() != null ? primera.getUsuario().getId() : null,
                username,
                cajas
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
    public ExpedicionGroupListDTO createLote(ExpedicionLoteRequestDTO request, boolean confirmar) {

        validarSolicitudRequest(request);

        Usuario usu = buscarUsuarioObligatorio(request.getUsuarioId());

        LocalDateTime now = LocalDateTime.now();

        String referencia = generarReferenciaExpedicion(now);

        EstadoExpedicion estadoInicial = confirmar ? EstadoExpedicion.en_transito : EstadoExpedicion.abierta;

        LocalDateTime fechaEnvio = confirmar ? now : null;

        List<Expedicion> expediciones = new ArrayList<>();

        for (Integer cajaId : request.getCajaIds()) {
            Caja caja = buscarCajaConTerminales(cajaId);

            validarCajaDisponibleParaExpedicion(caja);

            if (confirmar) {
                marcarCajaEnTransito(caja);
            } else {
                marcarCajaPendienteTransito(caja);
            }
            Expedicion expedicion = crearExpedicionDesdeLote(request, usu, caja, referencia, now, fechaEnvio, estadoInicial);

            expediciones.add(expedicion);

        }

        expedicionRepository.saveAll(expediciones);

        return expedicionRepository.findGroupByReferencia(referencia);
    }

    private void validarSolicitudRequest(ExpedicionLoteRequestDTO request) {
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
    }

    private Usuario buscarUsuarioObligatorio(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findSqlById(usuarioId);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        return usuario;
    }

    private Caja buscarCajaConTerminales(Integer cajaId) {
        Caja caja = cajaRepository.findByIdWithTerminales(cajaId);

        if (caja == null) {
            throw new RuntimeException("Caja no encontrada con ID: " + cajaId);
        }

        return caja;
    }

    private void validarCajaDisponibleParaExpedicion(Caja caja) {
        if (caja.getTerminales() == null || caja.getTerminales().isEmpty()) {
            throw new RuntimeException("La caja " + caja.getEtiqueta() + " no tiene terminales asignados");
        }

        boolean todosOperativos = caja.getTerminales()
                .stream()
                .allMatch(terminal -> terminal.getEstado() == EstadoTerminal.operativo);

        if (!todosOperativos) {
            throw new RuntimeException("La caja " + caja.getEtiqueta() + " no está disponible para expedición");
        }
    }

    private Expedicion crearExpedicionDesdeLote(
            ExpedicionLoteRequestDTO request,
            Usuario usuario,
            Caja caja,
            String referencia,
            LocalDateTime fechaCreacion,
            LocalDateTime fechaEnvio,
            EstadoExpedicion estado) {
        Expedicion expedicion = new Expedicion();

        expedicion.setReferenciaExpedicion(referencia);
        expedicion.setFechaCreacion(fechaCreacion);
        expedicion.setFechaModificacion(fechaCreacion);
        expedicion.setFechaEnvio(fechaEnvio);
        expedicion.setFechaRecepcion(null);

        expedicion.setDireccionDestino(request.getDireccionDestino().trim());
        expedicion.setPaquetes(request.getPaquetes() >= 0 ? request.getPaquetes() : 0);
        expedicion.setPeso(request.getPeso() >= 0 ? request.getPeso() : 0);
        expedicion.setNotas(request.getNotas());

        expedicion.setEstado(estado);
        expedicion.setUsuario(usuario);
        expedicion.setCaja(caja);

        return expedicion;
    }

    private String generarReferenciaExpedicion(LocalDateTime fechaCreacion) {

        String fecha = fechaCreacion.toLocalDate().format(DateTimeFormatter.BASIC_ISO_DATE);

        String prefijo = "EXP-" + fecha + "-";

        String ultimaReferencia = expedicionRepository.findLastReferenciaByPrefijo(prefijo);

        int siguienteNumero = 1;

        if (ultimaReferencia != null) {
            String ultimoNumeroTexto = ultimaReferencia.substring(ultimaReferencia.length() - 3);
            int ultimoNumero = Integer.parseInt(ultimoNumeroTexto);
            siguienteNumero = ultimoNumero + 1;
        }

        return prefijo + String.format("%03d", siguienteNumero);
    }

    private void marcarCajaEnTransito(Caja caja) {
        for (Terminal terminal : caja.getTerminales()) {
            terminal.setEstado(EstadoTerminal.en_transito);
        }
        String etiquetaNueva = "EXP-" + caja.getEtiqueta();
        caja.setEtiqueta(etiquetaNueva);

        caja.setPalet(null);

        terminalRepository.saveAll(caja.getTerminales());
        cajaRepository.save(caja);
    }

    private void marcarCajaPendienteTransito(Caja caja) {
        for (Terminal terminal : caja.getTerminales()) {
            terminal.setEstado(EstadoTerminal.pendiente_transito);
        }

        terminalRepository.saveAll(caja.getTerminales());
    }

    @Transactional
    public ExpedicionGroupListDTO confirmarExpedicion(String referenciaExpedicion, ExpedicionLoteRequestDTO request) {

        if (referenciaExpedicion == null || referenciaExpedicion.trim().isEmpty()) {
            throw new RuntimeException("La referencia de expedición es obligatoria");
        }

        if (request == null) {
            throw new RuntimeException("Los datos de la expedición son obligatorios");
        }

        String referencia = referenciaExpedicion.trim();

        ExpedicionGroupListDTO dtoGuardado = guardarExpedicionAbierta(referencia, request);

        if (dtoGuardado == null) {
            return null;
        }

        List<Expedicion> expediciones = expedicionRepository.findByReferenciaWithCajasAndTerminales(referencia);

        if (expediciones == null || expediciones.isEmpty()) {
            return null;
        }

        validarExpedicionesConfirmables(expediciones);

        LocalDateTime now = LocalDateTime.now();

        for (Expedicion expedicion : expediciones) {
            expedicion.setEstado(EstadoExpedicion.en_transito);
            expedicion.setFechaEnvio(now);
            expedicion.setFechaModificacion(now);

            Caja caja = expedicion.getCaja();

            if (caja != null) {
                marcarCajaEnTransito(caja);
            }
        }

        expedicionRepository.saveAll(expediciones);

        return expedicionRepository.findGroupByReferencia(referenciaExpedicion.trim());
    }

    private void validarExpedicionesConfirmables(List<Expedicion> expediciones) {
        for (Expedicion expedicion : expediciones) {
            if (expedicion.getEstado() != EstadoExpedicion.abierta) {
                throw new RuntimeException(
                        "Solo se pueden confirmar expediciones en estado abierta"
                );
            }

            if (expedicion.getCaja() == null) {
                throw new RuntimeException(
                        "La expedición con referencia "
                        + expedicion.getReferenciaExpedicion()
                        + " no tiene cajas asignadas"
                );
            }

            validarCajaPendienteTransito(expedicion.getCaja());
        }
    }

    private void validarCajaPendienteTransito(Caja caja) {
        if (caja.getTerminales() == null || caja.getTerminales().isEmpty()) {
            throw new RuntimeException("La caja " + caja.getEtiqueta() + " no tiene terminales asignados");
        }

        boolean todosPendientesTransito = caja.getTerminales()
                .stream()
                .allMatch(terminal -> terminal.getEstado() == EstadoTerminal.pendiente_transito);

        if (!todosPendientesTransito) {
            throw new RuntimeException(
                    "La caja " + caja.getEtiqueta() + " no está pendiente de tránsito"
            );
        }
    }

    @Transactional
    public ExpedicionGroupListDTO guardarExpedicionAbierta(
            String referenciaExpedicion,
            ExpedicionLoteRequestDTO request
    ) {
        if (referenciaExpedicion == null || referenciaExpedicion.trim().isEmpty()) {
            throw new RuntimeException("La referencia de expedición es obligatoria");
        }

        validarSolicitudRequest(request);

        String referencia = referenciaExpedicion.trim();

        List<Expedicion> expedicionesActuales = expedicionRepository
                .findByReferenciaWithCajasAndTerminales(referencia);

        if (expedicionesActuales == null || expedicionesActuales.isEmpty()) {
            return null;
        }

        validarExpedicionAbierta(expedicionesActuales);

        Usuario usuario = buscarUsuarioObligatorio(request.getUsuarioId());

        LocalDateTime now = LocalDateTime.now();

        List<Integer> cajaIdsNuevas = request.getCajaIds();

        List<Expedicion> expedicionesAEliminar = new ArrayList<>();

        for (Expedicion expedicion : expedicionesActuales) {
            Caja cajaActual = expedicion.getCaja();

            if (cajaActual == null) {
                throw new RuntimeException("Hay una línea de expedición sin caja asociada");
            }

            boolean cajaSigueEnLaExpedicion = cajaIdsNuevas.contains(cajaActual.getId());

            if (!cajaSigueEnLaExpedicion) {
                liberarCajaPendienteTransito(cajaActual);
                expedicionesAEliminar.add(expedicion);
            }
        }

        if (!expedicionesAEliminar.isEmpty()) {
            expedicionRepository.deleteAll(expedicionesAEliminar);
            expedicionesActuales.removeAll(expedicionesAEliminar);
        }

        for (Expedicion expedicion : expedicionesActuales) {
            actualizarDatosExpedicionAbierta(expedicion, request, usuario, now);
        }

        List<Integer> cajaIdsActuales = expedicionesActuales.stream()
                .map(expedicion -> expedicion.getCaja().getId())
                .toList();

        List<Expedicion> expedicionesNuevas = new ArrayList<>();

        for (Integer cajaId : cajaIdsNuevas) {
            boolean cajaYaExisteEnExpedicion = cajaIdsActuales.contains(cajaId);

            if (!cajaYaExisteEnExpedicion) {
                Caja cajaNueva = buscarCajaConTerminales(cajaId);

                validarCajaDisponibleParaExpedicion(cajaNueva);

                Expedicion nuevaExpedicion = crearExpedicionDesdeLote(
                        request,
                        usuario,
                        cajaNueva,
                        referencia,
                        expedicionesActuales.get(0).getFechaCreacion(),
                        null,
                        EstadoExpedicion.abierta
                );

                nuevaExpedicion.setFechaModificacion(now);

                expedicionesNuevas.add(nuevaExpedicion);

                marcarCajaPendienteTransito(cajaNueva);
            }
        }

        expedicionRepository.saveAll(expedicionesActuales);

        if (!expedicionesNuevas.isEmpty()) {
            expedicionRepository.saveAll(expedicionesNuevas);
        }

        return expedicionRepository.findGroupByReferencia(referencia);
    }

    private void validarExpedicionAbierta(List<Expedicion> expediciones) {
        for (Expedicion expedicion : expediciones) {
            if (expedicion.getEstado() != EstadoExpedicion.abierta) {
                throw new RuntimeException("Solo se pueden editar expediciones abiertas");
            }
        }
    }

    private void liberarCajaPendienteTransito(Caja caja) {
        validarCajaPendienteTransito(caja);

        for (Terminal terminal : caja.getTerminales()) {
            terminal.setEstado(EstadoTerminal.operativo);
        }

        terminalRepository.saveAll(caja.getTerminales());
    }

    private void actualizarDatosExpedicionAbierta(
            Expedicion expedicion,
            ExpedicionLoteRequestDTO request,
            Usuario usuario,
            LocalDateTime fechaModificacion
    ) {
        expedicion.setDireccionDestino(request.getDireccionDestino().trim());
        expedicion.setPaquetes(request.getPaquetes() >= 0 ? request.getPaquetes() : 0);
        expedicion.setPeso(request.getPeso() >= 0 ? request.getPeso() : 0);
        expedicion.setNotas(request.getNotas());

        expedicion.setUsuario(usuario);
        expedicion.setFechaModificacion(fechaModificacion);

        expedicion.setEstado(EstadoExpedicion.abierta);
        expedicion.setFechaEnvio(null);
        expedicion.setFechaRecepcion(null);
    }

    @Transactional(readOnly = true)
    public ExpeditionQuickViewDTO findQuickViewByReferencia(String referenciaExpedicion) {
        List<Expedicion> expediciones = expedicionRepository.findByReferenciaWithQuickViewData(referenciaExpedicion);

        if (expediciones == null || expediciones.isEmpty()) {
            return null;
        }

        Expedicion primera = expediciones.get(0);

        List<ExpeditionQuickViewPaymentDTO> terminales = expediciones.stream()
                .filter(expedicion -> expedicion.getCaja() != null)
                .flatMap(expedicion -> expedicion.getCaja().getTerminales().stream())
                .map(terminal -> new ExpeditionQuickViewPaymentDTO(
                terminal.getModelo(),
                terminal.getMarca(),
                terminal.getEstado(),
                terminal.getNumeroSerie()
        ))
                .toList();

        String username = null;

        if (primera.getUsuario() != null && primera.getUsuario().getUsuarioSecurity() != null) {
            username = primera.getUsuario().getUsuarioSecurity().getUsername();
        }

        return new ExpeditionQuickViewDTO(
                primera.getReferenciaExpedicion(),
                username,
                primera.getFechaEnvio(),
                primera.getDireccionDestino(),
                primera.getPaquetes(),
                primera.getPeso(),
                primera.getNotas(),
                (long) expediciones.size(),
                (long) terminales.size(),
                terminales
        );
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

}
