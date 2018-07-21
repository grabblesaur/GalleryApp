package bizapp.ru.galleryapp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.util.ArrayList;
import java.util.List;

import bizapp.ru.galleryapp.data.Post;
import bizapp.ru.galleryapp.data.source.PostDataSource;
import bizapp.ru.galleryapp.data.source.PostRepository;
import bizapp.ru.galleryapp.ui.posts.PostContract;
import bizapp.ru.galleryapp.ui.posts.PostPresenter;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test for the implementation of {@link bizapp.ru.galleryapp.ui.posts.PostPresenter}
 */
public class PostPresenterTest {

    private static List<Post> POSTS;
    private static List<Post> EMPTY_POSTS;

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

        EMPTY_POSTS = new ArrayList<>(0);
    }

    @Test
    public void loadAllPostsFromRepositoryAndLoadIntoView() {
        // Given an initializaed PostPresenter with initialized posts
        // when loading of posts is requested
        mPostPresenter.loadPosts(false);

        // Callback is captured and invoked with stubbed posts
        verify(mPostRepository).getPosts(eq("business"), mLoadPostsCallback.capture());
        mLoadPostsCallback.getValue().onPostsLoaded(POSTS);

        // Then progress indicator is shown
        InOrder inOrder = Mockito.inOrder(mPostView);
        inOrder.verify(mPostView).setLoadingIndicator(true);
        // Then progress indicator is hidden and all posts are shown in UI
        inOrder.verify(mPostView).setLoadingIndicator(false);
        ArgumentCaptor<List> showPostsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mPostView).showPosts(showPostsArgumentCaptor.capture());
        assertTrue(showPostsArgumentCaptor.getValue().size() == 3);
    }

    @Test
    public void loadAllPostsFromRepositoryOnDataNotAvailable() {
        mPostPresenter.loadPosts(false);
        verify(mPostRepository).getPosts(eq("business"), mLoadPostsCallback.capture());
        mLoadPostsCallback.getValue().onDataNotAvailable();

        InOrder inOrder = Mockito.inOrder(mPostView);
        inOrder.verify(mPostView).setLoadingIndicator(true);
        inOrder.verify(mPostView).setLoadingIndicator(false);

        verify(mPostView).showPostsEmpty();
    }

    @Test
    public void loadAllPostsFromRepositoryOnEmptyCase() {
        mPostPresenter.loadPosts(false);
        verify(mPostRepository).getPosts(eq("business"), mLoadPostsCallback.capture());
        mLoadPostsCallback.getValue().onPostsLoaded(EMPTY_POSTS);

        InOrder inOrder = Mockito.inOrder(mPostView);
        inOrder.verify(mPostView).setLoadingIndicator(true);
        inOrder.verify(mPostView).setLoadingIndicator(false);

        verify(mPostView).showPostsEmpty();
    }

    @Test
    public void loadPostsWithForceUpdate() {
        mPostPresenter.loadPosts(true);
        verify(mPostRepository).refreshTasks();
    }
}
