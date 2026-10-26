package proj.spring.catan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.core.util.Json;

public class PartidaController {
	
	@GetMapping(path="/{idPartida}/tauler")
	ResponseEntity<Json[]> getTauler(@PathVariable int idPartida) {
		return null;
	}
	
	@PostMapping()
	ResponseEntity<Json> iniciarPartida(@RequestBody Json jugadors, @RequestBody(required = false) Json puntsVictoria) {
		return null;
	}
	
	@PostMapping(path="/{idPartida}/daus")
	ResponseEntity<Json> tirarDaus(@PathVariable int idPartida) {
		return null;
	}

}
