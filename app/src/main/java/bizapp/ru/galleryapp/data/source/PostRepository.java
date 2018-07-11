package bizapp.ru.galleryapp.data.source;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import bizapp.ru.galleryapp.data.Post;

/**
 * Concrete implementation to load posts from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */

public class PostRepository implements PostDataSource {

    private static final String TAG = PostRepository.class.getSimpleName();

    private static PostRepository INSTANCE = null;

    private final PostDataSource mPostRemoteDataSource;

    private final PostDataSource mPostLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    LinkedHashMap<String, List<Post>> mCachedPosts;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested.
     * This variable has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;
    private String mCategory = "business";

    // Prevent direct instantiation
    private PostRepository(@NonNull PostDataSource postRemoteDataSource,
                           @NonNull PostDataSource postLocalDataSource) {
        mPostRemoteDataSource = postRemoteDataSource;
        mPostLocalDataSource = postLocalDataSource;
    }

    /**
     * Returns the single instance of this class, creating if it necessary
     *
     * @param postRemoteDataSource
     * @param postLocalDataSource
     * @return the {@link PostRepository} instance
     */
    public static PostRepository getInstance(PostDataSource postRemoteDataSource,
                                             PostDataSource postLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new PostRepository(postRemoteDataSource, postLocalDataSource);
        }
        return INSTANCE;
    }


    /**
     * Used to force {@link #getInstance(PostDataSource, PostDataSource)}
     * to create a new instance next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Gets posts from cache, local data source (SQLite) or remote data source,
     * whichever is available first.
     *
     * Note: {@link LoadPostsCallback#onDataNotAvailable()} is fired if all data
     * sources fail to get the data.
     *
     * @param callback
     */
    @Override
    public void getPosts(final String category, @NonNull final LoadPostsCallback callback) {

        Log.i(TAG, "getPosts: " + category);

        //Respond immediately with cache if available and not dirty
        if (mCachedPosts != null && mCachedPosts.containsKey(category) && !mCacheIsDirty) {
            callback.onPostsLoaded(new ArrayList<>(mCachedPosts.get(category)));
            return;
        }

        if (mCacheIsDirty) {
            // if the cache is dirty we need to fetch new data from the network.
            getPostsFromRemoteDataSource(category, callback);
        } else {
            // TODO: 11.07.2018 replace remote with local
            // Query the local storage if available. If not, query the network.
            mPostRemoteDataSource.getPosts(category, new LoadPostsCallback() {
                @Override
                public void onPostsLoaded(List<Post> posts) {
                    refreshCache(category, posts);
                    callback.onPostsLoaded(new ArrayList<>(mCachedPosts.get(category)));
                }

                @Override
                public void onDataNotAvailable() {
                    getPostsFromRemoteDataSource(category, callback);
                }
            });
        }
    }

    /**
     * pass action to localDataSource to save Post
     * @param post
     */
    @Override
    public void savePost(Post post) {

    }

    /**
     * pass action to localDataStorage to remove Posts
     */
    @Override
    public void deleteAllPosts() {

    }

    private void getPostsFromRemoteDataSource(final String category, @NonNull final LoadPostsCallback callback) {
        mPostRemoteDataSource.getPosts(category, new LoadPostsCallback() {
            @Override
            public void onPostsLoaded(List<Post> posts) {
                refreshCache(category, posts);
                callback.onPostsLoaded(new ArrayList<>(mCachedPosts.get(category)));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(String category, List<Post> posts) {
        if (mCachedPosts == null) {
            mCachedPosts = new LinkedHashMap<>();
        }
        mCachedPosts.clear();
        mCachedPosts.put(category, posts);
        mCacheIsDirty = false;
    }
}
