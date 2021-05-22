package com.example.wonbaeteamtest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListFragment.OnListSelectedListener{
    public void onListSelected(int position,ArrayList<ShelterData> mData){
        mData1=mData;
        /*리스트 뷰가 눌리면 해당 객체 정보를 대피소 보기 엑티비티로 보냄*/
        Intent intent = new Intent(this, ShelterInpo.class);
        intent.putExtra("position", position);
        putExtraInfo(intent, mData1.get(position).subject, mData1.get(position).name,
                mData1.get(position).address, mData1.get(position).provider);
        startActivityForResult(intent, 0);
    }

    ListFragment listFragment = new ListFragment();

private ArrayList<ShelterData>mData1=new ArrayList<>();
    private ArrayList<ShelterData>arraylist1=new ArrayList<>();
    private ArrayList<String> Subject = new ArrayList<>(); //스피너 생성용 배열

    private Spinner mSpinner;

    private MenuItem mSearch;

    private int subjectPosition;

    Toolbar toolbar;
    private long backKeyPressedTime = 0; //뒤로가기 버튼 눌렀던 시간 저장
    private Toast toast;//첫번째 뒤로가기 버튼을 누를때 표시하는 변수

    public static void putExtraInfo(Intent intent, int subject, String name, String address, String provider) {
        //putExtra함수를 묶어버림
        intent.putExtra("subject", subject);
        intent.putExtra("name", name);
        intent.putExtra("address", address);
        intent.putExtra("provider", provider);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*툴바생성*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("") ;
        super.setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.list,listFragment).commit();
        //스피너 사용을 위한 Subject 배열에 요소추가
        Subject.add("지진 대피소");
        Subject.add("해일 대피소");
        Subject.add("화산 대피소");
        Subject.add("대피소 전체보기");
        mSpinner = (Spinner) findViewById(R.id.Subject);

        //adapter1에 Subject 배열을 넣어줌.
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Subject);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter1);
        selectSP();//초기 설정값 (전체보기) 0.지진 1.해일 2.화산. 3전체

        /*스피너 클릭 이벤트*/
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listFragment.Select(position);
                subjectPosition = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public void selectSP(){
        mSpinner.setSelection(3);
        subjectPosition=3;
    }
    /*액션바에 메뉴구현 (서치뷰사용포함)*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        mSearch = menu.findItem(R.id.btnSearch);
        //menuItem을 이용해서 SearchView 변수 생성
        SearchView sv = (SearchView) mSearch.getActionView();
        sv.setSubmitButtonEnabled(true);//확인버튼 활성화
        sv.setQueryHint("전체에서 이름 검색");

        //SearchView의 검색 이벤트
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //검색버튼을 눌렀을 경우
            @Override
            public boolean onQueryTextSubmit(String query) {
                selectSP();//검색시 전체보기로 초기세팅값을 설정
                listFragment.search(query);
                return true;
            }
            //텍스트가 바뀔때마다 호출
            @Override
            public boolean onQueryTextChange(String newText) {
                selectSP();//검색시 전체보기로 초기세팅값을 설정
                listFragment.search(newText);
                return true;
            }
        });
        return true;
    }


    /* 액션바에서 추가버튼 눌렸을 시 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//메뉴에서 버튼이 눌리면
        switch (item.getItemId()) {
            case R.id.btnAdd://추가 버튼이 누르면
                Intent intent2 = new Intent(this, ShelterEdit.class);//편집 엑티비티로 이동
                startActivityForResult(intent2, 1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    /*인텐트이동*/
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        int position;
        if (requestCode == 1 && resultCode == RESULT_OK) {//추가를 누르고 편집엑티비티에서 저장을 누를경우 값들을 객체에 저장하고 리스트뷰 추가
            mData1.add(new ShelterData(R.drawable.testpic, data.getIntExtra("subject", -1), data.getStringExtra("name"),
                    data.getStringExtra("provider"), data.getStringExtra("address")));
            arraylist1.add(new ShelterData(R.drawable.testpic, data.getIntExtra("subject", -1), data.getStringExtra("name"),
                    data.getStringExtra("provider"), data.getStringExtra("address")));//mData에 객체가 추가되면 복사본도 추가

        } else if (requestCode == 0 && resultCode == 2) {//대피소 보기 엑티비티에서 삭제버튼을 눌렀을 경우 해당 객체를 삭제
            position = data.getIntExtra("position", -1);
            if(subjectPosition==3){
                arraylist1.remove(position);}
            else{
                int count=0;
                for(int i = 0;i < arraylist1.size(); i++)
                {
                    if(arraylist1.get(i).subject==subjectPosition){
                        if(count==position){
                            arraylist1.remove(i);
                        }
                        count++;
                    }
                }
            }
        }
        else if (requestCode == 0 && resultCode == 3) {//대피소 편집 엑티비티에서 저장을 누를 경우 객체 정보를 갱신
            position = data.getIntExtra("position", -1);
            mData1.get(position).subject = data.getIntExtra("subject", -1);
            mData1.get(position).name = data.getStringExtra("name");
            mData1.get(position).address = data.getStringExtra("address");
            mData1.get(position).provider = data.getStringExtra("provider");
        }
        listFragment.setmData(mData1);
        listFragment.setArraylist(arraylist1);
        listFragment.Select(subjectPosition);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* 뒤로가기 버튼 메소드*/
    public void onBackPressed() {
        //super.onBackPressed();
        //기존의 뒤로가기 버튼 기능 막기
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 버튼 한번더 누르시면 종료됩니다", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }// 뒤로가기버튼을 한번누르면 현재시간값에 현재버튼누른시간 저장
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }//위에서 저장한 현재시간값에 2초안에 버튼을 한번 더 누르면 앱을 종료함.
    }
}