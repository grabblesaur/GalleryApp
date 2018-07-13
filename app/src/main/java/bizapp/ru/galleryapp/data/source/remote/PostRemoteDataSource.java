package bizapp.ru.galleryapp.data.source.remote;

import android.util.Log;

import bizapp.ru.galleryapp.api.ApiClient;
import bizapp.ru.galleryapp.api.ApiService;
import bizapp.ru.galleryapp.data.Post;
import bizapp.ru.galleryapp.data.PostResponse;
import bizapp.ru.galleryapp.data.source.PostDataSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Implementation of the data source
 */

public class PostRemoteDataSource implements PostDataSource {

    private static final String TAG = PostRemoteDataSource.class.getSimpleName();

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
    public void getPosts(String category, final LoadPostsCallback callback) {
        ApiService apiClient = ApiClient.getInstance();
        apiClient.getPosts("ru", category, ApiClient.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PostResponse>() {
                    @Override
                    public void accept(PostResponse postResponse) throws Exception {
                        Log.i(TAG, "onNext: " + postResponse.getTotalResults());
                        callback.onPostsLoaded(postResponse.getPosts());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callback.onDataNotAvailable();
                    }
                });
    }

    @Override
    public void savePost(String category, Post post) {
        // do nothing
    }

    @Override
    public void deleteAllPosts() {
        // do nothing
    }
}
