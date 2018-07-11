package bizapp.ru.galleryapp.posts.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import bizapp.ru.galleryapp.R;
import bizapp.ru.galleryapp.data.Post;
import bizapp.ru.galleryapp.utils.GlideApp;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android on 06.07.2018.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private static final String TAG = PostAdapter.class.getName();
    private Context mContext;

    public interface PostAdapterListener {
        void onItemClick(Post post);
    }

    private PostAdapterListener mListener;
    private List<Post> mPostList;

    public PostAdapter(Context context, List<Post> postList, PostAdapterListener listener) {
        mContext = context;
        mPostList = postList;
        mListener = listener;
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

    public void replaceData(List<Post> postList) {
        if (postList == null) {
            throw new NullPointerException();
        }
        mPostList = postList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_post_layout)
        LinearLayout mLayout;
        @BindView(R.id.item_post_image)
        ImageView mImage;
        @BindView(R.id.item_post_title)
        TextView mTitleTextView;
        @BindView(R.id.item_post_description)
        TextView mDescriptionTextView;
        @BindView(R.id.item_post_btn_source)
        Button mSourceButton;

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

            if (post.getUrlToImage() != null && !post.getUrlToImage().isEmpty()) {
                GlideApp.with(mContext)
                        .load(post.getUrlToImage())
                        .centerCrop()
                        .into(mImage);
            }

            mTitleTextView.setText(post.getTitle());
            mDescriptionTextView.setText(post.getDescription());
            if (post.getSource() != null &&
                    post.getSource().getName() != null &&
                    !post.getSource().getName().isEmpty()) {
                mSourceButton.setText(String.format("Источник: %s", post.getSource().getName()));
                if (post.getUrl() != null && !post.getUrl().isEmpty()) {
                    mSourceButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(post.getUrl()));
                            mContext.startActivity(intent);
                        }
                    });
                }
            }
        }
    }
}
