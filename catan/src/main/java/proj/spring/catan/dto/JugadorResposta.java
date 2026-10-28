package proj.spring.catan.dto;

import proj.spring.catan.model.ColorJugador;
import proj.spring.catan.model.Jugador;

public record JugadorResposta(Integer id, String nom, ColorJugador color, Integer ordreTorn, boolean actiu) {
	public static JugadorResposta de(Jugador j) {
		return new JugadorResposta(j.getId(), j.getNom(), j.getColor(), j.getOrdreTorn(), j.isActiu());
	}
}
