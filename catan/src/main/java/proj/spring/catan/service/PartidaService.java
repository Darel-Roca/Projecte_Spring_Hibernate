package proj.spring.catan.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import proj.spring.catan.model.EstatPartida;
import proj.spring.catan.model.Jugador;
import proj.spring.catan.model.Partida;
import proj.spring.catan.repository.PartidaRepository;

@Service
public class PartidaService {
	
	@Autowired
	private PartidaRepository repository;
	
	public ResponseEntity<?> busca(int idPartida) {
		Optional<Partida> p = repository.findById(idPartida);
		if (p.isPresent()) {
			return ResponseEntity.ok(p.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO EXISTEIX CAP PARTIDA AMB ID " + idPartida);
		}
	}
	
	public ResponseEntity<?> iniciarPartida(List<Jugador> jugadors) {
		Partida partida = new  Partida();
		partida.setEstat(EstatPartida.PREPARACIO);
		for (Jugador j : jugadors) {
			partida.getJugadors().add(j);
		}
		return ResponseEntity.ok(partida);
	}
	
	public ResponseEntity<?> iniciarPartida(List<Jugador> jugadors, int puntsVictoria) {
		Partida partida = new  Partida();
		partida.setEstat(EstatPartida.PREPARACIO);
		for (Jugador j : jugadors) {
			partida.getJugadors().add(j);
		}
		partida.setPuntsPerGuanyar(puntsVictoria);
		return ResponseEntity.ok(partida);
	}
	
	public ResponseEntity<?> tirarDaus(int idPartida) {
		return null;
	}

}
