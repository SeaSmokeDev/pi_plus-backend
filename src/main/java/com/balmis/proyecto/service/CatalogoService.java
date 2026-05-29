package com.balmis.proyecto.service;

import com.balmis.proyecto.model.dtos.MarcaModelosProductoDto;
import com.balmis.proyecto.model.dtos.CapacidadMaximaModeloResponseDto;
import com.balmis.proyecto.model.dtos.MarcasCatalogoResponseDto;
import com.balmis.proyecto.model.dtos.ModeloProductoCatalogoDto;
import com.balmis.proyecto.model.dtos.ModelosPorMarcaResponseDto;
import com.balmis.proyecto.repository.CajaRepository;
import com.balmis.proyecto.repository.TerminalRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CatalogoService {

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private CajaRepository cajaRepository;

    @Transactional(readOnly = true)
    public List<ModeloProductoCatalogoDto> obtenerModelosProductoFlat() {
        List<Object[]> rows = terminalRepository.findDistinctMarcaAndModelo();
        List<ModeloProductoCatalogoDto> result = new ArrayList<>();

        for (Object[] row : rows) {
            String marca = row[0] != null ? row[0].toString().trim() : "";
            String modelo = row[1] != null ? row[1].toString().trim() : "";
            if (!marca.isEmpty() && !modelo.isEmpty()) {
                result.add(new ModeloProductoCatalogoDto(marca, modelo));
            }
        }

        result.sort(Comparator
                .comparing(ModeloProductoCatalogoDto::getMarca, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(ModeloProductoCatalogoDto::getModeloProducto, String.CASE_INSENSITIVE_ORDER));

        return result;
    }

    @Transactional(readOnly = true)
    public List<MarcaModelosProductoDto> obtenerModelosProductoAgrupado() {
        List<ModeloProductoCatalogoDto> flat = obtenerModelosProductoFlat();
        Map<String, Set<String>> grouped = new LinkedHashMap<>();

        for (ModeloProductoCatalogoDto item : flat) {
            grouped.computeIfAbsent(item.getMarca(), x -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER))
                    .add(item.getModeloProducto());
        }

        List<MarcaModelosProductoDto> result = new ArrayList<>();
        grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER))
                .forEach(entry -> result.add(new MarcaModelosProductoDto(entry.getKey(), new ArrayList<>(entry.getValue()))));

        return result;
    }

    @Transactional(readOnly = true)
    public boolean existeModeloProducto(String modeloProducto) {
        return resolverModeloProductoCanonico(modeloProducto).isPresent();
    }

    @Transactional(readOnly = true)
    public Optional<String> resolverModeloProductoCanonico(String valorEntrada) {
        if (valorEntrada == null || valorEntrada.trim().isEmpty()) {
            return Optional.empty();
        }

        String normalizedTarget = normalizarTexto(valorEntrada);
        return obtenerModelosProductoFlat().stream()
                .filter(item -> {
                    String modelo = normalizarTexto(item.getModeloProducto());
                    String marcaModelo = normalizarTexto(item.getMarca() + " " + item.getModeloProducto());
                    return normalizedTarget.equals(modelo) || normalizedTarget.equals(marcaModelo);
                })
                .map(item -> item.getMarca().trim() + " " + item.getModeloProducto().trim())
                .findFirst();
    }

    @Transactional(readOnly = true)
    public MarcasCatalogoResponseDto obtenerMarcasTerminales() {
        List<String> marcas = terminalRepository.findDistinctMarcas().stream()
                .map(String::trim)
                .filter(valor -> !valor.isBlank())
                .toList();

        return new MarcasCatalogoResponseDto(marcas);
    }

    @Transactional(readOnly = true)
    public ModelosPorMarcaResponseDto obtenerModelosPorMarca(String marca) {
        String marcaNormalizada = marca == null ? "" : marca.trim();
        if (marcaNormalizada.isBlank()) {
            throw new IllegalArgumentException("La marca es obligatoria");
        }

        List<String> modelos = terminalRepository.findDistinctModelosByMarca(marcaNormalizada).stream()
                .map(String::trim)
                .filter(valor -> !valor.isBlank())
                .toList();

        return new ModelosPorMarcaResponseDto(marcaNormalizada, modelos);
    }

    @Transactional(readOnly = true)
    public CapacidadMaximaModeloResponseDto obtenerCapacidadMaximaPorModelo(String modelo) {
        String marcaNormalizada = modelo == null ? "" : modelo.trim();
        if (marcaNormalizada.isBlank()) {
            throw new IllegalArgumentException("La marca es obligatoria");
        }

        Integer maxCapacity = cajaRepository.findMaxCapacityByMarca(marcaNormalizada);
        return new CapacidadMaximaModeloResponseDto(marcaNormalizada, maxCapacity);
    }

    private String normalizarTexto(String valor) {
        return valor == null
                ? ""
                : valor.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }
}
