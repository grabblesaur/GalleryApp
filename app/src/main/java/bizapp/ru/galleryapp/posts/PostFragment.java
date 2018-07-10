package bizapp.ru.galleryapp.posts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bizapp.ru.galleryapp.R;
import bizapp.ru.galleryapp.data.Post;
import bizapp.ru.galleryapp.posts.adapter.PostAdapter;
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

        mPostAdapter = new PostAdapter(new ArrayList<Post>(0));
        mPostAdapter.setListener(new PostAdapter.PostAdapterListener() {
            @Override
            public void onItemClick(Post post) {
                Toast.makeText(getActivity(), "on item clicked", Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mPostAdapter);
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
        mPostAdapter.replaceData(postList);
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyListTextView.setVisibility(View.GONE);
    }

    @Override
    public void showPostsEmpty() {
        mEmptyListTextView.setText("POST LIST EMPTY");
        mEmptyListTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }
}
