package com.example.wonbaeteamtest;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

public class ShelterData {
    public byte[] byteArray;//이미지
    public int _id;
    public int subject;//주제
    public String address;//대피소 주소
    public String name;//대피소 이름
    public String provider;//정보제공자
    public String audio;
    public String video;

    public ShelterData(int _id, byte[] byteArray, int subject, String name, String address,String provider,String audio,String video){
        this._id=_id;
        this.byteArray=byteArray;
        this.subject=subject;
        this.name=name;
        this.address=address;
        this.provider=provider;
        this.audio=audio;
        this.video=video;
    }
}
