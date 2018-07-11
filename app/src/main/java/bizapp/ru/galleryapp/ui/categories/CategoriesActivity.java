package bizapp.ru.galleryapp.ui.categories;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import bizapp.ru.galleryapp.R;
import bizapp.ru.galleryapp.ui.posts.PostActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android on 11.07.2018.
 */

public class CategoriesActivity extends AppCompatActivity {

    @BindView(R.id.ac_recyclerview)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ButterKnife.bind(this);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        List<String> categories = new ArrayList<>();
        categories.add("business");
        categories.add("sport");
        categories.add("science");
        categories.add("entertainment");

        CategoriesAdapter adapter = new CategoriesAdapter(categories, new CategoriesAdapter.CategoriesAdapterListener() {
            @Override
            public void onItemClick(String category) {
                Intent intent = new Intent(CategoriesActivity.this, PostActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }
}
