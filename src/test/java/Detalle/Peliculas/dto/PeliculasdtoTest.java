package detalle.peliculas.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

@DisplayName("Pruebas unitarias del DTO de peliculas")
class PeliculasdtoTest {

    private Peliculasdto dto;

    @BeforeEach
    void setUp() {
        dto = new Peliculasdto();
    }

    @Test
    @DisplayName("Debe crear un DTO vacio sin valores ni links")
    void constructorVacio_deberiaCrearDtoSinValoresNiLinks() {
        assertNull(dto.getId());
        assertNull(dto.getTitulo());
        assertNull(dto.getAnio());
        assertNull(dto.getDirector());
        assertNull(dto.getGenero());
        assertNull(dto.getSinopsis());
        assertFalse(dto.hasLinks());
    }

    @Test
    @DisplayName("Debe asignar todos los campos usando el constructor con parametros")
    void constructorConParametros_deberiaAsignarTodosLosCampos() {
        Peliculasdto resultado = new Peliculasdto(
                1,
                "Matrix",
                1999,
                "Lana Wachowski",
                "Ciencia ficcion",
                "Realidad simulada");

        assertEquals(1, resultado.getId());
        assertEquals("Matrix", resultado.getTitulo());
        assertEquals(1999, resultado.getAnio());
        assertEquals("Lana Wachowski", resultado.getDirector());
        assertEquals("Ciencia ficcion", resultado.getGenero());
        assertEquals("Realidad simulada", resultado.getSinopsis());
    }

    @Test
    @DisplayName("Debe actualizar todos los campos")
    void setters_deberianActualizarTodosLosCampos() {
        dto.setId(2);
        dto.setTitulo("Inception");
        dto.setAnio(2010);
        dto.setDirector("Christopher Nolan");
        dto.setGenero("Ciencia ficcion");
        dto.setSinopsis("Suenos compartidos");

        assertEquals(2, dto.getId());
        assertEquals("Inception", dto.getTitulo());
        assertEquals(2010, dto.getAnio());
        assertEquals("Christopher Nolan", dto.getDirector());
        assertEquals("Ciencia ficcion", dto.getGenero());
        assertEquals("Suenos compartidos", dto.getSinopsis());
    }

    @Test
    @DisplayName("Debe agregar y remover links HATEOAS")
    void addYRemoveLinks_deberiaAdministrarLinksHateoas() {
        dto.add(Link.of("/peliculas/1").withSelfRel());

        assertTrue(dto.hasLink("self"));

        dto.removeLinks();

        assertFalse(dto.hasLinks());
    }
}
