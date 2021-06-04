package com.example.wonbaeteamtest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class HomeLandFragment extends Fragment {


    public HomeLandFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rv = inflater.inflate(R.layout.fragment_home_land, container, false);
        ImageView news = rv.findViewById(R.id.disasternews);
        ImageView weather = rv.findViewById(R.id.weatherbt);
        ImageView behavior = rv.findViewById(R.id.behavioralbt);

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://rscanner.ndmi.go.kr/scanning/"));
                startActivity(intent);
            }
        });

        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.weather.go.kr/w/index.do"));
                startActivity(intent);
            }
        });

        behavior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.safekorea.go.kr/idsiSFK/neo/sfk/cs/contents/prevent/prevent01.html?menuSeq=126"));
                startActivity(intent);
            }
        });

        return rv;
    }
}