package bizapp.ru.galleryapp.data.source.local;

import android.provider.BaseColumns;

/**
 * The contract used for the db to save the tasks locally.
 */

public final class PostsPersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it am empty constructor
    private PostsPersistenceContract() {}

    // Inner class that defines the table contents
    public static abstract class PostsEntry implements BaseColumns {
        public static final String TABLE_NAME = "post";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_SOURCE = "source";
    }

}
