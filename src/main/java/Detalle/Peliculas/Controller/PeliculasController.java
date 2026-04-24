package detalle.peliculas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import detalle.peliculas.dto.Peliculasdto;
import detalle.peliculas.service.PeliculasService;

@RestController
@RequestMapping("/peliculas")
public class PeliculasController {

    private final PeliculasService service;

    public PeliculasController(PeliculasService service){

        this.service = service;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health(){

        return ResponseEntity.ok("OK");
    } 

    @GetMapping
    public ResponseEntity<CollectionModel<Peliculasdto>> readAll(){
        List<Peliculasdto> peliculas = service.obtenerTodos().stream()
                .map(this::agregarLinks)
                .toList();

        CollectionModel<Peliculasdto> collection = CollectionModel.of(
                peliculas,
                linkTo(methodOn(PeliculasController.class).readAll()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable("id") Integer id){

        Peliculasdto peliculas = service.obtenerPorId(id);

        if (peliculas == null){

            return ResponseEntity.notFound().build();
        } 

        return ResponseEntity.ok(agregarLinks(peliculas));
    }
    
    @PostMapping
    public ResponseEntity<Peliculasdto> create(@RequestBody Peliculasdto body) {
        Peliculasdto creada = agregarLinks(service.crear(body));

        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") Integer id, @RequestBody Peliculasdto request) {
        Peliculasdto actualiza = service.actualizar(id, request);

        if (actualiza == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Pelicula no encontrada. ID: " + id);
        }

        return ResponseEntity.ok(agregarLinks(actualiza));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable("id") Integer id) {
        boolean eliminada = service.eliminar(id);

        if (!eliminada) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Pelicula no encontrada. ID: " + id);
        }

        return ResponseEntity.noContent().build();
    }

    private Peliculasdto agregarLinks(Peliculasdto pelicula) {
        pelicula.removeLinks();
        pelicula.add(linkTo(methodOn(PeliculasController.class).read(pelicula.getId())).withSelfRel());
        pelicula.add(linkTo(methodOn(PeliculasController.class).readAll()).withRel("peliculas"));
        pelicula.add(linkTo(methodOn(PeliculasController.class).actualizar(pelicula.getId(), null)).withRel("actualizar"));
        pelicula.add(linkTo(methodOn(PeliculasController.class).eliminar(pelicula.getId())).withRel("eliminar"));
        return pelicula;
    }
}
