package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.CoppiaA;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> listAllGenres(){
		String sql = "SELECT DISTINCT mg.genre "
				+ "FROM movies_genres mg "
				+ "ORDER BY mg.genre ";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				String s = res.getString("genre");
				result.add(s);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Actor> listAllActors(String genre) {
		String sql = "SELECT DISTINCT a.* "
				+ "FROM actors a, roles r, movies_genres mg "
				+ "WHERE a.id = r.actor_id AND r.movie_id = mg.movie_id AND mg.genre = ? ";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genre);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*public Integer getWeight(Actor x, Actor y, String genre) {
		String sql = "SELECT COUNT(DISTINCT mg1.movie_id)AS peso "
				+ "FROM actors a1, roles r1, movies_genres mg1, actors a2, roles r2, movies_genres mg2 "
				+ "WHERE a1.id = r1.actor_id AND mg1.movie_id = r1.movie_id AND mg1.genre = ? "
				+ "AND a2.id = r2.actor_id AND mg2.movie_id = r2.movie_id AND mg2.genre = ? "
				+ "AND mg1.movie_id = mg2.movie_id "
				+ "AND a1.id = ? AND a2.id = ? ";
		Integer peso = 0;
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genre);
			st.setString(2, genre);
			st.setInt(3, x.getId());
			st.setInt(4, y.getId());
			ResultSet res = st.executeQuery();
			if (res.first()) {
				peso = res.getInt("peso");
			}
			conn.close();
			return peso;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}*/
	
	
	public List<CoppiaA>listArchi(String genre, Map<Integer, Actor>idMapActors) {
		String sql = "SELECT a1.id AS a1, a2.id AS a2, COUNT(DISTINCT mg1.movie_id)AS peso "
				+ "FROM actors a1, roles r1, movies_genres mg1, actors a2, roles r2, movies_genres mg2 "
				+ "WHERE a1.id = r1.actor_id AND mg1.movie_id = r1.movie_id AND mg1.genre = ? "
				+ "AND a2.id = r2.actor_id AND mg2.movie_id = r2.movie_id AND mg2.genre = ? "
				+ "AND mg1.movie_id = mg2.movie_id AND a1.id != a2.id "
				+ "GROUP BY a1.id, a2.id "
				+ "HAVING peso >= 1 ";
		List<CoppiaA> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genre);
			st.setString(2, genre);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Integer a1ID = res.getInt("a1");
				Integer a2ID = res.getInt("a2");
				Integer peso = res.getInt("peso");
				Actor a1 = idMapActors.get(a1ID);
				Actor a2 = idMapActors.get(a2ID);
				CoppiaA c = new CoppiaA(a1, a2, peso);
				result.add(c);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
	
}
