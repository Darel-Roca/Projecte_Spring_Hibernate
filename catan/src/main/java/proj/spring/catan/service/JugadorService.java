package proj.spring.catan.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.swagger.v3.core.util.Json;
import proj.spring.catan.model.Jugador;
import proj.spring.catan.repository.JugadorRepository;

@Service
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

	public ResponseEntity<?> construir(int idJugador, Json construccio) {
		return null;
	}

	public ResponseEntity<?> passar(int idJugador) {
		return null;
	}
}
