package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	private YelpDao dao;
	private Graph<Business, DefaultWeightedEdge> grafo;
	private Map<String, Business> idMap;
	List<Business> percorsoOtt;
	Business localeMigliore;
	private int lungMin;

	
	public Model() {
		this.dao = new YelpDao();
		this.idMap = new HashMap<>();
		this.dao.getAllBusiness(this.idMap);
		this.percorsoOtt = new ArrayList<>();
		localeMigliore = null;
		this.lungMin = 0;
	}
	
	public String creaGrafo(String citta, int anno) {
		// istanzio la struttura del grafo
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		// aggiungo i vertici
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(citta, anno, this.idMap));
		
		// aggiungo gli archi
		for(Business b1 : this.grafo.vertexSet()) {
			for(Business b2 : this.grafo.vertexSet()) {
				// ho una coppia (b1, b2)
				double avg1 = this.dao.calcolaPeso(b1.getBusinessId(), anno);
				double avg2 = this.dao.calcolaPeso(b2.getBusinessId(), anno);
				double peso = avg1-avg2;
				
				if(peso > 0) {
					// scambio source e target
					Graphs.addEdge(this.grafo, b2, b1, peso);
				}
				else if(peso < 0) {
					Graphs.addEdge(this.grafo, b1, b2, -peso);
				}
			}
		}
		
		// console
		// System.out.printf("# vertici: %d\n", this.grafo.vertexSet().size());
		// System.out.printf("# archi: %d\n", this.grafo.edgeSet().size());
		
		return String.format("GRAFO CREATO\n# vertici: %d\n# archi: %d\n", this.grafo.vertexSet().size(), 
				this.grafo.edgeSet().size());
	}
	
	public void localeMigliore() {	// funziona solo per es 1 e 2 ...
		Business migliore = null;
		double max = 0.0;
		for(Business b : this.grafo.vertexSet()) {
			// ho un vertice
			double sommaEntranti = 0.0;
			double sommaUscenti = 0.0;
			double differenza = 0.0;
			
			// calcolo somma pesi entranti
			for(DefaultWeightedEdge e : this.grafo.incomingEdgesOf(b)) {
				sommaEntranti += this.grafo.getEdgeWeight(e);
			}
			
			// calcolo la somma pesi uscenti
			for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(b)) {
				sommaUscenti += this.grafo.getEdgeWeight(e);
			}
			
			// calcolo la differenza e aggiorno il max
			differenza = sommaEntranti-sommaUscenti;
			if(differenza > max) {
				max = differenza;
				migliore = b;
			}
		}
		System.out.printf("LOCALE MIGLIORE: %s\n", migliore.toString());
		this.localeMigliore = migliore;
	}
	
	

	public List<String> getAllCitta() {
		// TODO Auto-generated method stub
		return this.dao.getAllCitta();
	}
	
	public void calcolaPercorso(Business partenza, double x) {
		List<Business> parziale = new ArrayList<>();
		parziale.add(partenza);
		this.percorsoOtt = new ArrayList<Business>();
		this.lungMin = this.grafo.vertexSet().size();
		this.ricerca(parziale, partenza, x);
		
		// console
		System.out.println("Percorso migliore: " + this.percorsoOtt);
	}

	private void ricerca(List<Business> parziale, Business partenza, double x) {		
		if(parziale.contains(this.localeMigliore)) {
			// ho finito il cammino
			this.percorsoOtt = new ArrayList<>(parziale);
			this.lungMin = parziale.size();
			return;
		}
		
		for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(partenza)) {
			// ho un arco uscente dalla mia partenza
			if(this.grafo.getEdgeWeight(e) >= x) {				
				Business target = this.grafo.getEdgeTarget(e);
				if(parziale.contains(target)) {
					break;
				}
				
				// provo ad aggiungere il locale
				parziale.add(target);
				
				// verifico la validità della soluzione parziale
				if(parziale.size() < this.lungMin) {
					// chiamo la ricorsione
					this.ricerca(parziale, target, x);
				}
				
				// backtracking
				parziale.remove(parziale.size()-1);
			}
		}
	}	
	
	public List<Business> getPercorso() {
		return this.percorsoOtt;
	}

	public List<Business> getVertici() {
		// TODO Auto-generated method stub
		List<Business> vertici = new ArrayList<>(this.grafo.vertexSet());
		return vertici;
	}

	public Business getLocaleMigliore() {
		// TODO Auto-generated method stub
		return this.localeMigliore;
	}
}
