package proj.spring.catan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.core.util.Json;
import proj.spring.catan.service.PartidaService;

@RestController
@RequestMapping(path="/api/partida")
public class PartidaController {
	
	@Autowired
	private PartidaService service;
	
	@GetMapping(path="/{idPartida}/tauler")
	ResponseEntity<Json[]> busca(@PathVariable int idPartida) {
		return service.busca(idPartida);
	}
	
	@PostMapping()
	ResponseEntity<Json> iniciarPartida(@RequestBody Json jugadors, @RequestBody(required = false) Json puntsVictoria) {
		return service.iniciarPartida(jugadors, puntsVictoria);
	}
	
	@PostMapping(path="/{idPartida}/daus")
	ResponseEntity<Json> tirarDaus(@PathVariable int idPartida) {
		return service.tirarDaus(idPartida);
	}

}
