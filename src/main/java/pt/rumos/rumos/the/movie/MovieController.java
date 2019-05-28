/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.rumos.rumos.the.movie;

import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import pt.rumos.rumos.the.movie.BestFilmsForYearResponse.Movie;
import pt.rumos.rumos.the.movie.database.controllers.MoviesDBJpaController;
import pt.rumos.rumos.the.movie.domain.MovieRequest;
import pt.rumos.rumos.the.movie.domain.MovieResponse;

@Path("/movies")
public class MovieController {
    

    @Context
    private UriInfo context;

    public MovieController() {
    }
    
    @GET
    @Path("/test")
    @Produces("text/plan")
    public String test(@QueryParam("name") String name){
        insertIntoTableTest("name");
        return name;
    }
    
    private void insertIntoTableTest(String name){
        BestFilmsForYearResponse best = new BestFilmsForYearResponse();
        BestFilmsForYearResponse.Movie movie = best.new Movie();
        
        movie.setName(name);
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pt.rumos_rumos-the-movie_war_1.0.0-SNAPSHOTPU");
        MoviesDBJpaController moviesController = new MoviesDBJpaController(emf);
        moviesController.create(movie);
    }
    
    @GET
    @Path("/search")
    @Produces("application/json")
    public List<MovieResponse> searchMovie(@QueryParam("name") String name) throws IOException{
        final String uri = "https://api.themoviedb.org/3/search/keyword?api_key=3d885f69e9747a37c64bc72ec966cf02&query="+name+"&page=1";
        
        CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = client.execute(new HttpGet(uri));
        String bodyAsString = EntityUtils.toString(response.getEntity());
        
        BestFilmsForYearResponse responseFromTMDBDTO = new GsonBuilder()
                .create()
                .fromJson(bodyAsString, BestFilmsForYearResponse.class);
        
        List<Movie> movieDB = responseFromTMDBDTO.getMovies();
        
        List<MovieResponse> moviesFrontendResponse = new ArrayList<>();
        
        for (Movie m : movieDB) {
            MovieResponse movieFE = new MovieResponse();
            movieFE.setName(m.getName());
            //movieFE.setDescription(m.getOverview());
            moviesFrontendResponse.add(movieFE);
        }
        System.out.println(moviesFrontendResponse);
        return moviesFrontendResponse; 
        
    }

     @GET
     @Path("/best")
     @Produces("application/json")
     public List<MovieResponse> getMoviesForYear(@QueryParam("year") String year) throws IOException {

         final String uri = "https://api.themoviedb.org/3/discover/movie?primary_release_year=" + year + "&sort_by=vote_average.desc&api_key=3d885f69e9747a37c64bc72ec966cf02";

         CloseableHttpClient client = HttpClientBuilder.create().build();
         CloseableHttpResponse response = client.execute(new HttpGet(uri));
         String bodyAsString = EntityUtils.toString(response.getEntity());

         BestFilmsForYearResponse responseFromTMDBDTO = new GsonBuilder()
                 .create()
                 .fromJson(bodyAsString, BestFilmsForYearResponse.class);

         List<Movie> moviesFromTMDB = responseFromTMDBDTO.getMovies();

          List<MovieResponse> moviesFrontendResponse = new ArrayList<>();
          for (Movie m : moviesFromTMDB) {
              MovieResponse movie = new MovieResponse();
              movie.setName(m.getName());
              //movie.setDescription(m.getOverview());
              moviesFrontendResponse.add(movie);
          }
         return moviesFrontendResponse;
     }

     @GET
     @Produces("application/json")
     public List<MovieResponse> getMovies() {

         //chamar api remota
         MovieResponse movie = new MovieResponse();
         movie.setName("Interstellar");
         return Arrays.asList(movie);
     }

     @PUT
     @Consumes("application/json")
     public MovieResponse updateMovieName(@QueryParam("name") String newName) {

         MovieResponse movie = new MovieResponse();
         movie.setName(newName);
         return movie;
     }

     @POST
     @Consumes("application/json")
     public MovieResponse createMovie(MovieRequest request) {

         MovieResponse response = new MovieResponse();
         response.setName(request.getName());
         return response;
     }
}
