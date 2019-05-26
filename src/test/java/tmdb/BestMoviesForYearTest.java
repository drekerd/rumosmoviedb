/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tmdb;

import com.google.gson.ExclusionStrategy;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.UriBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import pt.rumos.rumos.the.movie.MovieController;
import pt.rumos.rumos.the.movie.domain.MovieResponse;

public class BestMoviesForYearTest {

    @Test
    public void getBestMoviesForYear() throws MalformedURLException, IOException {
        MovieController movieController = new MovieController();
        List<MovieResponse> moviesResponse = new ArrayList<>();
        moviesResponse = movieController.getMoviesForYear("2019");
        
        System.out.println("Movies by year" + moviesResponse);
    }
    
    @Test
    public void searchMovieTest() throws IOException{
        String name = "avengers";
        MovieController movieController = new MovieController();
        List<MovieResponse> moviesResponse = new ArrayList<>();
        moviesResponse = movieController.searchMovie(name);
        
        System.out.println("Searched by "+ name + " and got " + moviesResponse);
    }

    @Data
    class BestFilmsForYearResponse {

        int page;
        
        @SerializedName("total_results")
        int totalResults;
        @SerializedName("total_pages")
        int totalPages;

        @SerializedName("results")
        List<Movie> movies;

        @Data
        class Movie {

            String title;
        }
    }
}
