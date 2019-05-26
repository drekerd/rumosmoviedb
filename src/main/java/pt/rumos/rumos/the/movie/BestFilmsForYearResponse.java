/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.rumos.rumos.the.movie;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

/**
 *
 * @author osboxes
 */
@Data
public class BestFilmsForYearResponse {
   int page;
        
        @SerializedName("total_results")
        int totalResults;
        @SerializedName("total_pages")
        int totalPages;
        @SerializedName("results")
        List<Movie> movies;

        @Data
        public class Movie {
            @SerializedName("title")
            String name;
            String overview;
        } 
}
