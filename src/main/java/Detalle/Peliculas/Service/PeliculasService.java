package Detalle.Peliculas.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import Detalle.Peliculas.DTO.PeliculasDTO;
import Detalle.Peliculas.Entity.PeliculasEntity;
import Detalle.Peliculas.Repository.PeliculasRepository;

@Service
public class PeliculasService {

    private final PeliculasRepository repository;

    public PeliculasService(PeliculasRepository repository) {
        this.repository = repository;
    }

    public List<PeliculasDTO> obtenerTodos() {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public PeliculasDTO obtenerPorId(Integer id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    public PeliculasDTO crear(PeliculasDTO nuevo) {
        PeliculasEntity entity = toEntity(nuevo);
        entity.setId(null);

        return toDto(repository.save(entity));
    }

    private PeliculasDTO toDto(PeliculasEntity entity) {
        return new PeliculasDTO(
                entity.getId(),
                entity.getTitulo(),
                entity.getAnio(),
                entity.getDirector(),
                entity.getGenero(),
                entity.getSinopsis());
    }

    private PeliculasEntity toEntity(PeliculasDTO dto) {
        return new PeliculasEntity(
                dto.getId(),
                dto.getTitulo(),
                dto.getAnio(),
                dto.getDirector(),
                dto.getGenero(),
                dto.getSinopsis());
    }
}
