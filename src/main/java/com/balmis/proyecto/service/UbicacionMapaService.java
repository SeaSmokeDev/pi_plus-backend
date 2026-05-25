package com.balmis.proyecto.service;

import com.balmis.proyecto.model.Caja;
import com.balmis.proyecto.model.Estanteria;
import com.balmis.proyecto.model.Palet;
import com.balmis.proyecto.model.UbicacionAlmacen;
import com.balmis.proyecto.model.dtos.AlmacenResumenDto;
import com.balmis.proyecto.model.dtos.CajaResumenDto;
import com.balmis.proyecto.model.dtos.EstanteriaResumenDto;
import com.balmis.proyecto.model.dtos.PaletResumenDto;
import com.balmis.proyecto.model.dtos.PasilloResumenDto;
import com.balmis.proyecto.model.dtos.UbicacionMapaDto;
import com.balmis.proyecto.repository.EstanteriaRepository;
import com.balmis.proyecto.repository.UbicacionAlmacenRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UbicacionMapaService {

    @Autowired
    private UbicacionAlmacenRepository ubicacionAlmacenRepository;

    @Autowired
    private EstanteriaRepository estanteriaRepository;

    @Transactional
    public List<UbicacionMapaDto> obtenerMapa(Integer pasilloId) {
        List<UbicacionAlmacen> ubicacionesReales = pasilloId == null
                ? ubicacionAlmacenRepository.findAllForMapa()
                : ubicacionAlmacenRepository.findAllForMapaByPasilloId(pasilloId);

        List<Estanteria> estanterias = pasilloId == null
                ? estanteriaRepository.findAllWithPasilloForMapa()
                : estanteriaRepository.findAllWithPasilloForMapaByPasillo(pasilloId);

        Map<String, UbicacionAlmacen> ubicacionPorEstanteriaNivel = new HashMap<>();
        for (UbicacionAlmacen ubicacion : ubicacionesReales) {
            ubicacionPorEstanteriaNivel.put(buildKey(ubicacion.getEstanteria().getId(), ubicacion.getNivel()), ubicacion);
        }

        // Materializa en BD ubicaciones faltantes para que el frontend siempre reciba un PK real reutilizable.
        for (Estanteria estanteria : estanterias) {
            for (int nivel = 1; nivel <= estanteria.getNivelesMaximos(); nivel++) {
                String key = buildKey(estanteria.getId(), nivel);
                if (!ubicacionPorEstanteriaNivel.containsKey(key)) {
                    UbicacionAlmacen nueva = new UbicacionAlmacen();
                    nueva.setEstanteria(estanteria);
                    nueva.setNivel(nivel);
                    nueva.setReferencia(buildReferencia(estanteria, nivel));
                    nueva = ubicacionAlmacenRepository.save(nueva);
                    ubicacionPorEstanteriaNivel.put(key, nueva);
                }
            }
        }

        List<UbicacionMapaDto> resultado = new ArrayList<>();
        for (Estanteria estanteria : estanterias) {
            for (int nivel = 1; nivel <= estanteria.getNivelesMaximos(); nivel++) {
                UbicacionAlmacen ubicacion = ubicacionPorEstanteriaNivel.get(buildKey(estanteria.getId(), nivel));
                resultado.add(mapearUbicacionReal(ubicacion));
            }
        }
        return resultado;
    }

    private UbicacionMapaDto mapearUbicacionReal(UbicacionAlmacen ubicacion) {
        Set<Palet> palets = ubicacion.getPalets();

        Palet paletVista = palets.stream()
                .filter(p -> p.getId() != null)
                .min(Comparator.comparing(Palet::getId))
                .orElse(null);

        List<CajaResumenDto> cajasVista = new ArrayList<>();
        if (paletVista != null && paletVista.getCajas() != null) {
            paletVista.getCajas().stream()
                    .sorted(Comparator.comparing(Caja::getId, Comparator.nullsLast(Integer::compareTo)))
                    .forEach(caja -> cajasVista.add(new CajaResumenDto(caja.getId(), caja.getEtiqueta())));
        }

        int ocupacionActual = palets.stream()
                .map(Palet::getCajas)
                .filter(cajas -> cajas != null)
                .mapToInt(Set::size)
                .sum();

        PaletResumenDto pale = paletVista == null ? null : new PaletResumenDto(
                paletVista.getId(),
                paletVista.getDescripcion(),
                paletVista.getMaterial() != null ? paletVista.getMaterial().name().toLowerCase() : null,
                paletVista.getTipo() != null ? paletVista.getTipo().name().toLowerCase() : null,
                paletVista.getCapacidadMaxCajas()
        );

        return new UbicacionMapaDto(
                ubicacion.getId(),
                ubicacion.getId(),
                ubicacion.getReferencia(),
                (AlmacenResumenDto) null,
                new PasilloResumenDto(
                        ubicacion.getEstanteria().getPasillo().getId(),
                        ubicacion.getEstanteria().getPasillo().getNumeroPasillo()
                ),
                new EstanteriaResumenDto(
                        ubicacion.getEstanteria().getId(),
                        ubicacion.getEstanteria().getCodigo(),
                        ubicacion.getNivel(),
                        ubicacion.getEstanteria().getCapacidadNivel()
                ),
                pale,
                ocupacionActual,
                cajasVista
        );
    }

    private String buildKey(Integer estanteriaId, int nivel) {
        return estanteriaId + ":" + nivel;
    }

    private String buildReferencia(Estanteria estanteria, int nivel) {
        return estanteria.getPasillo().getNumeroPasillo() + estanteria.getCodigo() + nivel;
    }
}
