package bizapp.ru.galleryapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 10.07.2018.
 */

public class PostResponse {

    @SerializedName("status")
    private String mStatus;
    @SerializedName("totalResults")
    private int mTotalResults;
    @SerializedName("articles")
    private List<Post> mPosts;

    public String getStatus() {
        return mStatus;
    }

    public int getTotalResults() {
        return mTotalResults;
    }

    public List<Post> getPosts() {
        return mPosts;
    }
}
