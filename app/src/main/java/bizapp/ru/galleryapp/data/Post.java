package bizapp.ru.galleryapp.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.UUID;

/**
 * Created by android on 05.07.2018.
 */

public final class Post {

    @NonNull
    private final String mId;

    @Nullable
    private final String mTitle;

    @Nullable
    private final String mDescription;


    public Post(@NonNull String id,
                @Nullable String title,
                @Nullable String description) {
        mId = id;
        mTitle = title;
        mDescription = description;
    }

    public Post(@Nullable String title,
                @Nullable String description) {
        this(UUID.randomUUID().toString(), title, description);
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }
}
