package Detalle.Peliculas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Detalle.Peliculas.Entity.PeliculasEntity;

public interface PeliculasRepository extends JpaRepository<PeliculasEntity, Integer> {
}
