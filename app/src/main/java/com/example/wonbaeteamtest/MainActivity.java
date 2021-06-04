package com.example.wonbaeteamtest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListFragment.OnListSelectedListener{
    public static ArrayList<ShelterData>mData=new ArrayList<>();//전역변수로 설정
    public static ArrayList<ShelterData>arraylist=new ArrayList<>();
    public static byte[] byteArray;
    public void onListSelected(int position){

        /*리스트 뷰가 눌리면 해당 객체 정보를 대피소 보기 엑티비티로 보냄*/
        Intent intent = new Intent(this, ShelterInpo.class);
        putExtraInfo(intent,mData.get(position)._id, mData.get(position).byteArray, mData.get(position).subject, mData.get(position).name,
                mData.get(position).address, mData.get(position).provider,mData.get(position).audio,mData.get(position).video);
        startActivityForResult(intent, 0);
    }

    ListFragment listFragment = new ListFragment();
    HomeFragment homeFragment = new HomeFragment();
    HomeLandFragment homelandFragment = new HomeLandFragment();

    private ArrayList<String> Subject = new ArrayList<>(); //스피너 생성용 배열
    private Spinner mSpinner;

    private MenuItem mSearch;

    private int subjectPosition;

    Toolbar toolbar;
    private boolean fragmentCheck;

    private long backKeyPressedTime = 0; //뒤로가기 버튼 눌렀던 시간 저장
    private Toast toast;//첫번째 뒤로가기 버튼을 누를때 표시하는 변수

    private DbOpenHelper mDbOpenHelper;
    private Cursor mCursor;
    private ShelterData shelterData;

    public static void putExtraInfo(Intent intent,int id, byte[] byteArray , int subject, String name, String address, String provider,String audio,String video) {
        //putExtra함수를 묶어버림
        intent.putExtra("id",id);
        intent.putExtra("image",byteArray);
        intent.putExtra("subject", subject);
        intent.putExtra("name", name);
        intent.putExtra("address", address);
        intent.putExtra("provider", provider);
        intent.putExtra("audio",audio);
        intent.putExtra("video",video);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT&&fragmentCheck) {
            goHomeFrag();
        }
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE&&fragmentCheck) {
            goHomeLandFrag();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*툴바생성*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        super.setSupportActionBar(toolbar);

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

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        doWhileCursorToArray();
        mDbOpenHelper.close();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, listFragment).commit();//list프래그먼트 생성 (이거먼저안하면 오류남)
            if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
            goHomeLandFrag();
            }
            else{
                    goHomeFrag();//시작화면이 홈프래그먼트로 시작하게
                }
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
    private void doWhileCursorToArray(){
        arraylist.clear();
        mCursor = null;
        mCursor = mDbOpenHelper.getAllColumns();
        while (mCursor.moveToNext()) {
            shelterData = new ShelterData(
                    mCursor.getInt(mCursor.getColumnIndex("_id")),
                    mCursor.getBlob(mCursor.getColumnIndex("image")),
                    mCursor.getInt(mCursor.getColumnIndex("subject")),
                    mCursor.getString(mCursor.getColumnIndex("name")),
                    mCursor.getString(mCursor.getColumnIndex("address")),
                    mCursor.getString(mCursor.getColumnIndex("provider")),
                    mCursor.getString(mCursor.getColumnIndex("audio")),
                    mCursor.getString(mCursor.getColumnIndex("video"))
            );
            arraylist.add(shelterData);
        }
        mCursor.close();
    }
    public void selectAllView(){
        mSpinner.setSelection(3);
        subjectPosition=3;
        listFragment.Select(subjectPosition);
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
                goSheltFrag();
                listFragment.search(query);
                return true;
            }
            //텍스트가 바뀔때마다 호출
            @Override
            public boolean onQueryTextChange(String newText) {
                goSheltFrag();
                listFragment.search(newText);
                return true;
            }
        });
        return true;
    }
    public void goHomeFrag(){//홈프래그먼트로 가는 메소드
        fragmentCheck=true;
        toolbar.setTitle("네얼간이 대피소") ;//타이틀을 설정
        mSpinner.setVisibility(View.GONE);//스피너를 안보이게
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,homeFragment).commit();
    }
    public void goHomeLandFrag(){
        fragmentCheck=true;
        toolbar.setTitle("네얼간이 대피소") ;//타이틀을 설정
        mSpinner.setVisibility(View.GONE);//스피너를 안보이게
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,homelandFragment).commit();
    }
    public void goSheltFrag(){
        fragmentCheck=false;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,listFragment).commit();
        toolbar.setTitle("") ;//타이틀을 안보이게
        mSpinner.setVisibility(View.VISIBLE);//스피너를 보이게 설정
        selectAllView();//스피너 전체 보기 설정
    }
public void mOnClick(View view){
        switch (view.getId()){
            case R.id.toHome:
                if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
                    goHomeLandFrag();
                }
                else{
                    goHomeFrag();//시작화면이 홈프래그먼트로 시작하게
                }
                break;
            case R.id.toShelter:
               goSheltFrag();
                break;
            case R.id.emer:
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:119"));
                startActivity(myIntent);
        }
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
        int id = data.getIntExtra("id", -1);
        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        if (requestCode == 1 && resultCode == RESULT_OK) {//추가를 누르고 편집엑티비티에서 저장을 누를경우 값들을 객체에 저장하고 리스트뷰 추가
            mDbOpenHelper.insertColumn(data.getByteArrayExtra("image"),data.getIntExtra("subject", -1), data.getStringExtra("name"),
                    data.getStringExtra("provider"), data.getStringExtra("address"),data.getStringExtra("audio"),data.getStringExtra("video"));
        } else if (requestCode == 0 && resultCode == 2) {//대피소 보기 엑티비티에서 삭제버튼을 눌렀을 경우 해당 객체를 삭제
            mDbOpenHelper.deleteColumn(id);
        }
        else if (requestCode == 0 && resultCode == 3) {//대피소 편집 엑티비티에서 저장을 누를 경우 객체 정보를 갱신
            mDbOpenHelper.updateColumn(id,data.getByteArrayExtra("image"), data.getIntExtra("subject", -1), data.getStringExtra("name"),
                    data.getStringExtra("address") ,data.getStringExtra("provider"),data.getStringExtra("audio"),data.getStringExtra("video"));
        }
        doWhileCursorToArray();
        mDbOpenHelper.close();
        listFragment.Select(subjectPosition);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* 뒤로가기 버튼 메소드*/
    public void onBackPressed(){
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