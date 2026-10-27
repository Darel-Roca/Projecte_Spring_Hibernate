package proj.spring.catan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.swagger.v3.core.util.Json;
import proj.spring.catan.repository.PartidaRepository;

@Service
public class PartidaService {
	
	@Autowired
	private PartidaRepository repository;
	
	public ResponseEntity<Json[]> busca(int idPartida) {
		return null;
	}
	
	public ResponseEntity<Json> iniciarPartida(Json jugadors, Json puntsVictoria) {
		return null;
	}
	
	public ResponseEntity<Json> tirarDaus(int idPartida) {
		return null;
	}

}
