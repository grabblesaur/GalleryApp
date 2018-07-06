package bizapp.ru.galleryapp.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import bizapp.ru.galleryapp.R;
import bizapp.ru.galleryapp.data.Post;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android on 06.07.2018.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    public interface PostAdapterListener {
        void onItemClick(Post post);
    }

    void setListener(PostAdapterListener listener) {
        mListener = listener;
    }

    private PostAdapterListener mListener;
    private List<Post> mPostList;

    public PostAdapter(List<Post> postList) {
        mPostList = postList;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.onBind(mPostList.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_post_layout)
        LinearLayout mLayout;
        @BindView(R.id.item_post_title)
        TextView mTitle;
        @BindView(R.id.item_post_description)
        TextView mDescription;

        PostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void onBind(final Post post) {
            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(post);
                    }
                }
            });
            mTitle.setText(post.getTitle());
            mDescription.setText(post.getDescription());
        }
    }
}
