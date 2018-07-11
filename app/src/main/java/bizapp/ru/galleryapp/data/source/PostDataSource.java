package bizapp.ru.galleryapp.data.source;

import java.util.List;

import bizapp.ru.galleryapp.data.Post;

/**
 * Main entry point for accessing tasks data.
 * <p>
 * For simplicity, only getPosts() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when a new post is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 */

public interface PostDataSource {

    interface LoadPostsCallback {
        void onPostsLoaded(List<Post> posts);
        void onDataNotAvailable();
    }

    void getPosts(String category, LoadPostsCallback callback);
}
