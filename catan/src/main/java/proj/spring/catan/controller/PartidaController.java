package proj.spring.catan.controller;

import java.util.List;

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
	ResponseEntity<?> busca(@PathVariable int idPartida) {
		return service.busca(idPartida);
	}
	
	@PostMapping()
	ResponseEntity<?> iniciarPartida(@Valid @RequestBody List<String> jugadors, @Valid @RequestBody(required = false) int puntsVictoria) {
		return service.iniciarPartida(jugadors, puntsVictoria);
	}
	
	@PostMapping(path="/{idPartida}/daus")
	ResponseEntity<?> tirarDaus(@PathVariable int idPartida) {
		return service.tirarDaus(idPartida);
	}

}
