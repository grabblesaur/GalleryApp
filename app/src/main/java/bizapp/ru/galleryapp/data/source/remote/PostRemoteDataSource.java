package bizapp.ru.galleryapp.data.source.remote;

import android.util.Log;

import bizapp.ru.galleryapp.api.ApiClient;
import bizapp.ru.galleryapp.api.ApiService;
import bizapp.ru.galleryapp.data.Post;
import bizapp.ru.galleryapp.data.PostResponse;
import bizapp.ru.galleryapp.data.source.PostDataSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
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
                .subscribe(new Observer<PostResponse>() {

                    Disposable mDisposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: " + d);
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(PostResponse postResponse) {
                        Log.i(TAG, "onNext: " + postResponse.getTotalResults());
                        callback.onPostsLoaded(postResponse.getPosts());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: " + e);
                        callback.onDataNotAvailable();
                        if (!mDisposable.isDisposed()) {
                            mDisposable.dispose();
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                        if (!mDisposable.isDisposed()) {
                            mDisposable.dispose();
                        }
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

    @Override
    public void refreshTasks() {
        // Not required because the {@link PostRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }
}
