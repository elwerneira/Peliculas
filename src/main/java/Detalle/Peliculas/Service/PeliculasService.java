package Detalle.Peliculas.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import Detalle.Peliculas.DTO.Peliculasdto;
import Detalle.Peliculas.Entity.PeliculasEntity;
import Detalle.Peliculas.Repository.PeliculasRepository;

@Service
public class PeliculasService {

    private final PeliculasRepository repository;

    public PeliculasService(PeliculasRepository repository) {
        this.repository = repository;
    }

    public List<Peliculasdto> obtenerTodos() {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public Peliculasdto obtenerPorId(Integer id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    public Peliculasdto crear(Peliculasdto nuevo) {
        PeliculasEntity entity = toEntity(nuevo);
        entity.setId(null);

        return toDto(repository.save(entity));
    }

    public Peliculasdto actualizar(Integer id, Peliculasdto datos) {
        Optional<PeliculasEntity> peliculaExistente = repository.findById(id);

        if (peliculaExistente.isEmpty()) {
            return null;
        }

        PeliculasEntity entity = peliculaExistente.get();
        entity.setTitulo(datos.getTitulo());
        entity.setAnio(datos.getAnio());
        entity.setDirector(datos.getDirector());
        entity.setGenero(datos.getGenero());
        entity.setSinopsis(datos.getSinopsis());

        return toDto(repository.save(entity));
    }

    public boolean eliminar(Integer id) {
        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }

    private Peliculasdto toDto(PeliculasEntity entity) {
        return new Peliculasdto(
                entity.getId(),
                entity.getTitulo(),
                entity.getAnio(),
                entity.getDirector(),
                entity.getGenero(),
                entity.getSinopsis());
    }

    private PeliculasEntity toEntity(Peliculasdto dto) {
        return new PeliculasEntity(
                dto.getId(),
                dto.getTitulo(),
                dto.getAnio(),
                dto.getDirector(),
                dto.getGenero(),
                dto.getSinopsis());
    }
}
