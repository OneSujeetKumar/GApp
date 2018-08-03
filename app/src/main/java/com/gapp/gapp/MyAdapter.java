package com.gapp.gapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

public class MyAdapter extends ExpandableRecyclerViewAdapter {

    private Context context;

    public MyAdapter(List<? extends ExpandableGroup> groups, Context context) {
        super(groups);
        this.context = context;
    }

    @Override
    public GroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        return new ParentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        return new ChildHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_ex, parent, false));
    }

    @Override
    public void onBindGroupViewHolder(GroupViewHolder holder, int flatPosition, ExpandableGroup group) {
        ParentHolder parentHolder = (ParentHolder) holder;
        parentHolder.name.setText(group.getTitle());
        Picasso.with(context).load(((ParentI)group).getImg()).into(parentHolder.img);
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        ChildHolder childHolder = (ChildHolder) holder;
        ChildI childI = (ChildI) group.getItems().get(childIndex);
        childHolder.country.setText(childI.getCountry());
        childHolder.categories.setText(childI.getCategories());
    }

    class ParentHolder extends GroupViewHolder {

        TextView name;
        ImageView img;

        ParentHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.li_name);
            img = itemView.findViewById(R.id.li_img);
        }
    }

    class ChildHolder extends ChildViewHolder {

        TextView country, categories;

        ChildHolder(View itemView) {
            super(itemView);
            country = itemView.findViewById(R.id.lie_country);
            categories = itemView.findViewById(R.id.lie_categories);
        }
    }

}
