package com.example.wonbaeteamtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShelterEdit extends AppCompatActivity {
    private EditText nameText;
    private EditText addressText;
    private EditText providerText;
    private ArrayList<String> Subject = new ArrayList<>();
    private Spinner mSpinner;
    private int subjectPosition;
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

        Subject.add("지진");
        Subject.add("해일");
        Subject.add("화산");

        mSpinner = (Spinner)findViewById(R.id.editSubject);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Subject);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter1);
        subjectPosition=intent.getIntExtra("subject",-1);
        mSpinner.setSelection(subjectPosition);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               subjectPosition=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                MainActivity.putExtraInfo(data,subjectPosition,nameText.getText().toString(),
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

