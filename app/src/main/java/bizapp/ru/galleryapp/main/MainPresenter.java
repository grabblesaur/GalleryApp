package bizapp.ru.galleryapp.main;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import bizapp.ru.galleryapp.data.Post;
import bizapp.ru.galleryapp.data.source.PostDataSource;
import bizapp.ru.galleryapp.data.source.PostRepository;

/**
 * Listens to user actions from the UI ({@link MainFragment}), retrieves the data
 * and updates the UI when required.
 */

public class MainPresenter implements MainContract.Presenter {

    private static final String TAG = MainPresenter.class.getName();

    private final PostRepository mPostRepository;
    private final MainContract.View mMainView;

    private boolean mFirstLoad = true;

    public MainPresenter(@NonNull PostRepository postRepository,
                         @NonNull MainContract.View view) {
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
        mPostRepository.getPosts(new PostDataSource.LoadPostsCallback() {
            @Override
            public void onPostsLoaded(List<Post> posts) {
                mMainView.showPosts(posts);
            }

            @Override
            public void onDataNotAvailable() {
                Log.i(TAG, "onDataNotAvailable: ");
            }
        });
    }
}
