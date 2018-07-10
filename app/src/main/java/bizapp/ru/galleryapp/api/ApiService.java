package bizapp.ru.galleryapp.api;

import bizapp.ru.galleryapp.data.PostResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by android on 10.07.2018.
 */

// "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=API_KEY"


public interface ApiService {

    @GET("top-headlines")
    Observable<PostResponse> getPosts(@Query("country") String country,
                                      @Query("category") String category,
                                      @Query("apiKey") String apiKey);

}
