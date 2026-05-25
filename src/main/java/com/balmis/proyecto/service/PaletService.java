package com.balmis.proyecto.service;

import com.balmis.proyecto.model.Caja;
import com.balmis.proyecto.model.Palet;
import com.balmis.proyecto.repository.CajaRepository;
import com.balmis.proyecto.repository.PaletRepository;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaletService {

    @Autowired
    public PaletRepository paletRepository;

    @Autowired
    public CajaRepository cajaRepository;

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

    @Transactional(readOnly = true)
    public List<Palet> findFree() {
        return paletRepository.findSqlFree();
    }

    @Transactional
    public Map<String, Object> desasignarCajaDePalet(int paletId, int cajaId) {
        Palet palet = paletRepository.findSqlById(paletId);
        if (palet == null) {
            return Map.of(
                    "success", false,
                    "error", "Palet no encontrado",
                    "paletId", paletId
            );
        }

        Caja caja = cajaRepository.findSqlById(cajaId);
        if (caja == null) {
            return Map.of(
                    "success", false,
                    "error", "Caja no encontrada",
                    "cajaId", cajaId
            );
        }

        if (caja.getPalet() == null) {
            return Map.of(
                    "success", false,
                    "error", "La caja no está asociada a ningún palé",
                    "cajaId", cajaId
            );
        }

        if (!caja.getPalet().getId().equals(paletId)) {
            return Map.of(
                    "success", false,
                    "error", "La caja está asociada a otro palé",
                    "cajaId", cajaId,
                    "paletIdActual", caja.getPalet().getId()
            );
        }

        caja.setPalet(null);
        cajaRepository.save(caja);

        return Map.of(
                "success", true,
                "mensaje", "Caja desasignada del palé con éxito",
                "paletId", paletId,
                "cajaId", cajaId
        );
    }

    @Transactional
    public Map<String, Object> desasignarPaletDeUbicacion(int ubicacionId, int paletId) {
        Palet palet = paletRepository.findSqlById(paletId);
        if (palet == null) {
            return Map.of(
                    "success", false,
                    "error", "Palet no encontrado",
                    "paletId", paletId
            );
        }

        if (palet.getUbicacionAlmacen() == null) {
            return Map.of(
                    "success", false,
                    "error", "El palet no está asociado a ningún habitáculo",
                    "paletId", paletId
            );
        }

        if (!palet.getUbicacionAlmacen().getId().equals(ubicacionId)) {
            return Map.of(
                    "success", false,
                    "error", "El palet está asociado a otro habitáculo",
                    "paletId", paletId,
                    "ubicacionAlmacenIdActual", palet.getUbicacionAlmacen().getId()
            );
        }

        palet.setUbicacionAlmacen(null);
        paletRepository.save(palet);

        return Map.of(
                "success", true,
                "mensaje", "Palet desasignado del habitáculo con éxito",
                "paletId", paletId,
                "ubicacionAlmacenId", ubicacionId
        );
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
