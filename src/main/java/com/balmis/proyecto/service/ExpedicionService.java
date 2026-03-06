package com.balmis.proyecto.service;

import com.balmis.proyecto.model.EstadoExpedicion;
import com.balmis.proyecto.model.Usuario;
import com.balmis.proyecto.model.Expedicion;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.balmis.proyecto.repository.ExpedicionRepository;
import com.balmis.proyecto.repository.UsuarioRepository;
import java.time.LocalDateTime;

@Service
public class ExpedicionService {

    @Autowired
    public ExpedicionRepository expedicionRepository;

    @Autowired
    public UsuarioRepository usuarioRepository;

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

    // ************************
    // ACTUALIZACIONES
    // ************************  
    @Transactional
    public Expedicion save(Expedicion expedicion) {
        if(expedicion.getEstado() == null) expedicion.setEstado(EstadoExpedicion.abierta);
        if(expedicion.getFechaCreacion() == null) expedicion.setFechaCreacion(LocalDateTime.now());
        expedicion.setFechaModificacion(LocalDateTime.now());
        return expedicionRepository.save(expedicion);
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
