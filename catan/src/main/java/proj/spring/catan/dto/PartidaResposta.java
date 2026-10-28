package proj.spring.catan.dto;

import java.util.ArrayList;
import java.util.List;

import proj.spring.catan.model.EstatPartida;
import proj.spring.catan.model.Hexagon;
import proj.spring.catan.model.Jugador;
import proj.spring.catan.model.Partida;

public record PartidaResposta (Integer id, EstatPartida estat, Integer ronda,
		List<JugadorResposta> jugadors, List<HexagonResposta> tauler) {

	// Conversió entitat -> DTO en un sol lloc
	public static PartidaResposta de(Partida l) {
		
		ArrayList<JugadorResposta> jugadors = new ArrayList<>();
		for (Jugador c : l.getJugadors()) {
			jugadors.add(JugadorResposta.de(c));
		}

		ArrayList<HexagonResposta> tauler = new ArrayList<>();
		for (Hexagon h : l.getTauler()) {
			tauler.add(HexagonResposta.de(h));
		}
		
		return new PartidaResposta(l.getId(),l.getEstat(),l.getRonda(),jugadors,tauler);
	}

}
