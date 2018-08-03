package com.gapp.gapp;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;

public class ParentI extends ExpandableGroup{

    private String img;

    public ParentI(String title, String img, ArrayList<ChildI> items) {
        super(title, items);
        this.img = img;
    }

    public String getImg() {
        return img;
    }
}
