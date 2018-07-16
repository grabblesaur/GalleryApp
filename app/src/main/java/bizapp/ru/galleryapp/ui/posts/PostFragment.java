package bizapp.ru.galleryapp.ui.posts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader.PreloadSizeProvider;
import com.bumptech.glide.ListPreloader.PreloadModelProvider;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bizapp.ru.galleryapp.R;
import bizapp.ru.galleryapp.data.Post;
import bizapp.ru.galleryapp.ui.posts.adapter.PostAdapter;
import bizapp.ru.galleryapp.utils.GlideApp;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android on 06.07.2018.
 */

public class PostFragment extends Fragment
        implements PostContract.View {

    private static final String TAG = PostFragment.class.getName();

    private PostContract.Presenter mPresenter;
    private PostAdapter mPostAdapter;
    private PostPreloadModelProvider mModelProvider;

    @BindView(R.id.fm_swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fm_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.fm_tv_empty_list)
    TextView mEmptyListTextView;
    @BindView(R.id.fm_progress_bar_layout)
    RelativeLayout mProgressBarLayout;

    public PostFragment() {}

    public static PostFragment newInstance() {
        return new PostFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mPostAdapter = new PostAdapter(this,
                new ArrayList<Post>(0),
                new PostAdapter.PostAdapterListener() {
                    @Override
                    public void onItemClick(Post post) {
                        Toast.makeText(getActivity(), "on item clicked", Toast.LENGTH_SHORT).show();
                    }
                });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mPostAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadPosts(true);
            }
        });

        PreloadSizeProvider sizeProvider = new ViewPreloadSizeProvider();
        mModelProvider = new PostPreloadModelProvider(new ArrayList<Post>(0));
        RecyclerViewPreloader<Post> preloader =
                new RecyclerViewPreloader<Post>(Glide.with(this),
                        mModelProvider,
                        sizeProvider,
                        3);
        mRecyclerView.addOnScrollListener(preloader);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull PostContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mProgressBarLayout.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showPosts(List<Post> postList) {
        mModelProvider.replaceData(postList);
        mPostAdapter.replaceData(postList);

        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyListTextView.setVisibility(View.GONE);

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showPostsEmpty() {
        mEmptyListTextView.setText("POST LIST EMPTY");
        mEmptyListTextView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);

        mSwipeRefreshLayout.setRefreshing(false);
    }

    private class PostPreloadModelProvider implements PreloadModelProvider {

        private List<Post> postList;

        private PostPreloadModelProvider(List<Post> postList) {
            this.postList = postList;
        }

        @NonNull
        @Override
        public List getPreloadItems(int position) {
            String url = postList.get(position).getUrlToImage();
            if (TextUtils.isEmpty(url)) {
                return Collections.emptyList();
            }
            return Collections.singletonList(url);
        }

        @Nullable
        @Override
        public RequestBuilder getPreloadRequestBuilder(@NonNull Object item) {
            return GlideApp.with(PostFragment.this)
                    .load(item);
        }

        private void replaceData(List<Post> postList) {
            this.postList = postList;
        }
    }
}
