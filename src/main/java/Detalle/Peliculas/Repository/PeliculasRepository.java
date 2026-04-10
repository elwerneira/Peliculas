package Detalle.Peliculas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Detalle.Peliculas.Entity.PeliculasEntity;

@Repository
public interface PeliculasRepository extends JpaRepository<PeliculasEntity, Integer> {
}
