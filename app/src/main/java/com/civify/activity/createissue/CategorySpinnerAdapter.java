package com.civify.activity.createissue;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.civify.R;
import com.civify.model.issue.Category;

public class CategorySpinnerAdapter extends ArrayAdapter<CategoryItem> {
    private CategoryItem[] mItems;

    public CategorySpinnerAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        prepareItems();
    }

    private void prepareItems() {
        Category[] categories = Category.values();
        mItems = new CategoryItem[categories.length + 1];
        for (int i = 0; i < categories.length; ++i) {
            mItems[i] = new CategoryItem(categories[i].getName(), categories[i].getIcon());
        }
        mItems[categories.length] = new CategoryItem(R.string.select_category, -1);

        addAll(mItems);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
            @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, @Nullable View convertView,
            @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View spinnerItem = inflater.inflate(R.layout.text_and_icon_spinner_tem, parent, false);

        TextView categoryName = (TextView) spinnerItem.findViewById(R.id.category_text);
        ImageView categoryIcon = (ImageView) spinnerItem.findViewById(R.id.category_icon);

        categoryName.setText(mItems[position].getName());
        if (position == getCount()) {
            categoryName.setTextColor(getContext().getResources().getColor(R.color.grey));
        } else {
            categoryIcon.setImageResource(mItems[position].getIcon());
        }

        return spinnerItem;
    }

    @Override
    public int getCount() {
        return super.getCount() - 1;
    }
}
