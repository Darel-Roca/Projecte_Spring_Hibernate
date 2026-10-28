package proj.spring.catan.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import proj.spring.catan.model.Jugador;
import proj.spring.catan.service.PartidaService;

@RestController
@RequestMapping(path="/api/partida")
public class PartidaController {
	
	@Autowired
	private PartidaService service;
	
	@GetMapping(path="/{idPartida}/tauler")
	ResponseEntity<?> busca(@Valid @PathVariable int idPartida) {
		return service.busca(idPartida);
	}
	
	@PostMapping()
	ResponseEntity<?> iniciarPartida(@Valid @RequestBody String[] jugadors, @Valid @RequestBody int puntsVictoria) {
		return service.iniciarPartida(jugadors, Objects.isNull(puntsVictoria) ? 10 : puntsVictoria);
	}
	
	@PostMapping(path="/{idPartida}/daus")
	ResponseEntity<?> tirarDaus(@Valid@PathVariable int idPartida) {
		return service.tirarDaus(idPartida);
	}

}
