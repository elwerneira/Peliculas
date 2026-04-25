package detalle.peliculas.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import detalle.peliculas.dto.Peliculasdto;
import detalle.peliculas.service.PeliculasService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias del controlador de peliculas")
class PeliculasControllerTest {

    @Mock
    private PeliculasService service;

    private PeliculasController controller;

    @BeforeEach
    void setUp() {
        controller = new PeliculasController(service);
    }

    @Test
    @DisplayName("Debe retornar OK en el endpoint de health")
    void health_deberiaRetornarOk() {
        ResponseEntity<String> respuesta = controller.health();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("OK", respuesta.getBody());
    }

    @Test
    @DisplayName("Debe retornar una coleccion con peliculas y links")
    void readAll_deberiaRetornarCollectionModelConLinks() {
        when(service.obtenerTodos()).thenReturn(List.of(
                crearDto(1, "Matrix", 1999, "Lana Wachowski", "Ciencia ficcion", "Realidad simulada")));

        ResponseEntity<CollectionModel<Peliculasdto>> respuesta = controller.readAll();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(1, respuesta.getBody().getContent().size());
        assertTrue(respuesta.getBody().hasLink("self"));
        assertTrue(respuesta.getBody().getContent().iterator().next().hasLink("self"));
        assertTrue(respuesta.getBody().getContent().iterator().next().hasLink("peliculas"));
        assertTrue(respuesta.getBody().getContent().iterator().next().hasLink("actualizar"));
        assertTrue(respuesta.getBody().getContent().iterator().next().hasLink("eliminar"));
    }

    @Test
    @DisplayName("Debe retornar una coleccion vacia con link self cuando no hay peliculas")
    void readAll_cuandoNoHayPeliculas_deberiaRetornarCollectionModelVacioConSelfLink() {
        when(service.obtenerTodos()).thenReturn(List.of());

        ResponseEntity<CollectionModel<Peliculasdto>> respuesta = controller.readAll();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertTrue(respuesta.getBody().getContent().isEmpty());
        assertTrue(respuesta.getBody().hasLink("self"));
    }

    @Test
    @DisplayName("Debe retornar OK con links cuando la pelicula existe")
    void read_cuandoExiste_deberiaRetornarOkConLinks() {
        when(service.obtenerPorId(1)).thenReturn(
                crearDto(1, "Inception", 2010, "Christopher Nolan", "Ciencia ficcion", "Suenos"));

        ResponseEntity<?> respuesta = controller.read(1);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertTrue(respuesta.getBody() instanceof Peliculasdto);

        Peliculasdto body = (Peliculasdto) respuesta.getBody();
        assertEquals(1, body.getId());
        assertEquals("Inception", body.getTitulo());
        assertTrue(body.hasLink("self"));
        assertTrue(body.hasLink("peliculas"));
        assertTrue(body.hasLink("actualizar"));
        assertTrue(body.hasLink("eliminar"));
    }

    @Test
    @DisplayName("Debe retornar NOT_FOUND cuando la pelicula no existe")
    void read_cuandoNoExiste_deberiaRetornarNotFound() {
        when(service.obtenerPorId(99)).thenReturn(null);

        ResponseEntity<?> respuesta = controller.read(99);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
    }

    @Test
    @DisplayName("Debe retornar CREATED con la pelicula creada")
    void create_deberiaRetornarCreatedConPelicula() {
        Peliculasdto request = crearDto(null, "Interstellar", 2014, "Christopher Nolan", "Ciencia ficcion",
                "Viaje espacial");
        Peliculasdto creada = crearDto(1, "Interstellar", 2014, "Christopher Nolan", "Ciencia ficcion",
                "Viaje espacial");

        when(service.crear(request)).thenReturn(creada);

        ResponseEntity<Peliculasdto> respuesta = controller.create(request);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(1, respuesta.getBody().getId());
        assertTrue(respuesta.getBody().hasLink("self"));
        assertTrue(respuesta.getBody().hasLink("peliculas"));
        verify(service).crear(request);
    }

    @Test
    @DisplayName("Debe retornar OK cuando actualiza una pelicula existente")
    void actualizar_cuandoExiste_deberiaRetornarOk() {
        Peliculasdto request = crearDto(null, "Titulo nuevo", 2024, "Director nuevo", "Accion", "Sinopsis nueva");
        Peliculasdto actualizada = crearDto(1, "Titulo nuevo", 2024, "Director nuevo", "Accion", "Sinopsis nueva");

        when(service.actualizar(1, request)).thenReturn(actualizada);

        ResponseEntity<?> respuesta = controller.actualizar(1, request);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertTrue(respuesta.getBody() instanceof Peliculasdto);
        assertTrue(((Peliculasdto) respuesta.getBody()).hasLink("actualizar"));
    }

    @Test
    @DisplayName("Debe retornar NOT_FOUND con mensaje cuando no puede actualizar")
    void actualizar_cuandoNoExiste_deberiaRetornarNotFoundConMensaje() {
        Peliculasdto request = crearDto(null, "Titulo nuevo", 2024, "Director nuevo", "Accion", "Sinopsis nueva");

        when(service.actualizar(99, request)).thenReturn(null);

        ResponseEntity<?> respuesta = controller.actualizar(99, request);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertEquals("Pelicula no encontrada. ID: 99", respuesta.getBody());
    }

    @Test
    @DisplayName("Debe retornar NO_CONTENT cuando elimina una pelicula existente")
    void eliminar_cuandoExiste_deberiaRetornarNoContent() {
        when(service.eliminar(1)).thenReturn(true);

        ResponseEntity<?> respuesta = controller.eliminar(1);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        verify(service).eliminar(1);
    }

    @Test
    @DisplayName("Debe retornar NOT_FOUND con mensaje cuando no puede eliminar")
    void eliminar_cuandoNoExiste_deberiaRetornarNotFoundConMensaje() {
        when(service.eliminar(99)).thenReturn(false);

        ResponseEntity<?> respuesta = controller.eliminar(99);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertEquals("Pelicula no encontrada. ID: 99", respuesta.getBody());
    }

    private Peliculasdto crearDto(Integer id, String titulo, Integer anio, String director, String genero,
            String sinopsis) {
        return new Peliculasdto(id, titulo, anio, director, genero, sinopsis);
    }
}
