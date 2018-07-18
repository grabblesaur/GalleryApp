package bizapp.ru.galleryapp;

import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import bizapp.ru.galleryapp.data.Post;
import bizapp.ru.galleryapp.data.source.PostDataSource;
import bizapp.ru.galleryapp.data.source.PostRepository;
import bizapp.ru.galleryapp.ui.posts.PostContract;
import bizapp.ru.galleryapp.ui.posts.PostPresenter;

import static org.mockito.Mockito.when;

/**
 * Unit test for the implementation of {@link bizapp.ru.galleryapp.ui.posts.PostPresenter}
 */
public class PostPresenterTest {

    private static List<Post> POSTS;

    @Mock
    private PostRepository mPostRepository;

    @Mock
    private PostContract.View mPostView;

    /**
     * {@link ArgumentCaptor} is a powerful mockito API to capture argument values
     * and use them to perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<PostDataSource.LoadPostsCallback> mLoadPostsCallback;

    private PostPresenter mPostPresenter;

    @Before
    public void setupPostPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock
        // annotation. To inject the mocks in the test the initMocks method
        // needs to be called
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mPostPresenter = new PostPresenter(mPostRepository,
                mPostView,
                "business");

        // The presenter won't update the view unless it's active.
        when(mPostView.isActive()).thenReturn(true);

        // We start the post to 3
        POSTS = new ArrayList<>(3);
        POSTS.add(new Post("Title1", "Description1", "Source1"));
        POSTS.add(new Post("Title2", "Description2", "Source2"));
        POSTS.add(new Post("Title3", "Description3", "Source3"));
    }
}
