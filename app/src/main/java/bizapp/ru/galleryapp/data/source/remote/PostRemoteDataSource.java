package bizapp.ru.galleryapp.data.source.remote;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import bizapp.ru.galleryapp.data.Post;
import bizapp.ru.galleryapp.data.source.PostDataSource;

/**
 * Implementation of the data source
 */

public class PostRemoteDataSource implements PostDataSource {

    private static PostRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private final static Map<String, Post> POST_SERVICE_DATA;

    static {
        POST_SERVICE_DATA = new LinkedHashMap<>(2);
        addPost("First post title", "Description for first post");
        addPost("Second post title", "Description for Second post");
    }

    public static PostRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PostRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private PostRemoteDataSource() {}

    private static void addPost(String title, String description) {
        Post newPost = new Post(title, description);
        POST_SERVICE_DATA.put(newPost.getId(), newPost);
    }

    /**
     * Note: {@link LoadPostsCallback#onDataNotAvailable()} is never fired.
     * In a real remote data source implementation, this would be fired if the
     * server can't be contracted or the server returns an error.
     * @param callback
     */
    @Override
    public void getPosts(@NonNull final LoadPostsCallback callback) {
        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onPostsLoaded(new ArrayList<>(POST_SERVICE_DATA.values()));
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void savePost(@NonNull Post post) {
        POST_SERVICE_DATA.put(post.getId(), post);
    }

    @Override
    public void deleteAllPosts() {
        POST_SERVICE_DATA.clear();
    }
}
