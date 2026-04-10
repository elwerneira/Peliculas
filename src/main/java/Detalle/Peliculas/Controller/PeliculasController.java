package Detalle.Peliculas.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Detalle.Peliculas.DTO.Peliculasdto;
import Detalle.Peliculas.Service.PeliculasService;

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
    public ResponseEntity<List<Peliculasdto>> readAll(){

        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable("id") Integer id){

        Peliculasdto peliculas = service.obtenerPorId(id);

        if (peliculas == null){

            return ResponseEntity.notFound().build();
        } 

        return ResponseEntity.ok(peliculas);
    }
    
    @PostMapping
    public ResponseEntity<Peliculasdto> create(@RequestBody Peliculasdto body) {

        return ResponseEntity.ok(service.crear(body));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") Integer id, @RequestBody Peliculasdto request) {
        Peliculasdto actualiza = service.actualizar(id, request);

        if (actualiza == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Pelicula no encontrada. ID: " + id);
        }

        return ResponseEntity.ok(actualiza);
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
}
