package bizapp.ru.galleryapp.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import bizapp.ru.galleryapp.data.Post;
import bizapp.ru.galleryapp.data.source.PostDataSource;

/**
 * Concrete implementation of a data source as a db.
 */

public class PostLocalDataSource implements PostDataSource {

    private static PostLocalDataSource INSTANCE;
    private PostDbHelper mDbHelper;

    private PostLocalDataSource(Context context) {
        mDbHelper = new PostDbHelper(context);
    }

    public static PostDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PostLocalDataSource(context);
        }
        return INSTANCE;
    }

    /**
     * Note: {@link LoadPostsCallback#onDataNotAvailable()} is fired if the database
     * doesn't exist or the table is empty
     * @param category
     * @param callback
     */
    @Override
    public void getPosts(String category, LoadPostsCallback callback) {
        List<Post> posts = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection;
        projection = new String[]{
                PostsPersistenceContract.PostsEntry.COLUMN_NAME_TITLE,
                PostsPersistenceContract.PostsEntry.COLUMN_NAME_DESCRIPTION,
                PostsPersistenceContract.PostsEntry.COLUMN_NAME_SOURCE,
        };

        Cursor c = db
                .query(getTableName(category),
                        projection, null, null, null, null, null);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String title = c.getString(c.getColumnIndexOrThrow(PostsPersistenceContract.PostsEntry.COLUMN_NAME_TITLE));
                String description = c.getString(c.getColumnIndexOrThrow(PostsPersistenceContract.PostsEntry.COLUMN_NAME_DESCRIPTION));
                String source = c.getString(c.getColumnIndexOrThrow(PostsPersistenceContract.PostsEntry.COLUMN_NAME_SOURCE));
                Post post = new Post(title, description, source);
                posts.add(post);
            }
        }
        if (c != null) {
            c.close();
        }
        db.close();
        if (posts.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onPostsLoaded(posts);
        }
    }

    /**
     * helper method to determine name of the table by category
     * @param category
     * @return
     */
    private String getTableName(String category) {
        String result;
        switch (category) {
            case "business" : result = PostsPersistenceContract.PostsEntry.TABLE_NAME_BUSINESS; break;
            case "sport" : result = PostsPersistenceContract.PostsEntry.TABLE_NAME_SPORT; break;
            case "science" : result = PostsPersistenceContract.PostsEntry.TABLE_NAME_SCIENCE; break;
            case "entertainment" : result = PostsPersistenceContract.PostsEntry.TABLE_NAME_ENTERTAINMENT; break;
            default: result = PostsPersistenceContract.PostsEntry.TABLE_NAME_BUSINESS; break;
        }
        return result;
    }

    @Override
    public void savePost(String category, Post post) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PostsPersistenceContract.PostsEntry.COLUMN_NAME_TITLE, post.getTitle());
        values.put(PostsPersistenceContract.PostsEntry.COLUMN_NAME_DESCRIPTION, post.getDescription());
        values.put(PostsPersistenceContract.PostsEntry.COLUMN_NAME_SOURCE, post.getSource().getName());
        db.insert(getTableName(category), null, values);
        db.close();
    }

    @Override
    public void deleteAllPosts() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(PostsPersistenceContract.PostsEntry.TABLE_NAME_BUSINESS, null, null);
        db.delete(PostsPersistenceContract.PostsEntry.TABLE_NAME_SPORT, null, null);
        db.delete(PostsPersistenceContract.PostsEntry.TABLE_NAME_SCIENCE, null, null);
        db.delete(PostsPersistenceContract.PostsEntry.TABLE_NAME_ENTERTAINMENT, null, null);
        db.close();
    }

    @Override
    public void refreshTasks() {
        // Not required because the {@link PostRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }
}
