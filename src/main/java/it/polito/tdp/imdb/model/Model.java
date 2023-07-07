
package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	private ImdbDAO dao;
	private List<String>allGeneri;
	private Graph<Actor, DefaultWeightedEdge>grafo;
	private List<Actor>allActors;
	private Map<Integer, Actor>idMapActors;

	public Model() {
		this.dao = new ImdbDAO();
		this.allGeneri = new ArrayList<>(dao.listAllGenres());
		this.allActors = new ArrayList<>();
		this.idMapActors = new HashMap<>();
	}

	

	public String creaGrafo(String genre) {
		this.grafo = new SimpleWeightedGraph<Actor, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.allActors = dao.listAllActors(genre);
		Graphs.addAllVertices(grafo, this.allActors);
		
		for(Actor a : allActors) {
			this.idMapActors.put(a.getId(), a);
		}
		
		List<CoppiaA>coppie = new ArrayList<>(dao.listArchi(genre, idMapActors));
		for(CoppiaA x : coppie) {
			Graphs.addEdge(grafo, x.getA1(), x.getA2(), x.getPeso());
		}

		return "Grafo creato con "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi.";
	}
	
	public List<Actor>attoriSimili(Integer aID){
		Actor a = this.idMapActors.get(aID);
		List<Actor>simili = new ArrayList<>();
		ConnectivityInspector<Actor, DefaultWeightedEdge> inspector = new ConnectivityInspector<>(this.grafo);
        Set<Actor> connectedComponents = inspector.connectedSetOf(a);
        for (Actor x : connectedComponents) {
        	simili.add(x);
	    }
        Collections.sort(simili);
        return simili;
	}
	public List<String> getAllGeneri() {
		return allGeneri;
	}
	

	public Graph<Actor, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public List<Actor> getAllActors() {
		return allActors;
	}

	public Map<Integer, Actor> getIdMapActors() {
		return idMapActors;
	}	
}
