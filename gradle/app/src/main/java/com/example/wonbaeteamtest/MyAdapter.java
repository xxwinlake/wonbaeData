package com.example.wonbaeteamtest;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter  {
    private Context ctx;
    private ArrayList<ShelterData> data;//원본



    public MyAdapter(Context ctx,ArrayList<ShelterData> data){
        this.ctx=ctx;
        this.data=data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view ==null){
            LayoutInflater inflater = LayoutInflater.from(ctx);
            view = inflater.inflate(R.layout.shelter_list,viewGroup,false);
        }
        ImageView image = (ImageView)view.findViewById(R.id.image);
        image.setImageResource(data.get(i).img);
        TextView text1 = (TextView)view.findViewById(R.id.nameText);
        text1.setText(data.get(i).name);
        TextView text2 = (TextView)view.findViewById(R.id.providerText);
        text2.setText(data.get(i).provider);

        return view;
    }
}
