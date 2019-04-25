package app.com.thecentaurusmall.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.Comparator;

import app.com.thecentaurusmall.databinding.CategoryItemBinding;
import app.com.thecentaurusmall.model.Category;

public class CategoryAdapter extends SortedListAdapter<Category> {

    public interface Listener {
        void onCategoryClicked(Category categoryModel);
    }

    private final Listener mCategoryListener;
    private Context mContext;

    public CategoryAdapter(@NonNull Context context,
                           @NonNull Comparator<Category> comparator, Listener listener) {
        super(context, Category.class ,comparator);
        mContext = context;
        mCategoryListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder<? extends Category> onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        final CategoryItemBinding binding = CategoryItemBinding.inflate(inflater, parent, false);
        return new CategoryViewHolder(mContext,binding, mCategoryListener);
    }
}
