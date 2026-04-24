package detalle.peliculas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import detalle.peliculas.entity.PeliculasEntity;

@Repository
public interface PeliculasRepository extends JpaRepository<PeliculasEntity, Integer> {
}
