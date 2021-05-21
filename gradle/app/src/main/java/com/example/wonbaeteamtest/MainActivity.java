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

public class MainActivity extends AppCompatActivity {

    private ArrayList<ShelterData> mData = new ArrayList<ShelterData>();//원본 arraylist
    private ArrayList<ShelterData> arraylist;//mData 복사본
    private ArrayList<String> Subject = new ArrayList<>(); //스피너 생성용 배열

    private ListView mList;
    private MyAdapter mAdapter;

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
        super.setSupportActionBar(toolbar);

        /*리스트뷰 초기회*/
        mList = (ListView) findViewById(R.id.shetList);
        mAdapter = new MyAdapter(this, mData);
        mList.setAdapter(mAdapter);
        arraylist = new ArrayList<ShelterData>();
        arraylist.addAll(mData);//복사본 만들기

        // 리스트의 모든 데이터를 arraylist에 복사한다.// mdata 복사본을 만든다.

        //리스트뷰 클릭 이벤트
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*리스트 뷰가 눌리면 해당 객체 정보를 대피소 보기 엑티비티로 보냄*/
                Intent intent = new Intent(view.getContext(), ShelterInpo.class);
                intent.putExtra("position", position);
                putExtraInfo(intent, mData.get(position).subject, mData.get(position).name, mData.get(position).address, mData.get(position).provider);
                startActivityForResult(intent, 0);
            }
        });

        //스피너 사용을 위한 Subject 배열에 요소추가
        Subject.add("지진");
        Subject.add("해일");
        Subject.add("화산");
        Subject.add("전체보기");
        mSpinner = (Spinner) findViewById(R.id.Subject);

        //adapter1에 Subject 배열을 넣어줌.
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Subject);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter1);
        mSpinner.setSelection(3);//초기 설정값 (전체보기) 0.지진 1.해일 2.화산. 3전체

        /*스피너 클릭 이벤트*/
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Select(position);
                subjectPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /* Select 스피너 클릭메소드 구현*/
    public void Select(int pos) {
        mData.clear();
        if (pos == 3) {
            mData.addAll(arraylist);
        } else {
            for (int i = 0; i < arraylist.size(); i++) {
                //객체의 subject(스피너의 인덱스)가 pos(메인 스피너에서 선택된 인덱스)이면
                if (arraylist.get(i).subject == pos) {
                    // 그 객체를 mData에 넣겠다
                    mData.add(arraylist.get(i));
                }
            }
        }
        mAdapter.notifyDataSetChanged();
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
        sv.setQueryHint("대피소 이름 검색");

        //SearchView의 검색 이벤트
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //검색버튼을 눌렀을 경우
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                mSpinner.setSelection(3);//검색시 전체보기로 초기세팅값을 설정
                return true;
            }

            //텍스트가 바뀔때마다 호출
            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                mSpinner.setSelection(3);//검색시 전체보기로 초기세팅값을 설정
                return true;
            }
        });
        return true;
    }

  /*search 서치뷰기능 메소드 */
    public void search(String charText) {
        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        mData.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            mData.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else {
            // 리스트의 모든 데이터를 검색한다.
            for (int i = 0; i < arraylist.size(); i++) {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).name.contains(charText)) {
                    // 검색된 데이터를 리스트에 추가한다.
                    mData.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        mAdapter.notifyDataSetChanged();
    }

    /* 액션바에서 추가버튼 눌렸을 시 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//메뉴에서 버튼이 눌리면
        switch (item.getItemId()) {
            case R.id.btnAdd://추가 버튼이 눌르면
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


            mData.add(new ShelterData(R.drawable.testpic, data.getIntExtra("subject", -1), data.getStringExtra("name"),
                    data.getStringExtra("provider"), data.getStringExtra("address")));
            arraylist.add(new ShelterData(R.drawable.testpic, data.getIntExtra("subject", -1), data.getStringExtra("name"),
                    data.getStringExtra("provider"), data.getStringExtra("address")));//mData에 객체가 추가되면 복사본도 추가


        } else if (requestCode == 0 && resultCode == 2) {//대피소 보기 엑티비티에서 삭제버튼을 눌렀을 경우 해당 객체를 삭제
            position = data.getIntExtra("position", -1);
            mData.clear();//mData 초기화
            if(subjectPosition == 3) {//전체보기일때
                arraylist.remove(position);//복사본 리스트 삭제
            }else{
                for (int i = 0; i < arraylist.size(); i++) {//리스트를 모두 검사
                    if (arraylist.get(i).subject == subjectPosition)//현재 주제값과 리스트의 주제값이 같을때
                        arraylist.remove(i);//리스트삭제
                }
            }
        }

        else if (requestCode == 0 && resultCode == 3) {//대피소 편집 엑티비티에서 저장을 누를 경우 객체 정보를 갱신
            position = data.getIntExtra("position", -1);
            mData.get(position).subject = data.getIntExtra("subject", -1);
            mData.get(position).name = data.getStringExtra("name");
            mData.get(position).address = data.getStringExtra("address");
            mData.get(position).provider = data.getStringExtra("provider");
        }
        Select(subjectPosition);

       // mAdapter.notifyDataSetChanged(); Select 함수에 이미 있음.

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