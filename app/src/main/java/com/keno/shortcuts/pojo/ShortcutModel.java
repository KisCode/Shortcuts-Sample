package com.keno.shortcuts.pojo;

import android.content.Context;

import androidx.annotation.DrawableRes;


public class ShortcutModel {
    private String id;

    private String title;
    private @DrawableRes
    int iconRes;
    private boolean isAdd;

    public ShortcutModel(String title, int iconRes, boolean isAdd) {
        this.title = title;
        this.iconRes = iconRes;
        this.isAdd = isAdd;
    }

    public String getId() {
        return id;
    }

    public ShortcutModel(String id, String title, int iconRes, boolean isAdd) {
        this.id = id;
        this.title = title;
        this.iconRes = iconRes;
        this.isAdd = isAdd;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }


}