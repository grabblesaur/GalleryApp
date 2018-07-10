package bizapp.ru.galleryapp.posts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import bizapp.ru.galleryapp.R;
import bizapp.ru.galleryapp.data.source.PostRepository;
import bizapp.ru.galleryapp.data.source.local.PostLocalDataSource;
import bizapp.ru.galleryapp.data.source.remote.PostRemoteDataSource;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PostActivity extends AppCompatActivity {

    private PostPresenter mMainPresenter;

    @BindView(R.id.am_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        PostFragment fragment = PostFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.am_fragment_container, fragment)
                .commit();

        // Create the presenter
        mMainPresenter = new PostPresenter(
                PostRepository.getInstance(
                        PostRemoteDataSource.getInstance(),
                        PostLocalDataSource.getInstance()),
                fragment
        );
    }
}
