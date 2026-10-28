package proj.spring.catan.dto;


import proj.spring.catan.model.Hexagon;
import proj.spring.catan.model.TipusTerreny;

public record HexagonResposta (Integer id, TipusTerreny terreny, Integer numero) {

	// Conversió entitat -> DTO en un sol lloc
	public static HexagonResposta de(Hexagon l) {
		return new HexagonResposta(l.getId(),l.getTerreny(), l.getNumero());
	}

}
