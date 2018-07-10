package bizapp.ru.galleryapp.posts;

import android.support.annotation.NonNull;

import java.util.List;

import bizapp.ru.galleryapp.data.Post;
import bizapp.ru.galleryapp.data.source.PostDataSource;
import bizapp.ru.galleryapp.data.source.PostRepository;

/**
 * Listens to user actions from the UI ({@link PostFragment}), retrieves the data
 * and updates the UI when required.
 */

public class PostPresenter implements PostContract.Presenter {

    private static final String TAG = PostPresenter.class.getSimpleName();

    private final PostRepository mPostRepository;
    private final PostContract.View mMainView;

    private boolean mFirstLoad = true;

    public PostPresenter(@NonNull PostRepository postRepository,
                         @NonNull PostContract.View view) {
        mPostRepository = postRepository;
        mMainView = view;
        mMainView.setPresenter(this);
    }

    @Override
    public void start() {
        loadPosts(false);
    }

    @Override
    public void loadPosts(boolean forceUpdate) {
        mMainView.setLoadingIndicator(true);
        mPostRepository.getPosts(new PostDataSource.LoadPostsCallback() {
            @Override
            public void onPostsLoaded(List<Post> posts) {
                if (posts.isEmpty()) {
                    mMainView.showPostsEmpty();
                } else {
                    mMainView.showPosts(posts);
                    mMainView.setLoadingIndicator(false);
                }
            }

            @Override
            public void onDataNotAvailable() {
                mMainView.setLoadingIndicator(false);
                mMainView.showPostsEmpty();
            }
        });
    }
}
