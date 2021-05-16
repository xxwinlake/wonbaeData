package com.example.wonbaeteamtest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ShelterInpo extends AppCompatActivity {
    Toolbar toolbar2;
    private TextView subjectText;
    private TextView nameText;
    private TextView addressText;
    private TextView providerText;
    private int position;
    private int subjectPosition;
    public void TextViewFVI(){//findViewById 4개 묶어버림
        subjectText=(TextView)findViewById(R.id.infoSubject);
        nameText=(TextView)findViewById(R.id.shtName);
        addressText=(TextView)findViewById(R.id.shtAdd);
        providerText=(TextView)findViewById(R.id.shtWho);
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
        position=intent.getIntExtra("position",-1);
        subjectPosition=intent.getIntExtra("subject",-1);
        switch (subjectPosition){
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

        nameText.setText(intent.getStringExtra("name"));
        addressText.setText(intent.getStringExtra("address"));
        providerText.setText(intent.getStringExtra("provider"));

        /*툴바생성*/
        toolbar2 = (Toolbar)findViewById(R.id.toolbar);
        super.setSupportActionBar(toolbar2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //툴바에 뒤로가기 추가.
        getSupportActionBar().setDisplayShowTitleEnabled(false);//이름안보이게

    }
    public void mOnClick(View view){

        switch (view.getId()){
            case R.id.btnEdit://편집 버튼을 누르면
                TextViewFVI();
                Intent intent = new Intent(this,ShelterEdit.class);
                MainActivity.putExtraInfo(intent,subjectPosition,nameText.getText().toString(),
                        addressText.getText().toString(),providerText.getText().toString());
                startActivityForResult(intent,0);
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
                        delFromInpo.putExtra("position",position);
                        setResult(2,delFromInpo);
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
        if (requestCode==0&&resultCode==RESULT_OK){
            Intent toMain = new Intent();
            toMain.putExtra("position",position);
            MainActivity.putExtraInfo(toMain,data.getIntExtra("subject",-1),data.getStringExtra("name"),
                    data.getStringExtra("address"),data.getStringExtra("provider"));
            setResult(3,toMain);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch ((item.getItemId())){
            case android.R.id.home:{  //뒤로가기 고유 id 값
                super.onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}