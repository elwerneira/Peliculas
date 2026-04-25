package detalle.peliculas.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Pruebas unitarias de la entidad de peliculas")
class PeliculasEntityTest {

    private PeliculasEntity entity;

    @BeforeEach
    void setUp() {
        entity = new PeliculasEntity();
    }

    @Test
    @DisplayName("Debe crear una entidad vacia sin valores")
    void constructorVacio_deberiaCrearEntidadSinValores() {
        assertNull(entity.getId());
        assertNull(entity.getTitulo());
        assertNull(entity.getAnio());
        assertNull(entity.getDirector());
        assertNull(entity.getGenero());
        assertNull(entity.getSinopsis());
    }

    @Test
    @DisplayName("Debe asignar todos los campos usando el constructor con parametros")
    void constructorConParametros_deberiaAsignarTodosLosCampos() {
        PeliculasEntity resultado = new PeliculasEntity(
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
        entity.setId(2);
        entity.setTitulo("Inception");
        entity.setAnio(2010);
        entity.setDirector("Christopher Nolan");
        entity.setGenero("Ciencia ficcion");
        entity.setSinopsis("Suenos compartidos");

        assertEquals(2, entity.getId());
        assertEquals("Inception", entity.getTitulo());
        assertEquals(2010, entity.getAnio());
        assertEquals("Christopher Nolan", entity.getDirector());
        assertEquals("Ciencia ficcion", entity.getGenero());
        assertEquals("Suenos compartidos", entity.getSinopsis());
    }
}
