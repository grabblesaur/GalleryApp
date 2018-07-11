package bizapp.ru.galleryapp.ui.categories;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import bizapp.ru.galleryapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android on 11.07.2018.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    public interface CategoriesAdapterListener {
        void onItemClick(String category);
    }

    private List<String> mCategories;
    private CategoriesAdapterListener mListener;

    public CategoriesAdapter(List<String> categories, CategoriesAdapterListener listener) {
        mCategories = categories;
        mListener = listener;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        holder.onBind(mCategories.get(position));
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    class CategoriesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_category_btn)
        Button mCategoryButton;

        CategoriesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void onBind(final String category) {
            mCategoryButton.setText(category);
            mCategoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(category);
                    }
                }
            });
        }
    }
}
