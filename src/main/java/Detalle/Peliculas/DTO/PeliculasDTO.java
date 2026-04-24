package detalle.peliculas.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Peliculasdto extends RepresentationModel<Peliculasdto> {

    private Integer id;
    private String titulo;
    private Integer anio;
    private String director;
    private String genero;
    private String sinopsis;
}
