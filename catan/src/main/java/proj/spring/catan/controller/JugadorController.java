package proj.spring.catan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import proj.spring.catan.service.JugadorService;

//Fa que la classe sigui un controlador REST: els mètodes retornen DADES (JSON), no vistes
@RestController
//Prefix de totes les URL d'aquesta classe: localhost:8080/api/plataformes
@RequestMapping(path="/api/jugadors")
public class JugadorController {
	
	@Autowired
	private JugadorService service;
	
	@GetMapping(path="/{idJugador}/recursos")
	ResponseEntity<?> getJugador(@PathVariable int idJugador) {
		return service.buscar(idJugador);
	}

}
