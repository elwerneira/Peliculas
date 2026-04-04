package Detalle.Peliculas.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Detalle.Peliculas.DTO.PeliculasDTO;
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
    public ResponseEntity<List<PeliculasDTO>> readAll(){

        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable("id") Integer id){

        PeliculasDTO peliculas = service.obtenerPorId(id);

        if (peliculas == null){

            return ResponseEntity.notFound().build();
        } 

        return ResponseEntity.ok(peliculas);
    }
    
    @PostMapping
    public ResponseEntity<PeliculasDTO> create(@RequestBody PeliculasDTO body) {

        return ResponseEntity.ok(service.crear(body));
    }
}
