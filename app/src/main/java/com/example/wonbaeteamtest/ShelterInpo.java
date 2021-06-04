package com.example.wonbaeteamtest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.w3c.dom.Text;

public class ShelterInpo extends AppCompatActivity {
    Toolbar toolbar2;
    private TextView subjectText;
    private TextView nameText;
    private TextView addressText;
    private TextView providerText;
    private ImageView imageView;
    private byte[] byteArray;
    private Uri audio;
    private Button mplaybt;
    private int id;
    private int subjectPosition;
    private MediaPlayer mPlayer;
    private VideoView mvideoview;
    private Uri video;

    public void TextViewFVI() {//findViewById 4개 묶어버림
        subjectText = (TextView) findViewById(R.id.infoSubject);
        nameText = (TextView) findViewById(R.id.shtName);
        addressText = (TextView) findViewById(R.id.shtAdd);
        providerText = (TextView) findViewById(R.id.shtWho);
        imageView = (ImageView) findViewById(R.id.shtImg);
        mplaybt =(Button)findViewById(R.id.info_playbt);
        mvideoview=(VideoView)findViewById(R.id.video_view);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_inpo);
        TextViewFVI();
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        subjectPosition = intent.getIntExtra("subject", -1);

        mvideoview.setVideoURI(video);
        mvideoview.setOnPreparedListener(mp -> {
            mp.setLooping(false);
            mvideoview.seekTo(0);
            mvideoview.start();
        });

        switch (subjectPosition) {
            case 0:
                subjectText.setText("지진");
                break;
            case 1:
                subjectText.setText("해일");
                break;
            case 2:
                subjectText.setText("화산");
                break;
        }
        byteArray = intent.getByteArrayExtra("image");
        if (byteArray != null) {
            Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView.setImageBitmap(image);
        } else {
            imageView.setImageResource(R.drawable.defaultimg);
        }

        nameText.setText(intent.getStringExtra("name"));
        addressText.setText(intent.getStringExtra("address"));
        providerText.setText(intent.getStringExtra("provider"));
        //녹음인텐트
        if(intent.getStringExtra("audio")==""){
        }
        else {
            audio = Uri.parse(intent.getStringExtra("audio"));
        }
        //비디오인텐트
        if(intent.getStringExtra("video")==""){
        }
        else {
            video = Uri.parse(intent.getStringExtra("video"));
        }



        /*툴바생성*/
        toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        super.setSupportActionBar(toolbar2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //툴바에 뒤로가기 추가.
        getSupportActionBar().setDisplayShowTitleEnabled(false);//이름안보이게


        mplaybt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(audio==null){
                    Toast.makeText(getApplicationContext(),"녹음파일이 없습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mPlayer==null){
                    mPlayer=new MediaPlayer();
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mplaybt.setEnabled(true);
                        }
                    });
                }else {
                    mPlayer.reset();
                }
                try{
                    mPlayer.setDataSource(getApplicationContext(),audio);
                    mPlayer.prepare();
                    mPlayer.start();
                    mplaybt.setEnabled(false);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });




    }

    public void mOnClick(View view) {

        switch (view.getId()) {
            case R.id.btnEdit://편집 버튼을 누르면
                TextViewFVI();
                Intent intent = new Intent(this, ShelterEdit.class);
                MainActivity.putExtraInfo(intent, id, byteArray, subjectPosition, nameText.getText().toString(),
                        addressText.getText().toString(), providerText.getText().toString(), audio.toString(),video.toString());
                startActivityForResult(intent, 0);
                break;
            case R.id.btnDel://삭제 버튼을 누르면
                /*팝업 메시지 출력*/
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("메시지");
                //builder.setIcon()//아이콘
                builder.setMessage(Html.fromHtml("해당 대피소 정보를 삭제하시겠습니까?"));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent delFromInpo = new Intent();
                        delFromInpo.putExtra("id", id);
                        setResult(2, delFromInpo);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Intent toMain = new Intent();
            toMain.putExtra("id", id);
            MainActivity.putExtraInfo(toMain, id, data.getByteArrayExtra("image"), data.getIntExtra("subject", -1), data.getStringExtra("name"),
                    data.getStringExtra("address"), data.getStringExtra("provider"), data.getStringExtra("audio"),data.getStringExtra("video"));
            setResult(3, toMain);
            finish();
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch ((item.getItemId())) {
            //ids.xml에 있는 안드로이드 오픈소스 아이디값
            case android.R.id.home: {
                /*Intent intent = new Intent(ShelterInpo.this, MainActivity.class); //지금 액티비티에서 다른 액티비티로 이동하는 인텐트 설정
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    //인텐트 플래그 설정
                startActivity(intent);  //인텐트 이동
                finish();   //현재 액티비티 종료*/
                onBackPressed();
            }
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void onBackPressed() {
        //super.onBackPressed();
        Intent toMain = new Intent();
        setResult(4, toMain);
        finish();
    }
}