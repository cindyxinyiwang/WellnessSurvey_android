package com.example.cindywang.wellnesssurvey.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cindywang.wellnesssurvey.R;

import java.util.ArrayList;

/**
 * Created by cindywang on 11/19/14.
 */
public class listViewBaseAdapter extends BaseAdapter {

    private static ArrayList<questionListItem> questionListItems;

    private LayoutInflater mInflater;

    public listViewBaseAdapter(Context context, ArrayList<questionListItem> listItems){
        questionListItems = listItems;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return questionListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return questionListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //improve performance of listview, make use of scrap view and reduce call to getViewById
        ViewHolder holder;

        if (convertView == null){
            //inflate layout
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.type = (TextView) convertView.findViewById(R.id.questionType);
            holder.checkMark = (ImageView) convertView.findViewById(R.id.checkMarkIMG);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        questionListItem currentItem = questionListItems.get(position);
        holder.type.setText(currentItem.getqType());
        if (currentItem.getClickable()){
            holder.checkMark.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            holder.checkMark.setImageResource(android.R.drawable.btn_star_big_off);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView type;
        ImageView checkMark;
    }
}
