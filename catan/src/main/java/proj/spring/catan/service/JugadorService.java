package proj.spring.catan.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import proj.spring.catan.model.Assentament;
import proj.spring.catan.model.Construccio;
import proj.spring.catan.model.EstatPartida;
import proj.spring.catan.model.Jugador;
import proj.spring.catan.model.Partida;
import proj.spring.catan.repository.JugadorRepository;

@Service
public class JugadorService {

	@Autowired
	private JugadorRepository repository;

	public ResponseEntity<?> buscar(int id) {
		Optional<Jugador> j = repository.findById(id);
		if (j.isPresent()) {
			return ResponseEntity.ok(j.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO EXISTEIX CAP JUGADOR AMB ID " + id);
		}
	}

	public ResponseEntity<?> construir(int idJugador, String tipus, int posicio) {
		switch (tipus) {
		case ("CARRETERA"):

			break;
		case ("POBLAT "):
			break;
		case ("CIUTAT"):
			break;
		default:
			break;
		}

		return null;
	}

	public ResponseEntity<?> passar(int idJugador) {

		Optional<Jugador> jugadorOpt = repository.findById(idJugador);
		Jugador jugador = jugadorOpt.get();
		if (jugadorOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existeix el jugador" + jugador.getNom());
		}

		if (!jugador.isActiu()) {
			return ResponseEntity.badRequest().body("El jugador" + jugador.getNom() + "no està actiu");
		}

		Partida partida = jugador.getPartida();

		if (partida.getEstat() == EstatPartida.EN_CURS) {
			if (!partida.isDausTirats()) {
				return ResponseEntity.badRequest().body("No s'han tirat els daus encara");
			}
		}

		if (partida.getEstat() == EstatPartida.PREPARACIO) {
			int carreteres = 0;
			int assentaments = 0;
			List<Construccio> propietats = jugador.getPropietari();

			for (Construccio propietat : propietats) {
				if (propietat instanceof Assentament) {
					assentaments++;
				} else {
					carreteres++;
				}
			}

			if (assentaments < partida.getRonda() || carreteres < partida.getRonda()) {
				return ResponseEntity.badRequest()
						.body("El jugador " + jugador.getNom() + "encara no ha col·locat el poblat i la carretera");
			}
		}

		if (partida.isLladrePendent()) {
			return ResponseEntity.badRequest().body("El lladre encara està pendent");
		}

		if ((jugador.getOrdreTorn() == 1 && partida.getRonda() % 2 == 1)
				|| (jugador.getOrdreTorn() == 4 && partida.getRonda() % 2 == 0)) {
			partida.setRonda(partida.getRonda() + 1);
		} else {

			int seguentIndex = jugador.getOrdreTorn() + (partida.getRonda() % 2 == 0 ? 1 : -1);
			partida.setActiu(partida.getJugadors().get(seguentIndex));
		}

		if (partida.getRonda() == 3 && partida.getEstat() == EstatPartida.PREPARACIO) {
			partida.setEstat(EstatPartida.EN_CURS);
			partida.setRonda(1);
		}

		partida.setDausTirats(false);

		return ResponseEntity.ok().build();
	}
}
