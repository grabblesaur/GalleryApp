package bizapp.ru.galleryapp.data.source;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bizapp.ru.galleryapp.data.Post;

/**
 * Concrete implementation to load posts from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */

public class PostRepository implements PostDataSource {

    private static PostRepository INSTANCE = null;

    private final PostDataSource mPostRemoteDataSource;

    private final PostDataSource mPostLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Post> mCachedPosts;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested.
     * This variable has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

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
    public void getPosts(@NonNull final LoadPostsCallback callback) {

        //Respond immediately with cache if available and not dirty
        if (mCachedPosts != null && !mCacheIsDirty) {
            callback.onPostsLoaded(new ArrayList<>(mCachedPosts.values()));
            return;
        }

        if (mCacheIsDirty) {
            // if the cache is dirty we need to fetch new data from the network.
            getPostsFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mPostRemoteDataSource.getPosts(new LoadPostsCallback() {
                @Override
                public void onPostsLoaded(List<Post> posts) {
                    refreshCache(posts);
                    callback.onPostsLoaded(new ArrayList<>(mCachedPosts.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getPostsFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void savePost(@NonNull Post post) {
        mPostRemoteDataSource.savePost(post);
        mPostLocalDataSource.savePost(post);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedPosts == null) {
            mCachedPosts = new LinkedHashMap<>();
        }
        mCachedPosts.put(post.getId(), post);
    }

    @Override
    public void deleteAllPosts() {
        mPostRemoteDataSource.deleteAllPosts();
        mPostLocalDataSource.deleteAllPosts();

        if (mCachedPosts == null) {
            mCachedPosts = new LinkedHashMap<>();
        }
        mCachedPosts.clear();
    }

    private void getPostsFromRemoteDataSource(@NonNull final LoadPostsCallback callback) {
        mPostRemoteDataSource.getPosts(new LoadPostsCallback() {
            @Override
            public void onPostsLoaded(List<Post> posts) {
                refreshCache(posts);
                refreshLocalDataSource(posts);
                callback.onPostsLoaded(new ArrayList<>(mCachedPosts.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Post> posts) {
        if (mCachedPosts == null) {
            mCachedPosts = new LinkedHashMap<>();
        }
        mCachedPosts.clear();
        for (Post post : posts) {
            mCachedPosts.put(post.getId(), post);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Post> posts) {
        mPostLocalDataSource.deleteAllPosts();
        for (Post post : posts) {
            mPostLocalDataSource.savePost(post);
        }
    }


}