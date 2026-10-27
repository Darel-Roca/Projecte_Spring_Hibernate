package proj.spring.catan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.core.util.Json;
import proj.spring.catan.model.Construccio;
import proj.spring.catan.service.JugadorService;


@RestController

@RequestMapping(path={"/api/jugador"})
public class JugadorController {
	
	@Autowired
	private JugadorService service;
	
	@GetMapping(path="/{idJugador}/recursos")
	ResponseEntity<?> getJugador(@PathVariable int idJugador) {
		return service.buscar(idJugador);
	}

	@PostMapping(path="/{idJugador}/construir")
	ResponseEntity<?> construir(@PathVariable int idJugador, @RequestBody Construccio construccio) {
		return service.construir(idJugador, construccio);
	}

	@PostMapping(path="/{idJugador}/passar")
	ResponseEntity<?> passar(@PathVariable int idJugador) {
		return service.passar(idJugador);
	}

}
