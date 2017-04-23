package com.civify.activity.createissue;

class CategoryItem {
    private int mName;
    private int mIcon;
    public CategoryItem(int name, int icon) {
        mName = name;
        mIcon = icon;
    }

    public int getName() {
        return mName;
    }

    public int getIcon() {
        return mIcon;
    }
}
