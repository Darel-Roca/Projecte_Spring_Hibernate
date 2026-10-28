package proj.spring.catan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	ResponseEntity<?> construir(@PathVariable int idJugador, @RequestParam("tipus") String tipus,
			@RequestParam("posicio") int posicio) {
		return service.construir(idJugador, tipus, posicio);
	}

	@PostMapping(path="/{idJugador}/passar")
	ResponseEntity<?> passar(@PathVariable int idJugador) {
		return service.passar(idJugador);
	}

}
