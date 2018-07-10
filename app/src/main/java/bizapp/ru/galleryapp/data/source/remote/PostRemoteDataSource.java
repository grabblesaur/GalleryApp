package bizapp.ru.galleryapp.data.source.remote;

import android.support.annotation.NonNull;

import bizapp.ru.galleryapp.api.ApiClient;
import bizapp.ru.galleryapp.api.ApiService;
import bizapp.ru.galleryapp.data.PostResponse;
import bizapp.ru.galleryapp.data.source.PostDataSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Implementation of the data source
 */

public class PostRemoteDataSource implements PostDataSource {

    private static final String TAG = PostRemoteDataSource.class.getName();

    private static PostRemoteDataSource INSTANCE;

    public static PostRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PostRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private PostRemoteDataSource() {}

    /**
     * Note: {@link LoadPostsCallback#onDataNotAvailable()} is never fired.
     * In a real remote data source implementation, this would be fired if the
     * server can't be contracted or the server returns an error.
     * @param callback
     */
    @Override
    public void getPosts(@NonNull final LoadPostsCallback callback) {
        ApiService apiClient = ApiClient.getInstance();
        apiClient.getPosts("us", "business", ApiClient.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PostResponse>() {
                    @Override
                    public void accept(PostResponse postResponse) throws Exception {
                        callback.onPostsLoaded(postResponse.getPosts());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callback.onDataNotAvailable();
                    }
                });
    }
}
