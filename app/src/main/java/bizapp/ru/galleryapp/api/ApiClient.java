package bizapp.ru.galleryapp.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by android on 10.07.2018.
 */

public class ApiClient {

    public static String BASE_URL = "https://newsapi.org/v2/";
    public static String API_KEY = "87de8158cc714fb5b2d3c9891ddcb358";

    private static ApiService INSTANCE = null;

    // prevent constructor usage
    private ApiClient() {}

    public static ApiService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(ApiService.class);
        }
        return INSTANCE;
    }
}
