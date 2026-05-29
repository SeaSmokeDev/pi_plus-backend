package com.balmis.proyecto.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.balmis.proyecto.model.Palet;
import com.balmis.proyecto.model.UbicacionAlmacen;
import com.balmis.proyecto.repository.CajaRepository;
import com.balmis.proyecto.repository.PaletRepository;
import com.balmis.proyecto.repository.UbicacionAlmacenRepository;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaletServiceTest {

    @Mock
    private PaletRepository paletRepository;

    @Mock
    private CajaRepository cajaRepository;

    @Mock
    private UbicacionAlmacenRepository ubicacionAlmacenRepository;

    private PaletService paletService;

    @BeforeEach
    void setUp() {
        paletService = new PaletService();
        paletService.paletRepository = paletRepository;
        paletService.cajaRepository = cajaRepository;
        paletService.ubicacionAlmacenRepository = ubicacionAlmacenRepository;
    }

    @Test
    void actualizarUbicacionPalet_debeAsignarUbicacionCuandoIdEsValido() {
        int paletId = 6;
        int ubicacionId = 4;
        Palet palet = new Palet();
        UbicacionAlmacen ubicacion = new UbicacionAlmacen();
        ubicacion.setId(ubicacionId);

        when(paletRepository.findSqlById(paletId)).thenReturn(palet);
        when(ubicacionAlmacenRepository.findSqlById(ubicacionId)).thenReturn(ubicacion);

        Map<String, Object> result = paletService.actualizarUbicacionPalet(paletId, ubicacionId);

        assertTrue((Boolean) result.get("success"));
        assertEquals(paletId, result.get("paletId"));
        assertEquals(ubicacionId, result.get("ubicacionAlmacenId"));
        verify(paletRepository).updateUbicacionAlmacenId(paletId, ubicacionId);
    }

    @Test
    void actualizarUbicacionPalet_debeDesasignarUbicacionCuandoIdEsNull() {
        int paletId = 6;
        Palet palet = new Palet();

        when(paletRepository.findSqlById(paletId)).thenReturn(palet);

        Map<String, Object> result = paletService.actualizarUbicacionPalet(paletId, null);

        assertTrue((Boolean) result.get("success"));
        assertEquals(paletId, result.get("paletId"));
        assertNull(result.get("ubicacionAlmacenId"));
        verify(paletRepository).updateUbicacionAlmacenId(paletId, null);
        verify(ubicacionAlmacenRepository, never()).findSqlById(org.mockito.ArgumentMatchers.anyInt());
    }

    @Test
    void actualizarUbicacionPalet_debeResponderErrorCuandoPaletNoExiste() {
        int paletId = 999;
        when(paletRepository.findSqlById(paletId)).thenReturn(null);

        Map<String, Object> result = paletService.actualizarUbicacionPalet(paletId, 4);

        assertFalse((Boolean) result.get("success"));
        assertEquals("Palet no encontrado", result.get("error"));
        assertEquals(paletId, result.get("paletId"));
        verify(paletRepository, never()).updateUbicacionAlmacenId(org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.any());
    }
}
