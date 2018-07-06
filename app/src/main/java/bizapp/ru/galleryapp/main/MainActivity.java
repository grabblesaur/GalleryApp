package bizapp.ru.galleryapp.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import bizapp.ru.galleryapp.R;
import bizapp.ru.galleryapp.data.source.PostRepository;
import bizapp.ru.galleryapp.data.source.local.PostLocalDataSource;
import bizapp.ru.galleryapp.data.source.remote.PostRemoteDataSource;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private MainPresenter mMainPresenter;

    @BindView(R.id.am_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MainFragment fragment = MainFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.am_fragment_container, fragment)
                .commit();

        // Create the presenter
        mMainPresenter = new MainPresenter(
                PostRepository.getInstance(
                        PostRemoteDataSource.getInstance(),
                        PostLocalDataSource.getInstance()),
                fragment
        );
    }
}
