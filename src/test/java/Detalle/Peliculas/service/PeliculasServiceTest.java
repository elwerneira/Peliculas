package detalle.peliculas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import detalle.peliculas.dto.Peliculasdto;
import detalle.peliculas.entity.PeliculasEntity;
import detalle.peliculas.repository.PeliculasRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias del servicio de peliculas")
class PeliculasServiceTest {

    @Mock
    private PeliculasRepository repository;

    private PeliculasService service;
    
    @BeforeEach
    void setUp() {
        service = new PeliculasService(repository);
    }

    @Test
    @DisplayName("Debe retornar una lista de DTOs al obtener todas las peliculas")
    void obtenerTodos_deberiaRetornarListaDeDtos() {
        PeliculasEntity entity = crearEntity(1, "Matrix", 1999, "Lana Wachowski", "Ciencia ficcion", "Realidad simulada");

        when(repository.findAll()).thenReturn(List.of(entity));

        List<Peliculasdto> resultado = service.obtenerTodos();

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getId());
        assertEquals("Matrix", resultado.get(0).getTitulo());
        assertEquals(1999, resultado.get(0).getAnio());
        assertEquals("Lana Wachowski", resultado.get(0).getDirector());
        assertEquals("Ciencia ficcion", resultado.get(0).getGenero());
        assertEquals("Realidad simulada", resultado.get(0).getSinopsis());
    }

    @Test
    @DisplayName("Debe retornar una lista vacia cuando no existen peliculas")
    void obtenerTodos_cuandoNoHayPeliculas_deberiaRetornarListaVacia() {
        when(repository.findAll()).thenReturn(List.of());

        List<Peliculasdto> resultado = service.obtenerTodos();

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debe retornar un DTO cuando la pelicula existe")
    void obtenerPorId_cuandoExiste_deberiaRetornarDto() {
        PeliculasEntity entity = crearEntity(1, "Inception", 2010, "Christopher Nolan", "Ciencia ficcion", "Suenos");

        when(repository.findById(1)).thenReturn(Optional.of(entity));

        Peliculasdto resultado = service.obtenerPorId(1);

        assertEquals(1, resultado.getId());
        assertEquals("Inception", resultado.getTitulo());
        assertEquals(2010, resultado.getAnio());
        assertEquals("Christopher Nolan", resultado.getDirector());
        assertEquals("Ciencia ficcion", resultado.getGenero());
        assertEquals("Suenos", resultado.getSinopsis());
    }

    @Test
    @DisplayName("Debe retornar null cuando la pelicula no existe")
    void obtenerPorId_cuandoNoExiste_deberiaRetornarNull() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        Peliculasdto resultado = service.obtenerPorId(99);

        assertNull(resultado);
    }

    @Test
    @DisplayName("Debe guardar una pelicula nueva con id null y retornar el DTO creado")
    void crear_deberiaGuardarEntidadConIdNullYRetornarDto() {
        Peliculasdto nuevo = crearDto(15, "Interstellar", 2014, "Christopher Nolan", "Ciencia ficcion", "Viaje espacial");
        PeliculasEntity guardada = crearEntity(1, "Interstellar", 2014, "Christopher Nolan", "Ciencia ficcion", "Viaje espacial");

        when(repository.save(any(PeliculasEntity.class))).thenReturn(guardada);

        Peliculasdto resultado = service.crear(nuevo);

        ArgumentCaptor<PeliculasEntity> captor = ArgumentCaptor.forClass(PeliculasEntity.class);
        verify(repository).save(captor.capture());

        assertNull(captor.getValue().getId());
        assertEquals("Interstellar", captor.getValue().getTitulo());
        assertEquals(1, resultado.getId());
        assertEquals("Interstellar", resultado.getTitulo());
    }

    @Test
    @DisplayName("Debe actualizar los campos y guardar cuando la pelicula existe")
    void actualizar_cuandoExiste_deberiaActualizarCamposYGuardar() {
        PeliculasEntity existente = crearEntity(1, "Titulo anterior", 2000, "Director anterior", "Drama", "Sinopsis anterior");
        Peliculasdto datos = crearDto(null, "Titulo nuevo", 2024, "Director nuevo", "Accion", "Sinopsis nueva");
        PeliculasEntity guardada = crearEntity(1, "Titulo nuevo", 2024, "Director nuevo", "Accion", "Sinopsis nueva");

        when(repository.findById(1)).thenReturn(Optional.of(existente));
        when(repository.save(any(PeliculasEntity.class))).thenReturn(guardada);

        Peliculasdto resultado = service.actualizar(1, datos);

        ArgumentCaptor<PeliculasEntity> captor = ArgumentCaptor.forClass(PeliculasEntity.class);
        verify(repository).save(captor.capture());

        assertEquals(1, captor.getValue().getId());
        assertEquals("Titulo nuevo", captor.getValue().getTitulo());
        assertEquals(2024, captor.getValue().getAnio());
        assertEquals("Director nuevo", captor.getValue().getDirector());
        assertEquals("Accion", captor.getValue().getGenero());
        assertEquals("Sinopsis nueva", captor.getValue().getSinopsis());
        assertEquals("Titulo nuevo", resultado.getTitulo());
    }

    @Test
    @DisplayName("Debe retornar null y no guardar cuando la pelicula no existe")
    void actualizar_cuandoNoExiste_deberiaRetornarNullYNoGuardar() {
        Peliculasdto datos = crearDto(null, "Titulo nuevo", 2024, "Director nuevo", "Accion", "Sinopsis nueva");

        when(repository.findById(99)).thenReturn(Optional.empty());

        Peliculasdto resultado = service.actualizar(99, datos);

        assertNull(resultado);
        verify(repository, never()).save(any(PeliculasEntity.class));
    }

    @Test
    @DisplayName("Debe eliminar y retornar true cuando la pelicula existe")
    void eliminar_cuandoExiste_deberiaEliminarYRetornarTrue() {
        when(repository.existsById(1)).thenReturn(true);

        boolean resultado = service.eliminar(1);

        assertTrue(resultado);
        verify(repository).deleteById(1);
    }

    @Test
    @DisplayName("Debe retornar false y no eliminar cuando la pelicula no existe")
    void eliminar_cuandoNoExiste_deberiaRetornarFalseYNoEliminar() {
        when(repository.existsById(99)).thenReturn(false);

        boolean resultado = service.eliminar(99);

        assertFalse(resultado);
        verify(repository, never()).deleteById(anyInt());
    }

    private Peliculasdto crearDto(Integer id, String titulo, Integer anio, String director, String genero, String sinopsis) {
        return new Peliculasdto(id, titulo, anio, director, genero, sinopsis);
    }

    private PeliculasEntity crearEntity(Integer id, String titulo, Integer anio, String director, String genero,
            String sinopsis) {
        return new PeliculasEntity(id, titulo, anio, director, genero, sinopsis);
    }
}
