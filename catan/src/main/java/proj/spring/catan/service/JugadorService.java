package proj.spring.catan.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import proj.spring.catan.model.Jugador;
import proj.spring.catan.repository.JugadorRepository;

public class JugadorService {

	@Autowired
	private JugadorRepository repository;
	
	public ResponseEntity<?> buscar(int id) {
		Optional<Jugador> j = repository.findById(id);
		if (j.isPresent()) {
			return ResponseEntity.ok(j.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO EXISTEIX CAP JUGADOR AMB ID " + id);
		}
	}
}
