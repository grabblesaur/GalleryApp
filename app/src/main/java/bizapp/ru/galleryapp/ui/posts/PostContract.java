package bizapp.ru.galleryapp.ui.posts;

import java.util.List;

import bizapp.ru.galleryapp.BasePresenter;
import bizapp.ru.galleryapp.BaseView;
import bizapp.ru.galleryapp.data.Post;

/**
 * This specifies the contract between the view and the presenter.
 */

public interface PostContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showPosts(List<Post> postList);

        void showPostsEmpty();

        boolean isActive();

    }

    interface Presenter extends BasePresenter {

        void loadPosts(boolean forceUpdate);
    }

}
