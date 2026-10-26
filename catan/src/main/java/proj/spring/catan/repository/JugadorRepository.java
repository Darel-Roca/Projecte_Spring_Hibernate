package proj.spring.catan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import proj.spring.catan.model.Jugador;

public interface JugadorRepository extends JpaRepository<Jugador, Integer> {

}
