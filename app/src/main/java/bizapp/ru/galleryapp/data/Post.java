package bizapp.ru.galleryapp.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 05.07.2018.
 */

public final class Post {

    @SerializedName("source")
    private Source mSource;
    @SerializedName("author")
    private String mAuthor;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("url")
    private String mUrl;
    @SerializedName("urlToImage")
    private String mUrlToImage;
    @SerializedName("publishedAt")
    private String mPublishedAt;

    public Post(String title, String description, String sourceName) {
        mTitle = title;
        mDescription = description;
        mSource = new Source(null, sourceName);
    }

    public Source getSource() {
        return mSource;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getUrlToImage() {
        return mUrlToImage;
    }

    public String getPublishedAt() {
        return mPublishedAt;
    }

    public class Source {
        @SerializedName("id")
        private String mId;
        @SerializedName("name")
        private String mName;

        public Source(String id, String name) {
            mId = id;
            mName = name;
        }

        public String getId() {
            return mId;
        }

        public String getName() {
            return mName;
        }
    }
}
