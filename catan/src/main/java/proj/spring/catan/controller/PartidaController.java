package proj.spring.catan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import proj.spring.catan.service.PartidaService;
import proj.spring.catan.dto.PartidaPeticio;

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
	ResponseEntity<?> iniciarPartida(@Valid @RequestBody PartidiaPeticio pp) {
		return service.iniciarPartida(pp.jugadors, pp.puntsVictoria == null ? 10 : pp.puntsVictoria);
	}
	
	@PostMapping(path="/{idPartida}/daus")
	ResponseEntity<?> tirarDaus(@Valid@PathVariable int idPartida) {
		return service.tirarDaus(idPartida);
	}

}
