package com.example.wonbaeteamtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShelterEdit extends AppCompatActivity {
    private EditText nameText;
    private EditText addressText;
    private EditText providerText;
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_edit);

        nameText=(EditText) findViewById(R.id.shtName);
        addressText=(EditText) findViewById(R.id.shtAdd);
        providerText=(EditText) findViewById(R.id.shtWho);

        Intent intent = getIntent();
        nameText.setText(intent.getStringExtra("name"));
        addressText.setText(intent.getStringExtra("address"));
        providerText.setText(intent.getStringExtra("provider"));
    }

    public void mOnClick(View view){
        Intent data = new Intent();
        switch (view.getId()){
            case R.id.btnSave:
                if(nameText.getText().toString().length()<=0){
                    Toast.makeText(this,"대피소 이름을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    break;
                }
                else{
                MainActivity.putExtraInfo(data,nameText.getText().toString(),
                        addressText.getText().toString(),providerText.getText().toString());
                setResult(RESULT_OK,data);
                finish();
                break;
                }
            case R.id.btnCancel:
                super.onBackPressed();
                break;
        }
    }
}

