package proj.spring.catan.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import proj.spring.catan.dto.PartidaResposta;
import proj.spring.catan.model.*;
import proj.spring.catan.repository.PartidaRepository;

@Service
public class PartidaService {
	
	@Autowired
	private PartidaRepository repository;
	
	public ResponseEntity<?> busca(int idPartida) {
		Optional<Partida> p = repository.findById(idPartida);
		if (p.isPresent()) {
			return ResponseEntity.ok(p.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO EXISTEIX CAP PARTIDA AMB ID " + idPartida);
		}
	}
	
	public ResponseEntity<?> iniciarPartida(List<String> jugadors, int puntsVictoria) {
		Partida partida = new Partida();
		partida.setEstat(EstatPartida.PREPARACIO);
		partida.setDataInici(LocalDate.now());
		partida.setRonda(1); 
		partida.setUltimaTirada(1); 
		partida.setPuntsPerGuanyar(puntsVictoria);
        partida.setDausTirats(false);
        partida.setLladrePendent(false);
        Collections.shuffle(jugadors);
        ColorJugador[] colors = ColorJugador.values();
        List<Jugador> llistaJugadors = new ArrayList<>();

        for (int i = 0; i < jugadors.size(); i++) {
            Jugador j = new Jugador();
            j.setNom(jugadors.get(i));
            j.setPartida(partida);
            j.setColor(colors[i]);
            j.setOrdreTorn(i + 1);
            j.setCavallersJugats(0);

            for (TipusRecurs tipus : TipusRecurs.values()) {
                RecursJugador rj = new RecursJugador();
                rj.setJugador(j);
                rj.setTipus(tipus);
                rj.setQuantitat(0);
            }
            llistaJugadors.add(j);
        }

        partida.setJugadors(llistaJugadors);
        
        String rutaHexagons = "src/main/resources/dades/hexagons.csv";
        String rutaVertexs = "src/main/resources/dades/vertexs.csv";
        String rutaArestes = "src/main/resources/dades/arestes.csv";
        String rutaHexagonVertex = "src/main/resources/dades/hexagon_vertex.csv";

        // Lectura de CSV
        try {
	        List<Hexagon> hexagons = carregarHexagonsDesdeCSV(rutaHexagons, partida);
	        List<Vertex> vertexs = carregarVertexsDesdeCSV(rutaVertexs); 
	        Map<Integer, Hexagon> hexagonsPerCodi = new HashMap<>();
	        Map<Integer, Vertex> vertexsPerCodi = new HashMap<>();
	        
	        generarMapaAleatori(hexagons);
	
	        Hexagon hexagonDesert = null;
	        for (Hexagon h : hexagons) {
	            hexagonsPerCodi.put(h.getCodi(), h);
	            if (TipusTerreny.DESERT.equals(h.getTerreny())) {
	                hexagonDesert = h;
	            }
	        }
	        partida.setTauler(hexagons);
	
	        if (hexagonDesert != null) {
	            partida.setLladre(hexagonDesert); 
	        }
	
	        for (Vertex v : vertexs) {
	            v.setPartida(partida);
	            vertexsPerCodi.put(v.getCodi(), v);
	        }
	
	        List<Aresta> arestes = carregarArestesDesdeCSV(rutaArestes, vertexsPerCodi);
	        for (Aresta a : arestes) {
	            a.setPartida(partida);
	        }
	
	        carregarHexagonVertexDesdeCSV(rutaHexagonVertex, hexagonsPerCodi, vertexsPerCodi);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        repository.save(partida);
		return ResponseEntity.ok(PartidaResposta.de(partida));
	}
	
	public ResponseEntity<?> tirarDaus(int idPartida) {
		Optional<Partida> p = repository.findById(idPartida);
		if (p.isPresent()) {
			Partida partida = p.get();
			try {
				if (partida.isDausTirats()) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO S'HAN TIRAT ELS DAUS");
				}
				int dau1 = (int) (Math.random() * 6) + 1;
				int dau2 = (int) (Math.random() * 6) + 1;
				int resultatTirada = dau1 + dau2;

				partida.setUltimaTirada(resultatTirada);
				partida.setDausTirats(true);
				
				System.out.println("\n ELS DAUS HAN RATLLAT UN: " + resultatTirada + " (" + dau1 + " + " + dau2 + ")");
				System.out.println("=========================================");

				if (resultatTirada == 7) {
					System.out.println("🦹 ¡Ha sortit un 7! S'activa el lladre. No hi ha producció de recursos.");
					partida.setLladrePendent(true);
				} 
				else {
					int lladreHexId = (partida.getLladre() != null) ? partida.getLladre().getId() : -1;
					
					List<Hexagon> hexagonsProductius = new ArrayList<>();
					
					List<Hexagon> hexagons = partida.getTauler();
					
					for (Hexagon h : hexagons) {
						if (h.getNumero() == resultatTirada && h.getId() != lladreHexId) {
							hexagonsProductius.add(h);
						}
					}

					if (hexagonsProductius.isEmpty()) {
						System.out.println("ℹ️ Cap hexàgon lliure conté el número " + resultatTirada + ". Ningú rep recursos.");
					}

					for (Hexagon h : hexagonsProductius) {
						TipusRecurs recursGenerat = obtenirTipusRecursPerTerreny(h.getTerreny());
						if (recursGenerat == null) continue;
						
						List<Assentament> assentamentsAdjacents = new ArrayList<>();
						
						List<HexagonVertex> hexagonVertexs = h.getHexagonvertices();
						
						for (HexagonVertex hv : hexagonVertexs) {
							if (hv.getVertex().getAssentament() != null) {
								assentamentsAdjacents.add(hv.getVertex().getAssentament());
							}
						}

						for (Assentament a : assentamentsAdjacents) {
							int quantitatAFegir = 0;

							if (a instanceof Poblat) {
								quantitatAFegir = 1;
							} else if (a instanceof Ciutat) {
								quantitatAFegir = 2;
							}

							if (quantitatAFegir > 0) {
								for (RecursJugador rj : a.getJugador().getInventari()) {
									if (rj.getTipus() == recursGenerat) {
										rj.setQuantitat(rj.getQuantitat()+quantitatAFegir);
									}
								}

								System.out.printf(" Jugador %s rep %d de %s (Hexàgon %d - %s)\n", 
										a.getJugador().getNom(), quantitatAFegir, recursGenerat, h.getId(), h.getTerreny());
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ResponseEntity.ok(p.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO EXISTEIX CAP PARTIDA AMB ID " + idPartida);
		}
	}
	
	private void generarMapaAleatori(List<Hexagon> hexagons) {
		List<TipusTerreny> terrenys = new ArrayList<>(List.of(
			TipusTerreny.DESERT,
			TipusTerreny.BOSC, TipusTerreny.BOSC, TipusTerreny.BOSC, TipusTerreny.BOSC,
			TipusTerreny.PASTURES, TipusTerreny.PASTURES, TipusTerreny.PASTURES, TipusTerreny.PASTURES,
			TipusTerreny.CAMPS, TipusTerreny.CAMPS, TipusTerreny.CAMPS, TipusTerreny.CAMPS,
			TipusTerreny.TURONS, TipusTerreny.TURONS, TipusTerreny.TURONS,
			TipusTerreny.MUNTANYES, TipusTerreny.MUNTANYES, TipusTerreny.MUNTANYES
		));
		
		while (terrenys.size() < hexagons.size()) {
			terrenys.add(TipusTerreny.BOSC); 
		}

		List<Integer> numeros = new ArrayList<>(List.of(
			2, 12, 
			3, 3, 4, 4, 5, 5, 6, 6, 
			8, 8, 9, 9, 10, 10, 11, 11
		));
		
		while (numeros.size() < hexagons.size()) {
			numeros.add(2); 
		}

		Collections.shuffle(terrenys);
		Collections.shuffle(numeros);

		int numIndex = 0;
		for (Hexagon h : hexagons) {
			TipusTerreny terreny = terrenys.remove(0);
			h.setTerreny(terreny);

			if (TipusTerreny.DESERT.equals(terreny)) {
				h.setNumero(0); 
			} else {
				h.setNumero(numeros.get(numIndex++));
			}
		}
	}

	private List<String[]> llegirCSV(String ruta) throws Exception {
		List<String[]> linies = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
			String l; boolean primera = true;
			while ((l = br.readLine()) != null) {
				if (l.trim().isEmpty()) continue;
				if (primera) { primera = false; continue; }
				linies.add(l.split(","));
			}
		}
		return linies;
	}

	private List<Hexagon> carregarHexagonsDesdeCSV(String ruta, Partida p) throws Exception {
		List<Hexagon> llista = new ArrayList<>();
		for (String[] c : llegirCSV(ruta)) {
			Hexagon h = new Hexagon();
			h.setPartida(p);
			h.setCodi(Integer.parseInt(c[0].trim()));
			h.setQ(Integer.parseInt(c[1].trim()));
			h.setR(Integer.parseInt(c[2].trim()));
			h.setTerreny(TipusTerreny.DESERT);
			h.setNumero(0);
			llista.add(h);
		}
		return llista;
	}

	private List<Vertex> carregarVertexsDesdeCSV(String ruta) throws Exception {
		List<Vertex> llista = new ArrayList<>();
		for (String[] c : llegirCSV(ruta)) {
			Vertex v = new Vertex();
			v.setCodi(Integer.parseInt(c[0].trim()));
			llista.add(v);
		}
		return llista;
	}

	private List<Aresta> carregarArestesDesdeCSV(String ruta, Map<Integer, Vertex> vertexsPerCodi) throws Exception {
		List<Aresta> llista = new ArrayList<>();
		for (String[] c : llegirCSV(ruta)) {
			Aresta a = new Aresta();
			a.setCodi(Integer.parseInt(c[0].trim()));
			a.setVertexA(cercarPerCodi(vertexsPerCodi, c[1], ruta));
			a.setVertexB(cercarPerCodi(vertexsPerCodi, c[2], ruta));
			llista.add(a);
		}
		return llista;
	}

	private void carregarHexagonVertexDesdeCSV(String r,
			Map<Integer, Hexagon> hexagonsPerCodi, Map<Integer, Vertex> vertexsPerCodi) throws Exception {
		for (String[] c : llegirCSV(r)) {
			HexagonVertex hv = new HexagonVertex();
			hv.setHexagon(cercarPerCodi(hexagonsPerCodi, c[0], r));
			hv.setVertex(cercarPerCodi(vertexsPerCodi, c[1], r));
			hv.setPosicio(Integer.parseInt(c[2].trim()));
		}
	}

	private <T> T cercarPerCodi(Map<Integer, T> elements, String valor, String ruta) {
		int codi = Integer.parseInt(valor.trim());
		T element = elements.get(codi);
		if (element == null) {
			throw new IllegalArgumentException("Codi " + codi + " inexistent al tauler: " + ruta);
		}
		return element;
	}
	
	private TipusRecurs obtenirTipusRecursPerTerreny(TipusTerreny terreny) {
		switch (terreny) {
			case BOSC:      return TipusRecurs.FUSTA;
			case TURONS:    return TipusRecurs.MAO;
			case PASTURES:  return TipusRecurs.LLANA;
			case CAMPS:     return TipusRecurs.BLAT;
			case MUNTANYES: return TipusRecurs.MINERAL;
			default:        return null;
		}
	}

}
