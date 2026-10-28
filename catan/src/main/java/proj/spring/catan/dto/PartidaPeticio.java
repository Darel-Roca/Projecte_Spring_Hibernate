package proj.spring.catan.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record PartidaPeticio (

		@NotNull(message = "Cal la llista de jugadors (pot ser buida)")
		List<String> jugadors,
		
		// Opcional: si no hi és, el videojoc no té fitxa tècnica
		Integer puntsVictoria
		
) {
	
}