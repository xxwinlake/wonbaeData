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
   private ArrayList<ShelterData> mData=new ArrayList<ShelterData>();//원본 arraylist
   private ListView mList;
   private MyAdapter mAdapter;
   private ArrayList<ShelterData> arraylist;//mData 복사본
    private ArrayList<String> Subject = new ArrayList<>();
    private Spinner mSpinner;
   private MenuItem mSearch;

   Toolbar toolbar;
    private long backKeyPressedTime = 0; //뒤로가기 버튼 눌렀던 시간 저장
    private Toast toast;//첫번째 뒤로가기 버튼을 누를때 표시하는 변수

    public static void putExtraInfo(Intent intent,int subject, String name, String address, String provider){
        //putExtra함수를 묶어버림
        intent.putExtra("subject",subject);
        intent.putExtra("name",name);
        intent.putExtra("address",address);
        intent.putExtra("provider",provider);
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
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        super.setSupportActionBar(toolbar);

        /*리스트뷰 초기회*/
        mList = (ListView)findViewById(R.id.shetList);
        mAdapter=new MyAdapter(this,mData);
        mList.setAdapter(mAdapter);
        arraylist = new ArrayList<ShelterData>();
        arraylist.addAll(mData);//복사본 만들기

        // 리스트의 모든 데이터를 arraylist에 복사한다.// mdata 복사본을 만든다.

        //리스트뷰 클릭 이벤트
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*리스트 뷰가 눌리면 해당 객체 정보를 대피소 보기 엑티비티로 보냄*/
                Intent intent = new Intent(view.getContext(),ShelterInpo.class);
                intent.putExtra("position",position);
                putExtraInfo(intent,mData.get(position).subject, mData.get(position).name, mData.get(position).address, mData.get(position).provider);
                startActivityForResult(intent,0);
            }
        });
        Subject.add("지진");
        Subject.add("해일");
        Subject.add("화산");
        mSpinner = (Spinner)findViewById(R.id.Subject);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Subject);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter1);
        /*스피너 클릭 이벤트*/
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(Subject.equals("전체")){

                }
                else{

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//메뉴 만들기
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

       mSearch=menu.findItem(R.id.btnSearch);
       //menuItem을 이용해서 SearchView 변수 생성
        SearchView sv=(SearchView)mSearch.getActionView();
        sv.setSubmitButtonEnabled(true);//확인버튼 활성화
        //SearchView의 검색 이벤트
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //검색버튼을 눌렀을 경우
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }
            //텍스트가 바뀔때마다 호출
            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });
        return true;
    }
    public void search(String charText) {
        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        mData.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            mData.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).name.contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    mData.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){//메뉴에서 버튼이 눌리면
        switch (item.getItemId()){
            case R.id.btnAdd://추가 버튼이 눌르면
                Intent intent2 = new Intent(this,ShelterEdit.class);//편집 엑티비티로 이동
                startActivityForResult(intent2,1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        int position;
        if (requestCode==1 && resultCode==RESULT_OK){//추가를 누르고 편집엑티비티에서 저장을 누를경우 값들을 객체에 저장하고 리스트뷰 추가
            mData.add(new ShelterData(R.drawable.testpic,data.getIntExtra("subject",-1),data.getStringExtra("name"),
                    data.getStringExtra("provider"),data.getStringExtra("address")));
            arraylist.add(new ShelterData(R.drawable.testpic,data.getIntExtra("subject",-1),data.getStringExtra("name"),
                    data.getStringExtra("provider"),data.getStringExtra("address")));//mData에 객체가 추가되면 복사본도 추가
        }
        else if(requestCode==0&&resultCode==2){//대피소 보기 엑티비티에서 삭제버튼을 눌렀을 경우 해당 객체를 삭제
            position=data.getIntExtra("position",-1);
            mData.remove(position);
        }
        else if(requestCode==0 && resultCode==3){//대피소 편집 엑티비티에서 저장을 누를 경우 객체 정보를 갱신
            position=data.getIntExtra("position",-1);
            mData.get(position).subject=data.getIntExtra("subject",-1);
            mData.get(position).name=data.getStringExtra("name");
            mData.get(position).address=data.getStringExtra("address");
            mData.get(position).provider=data.getStringExtra("provider");
        }
        mAdapter.notifyDataSetChanged();
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void onBackPressed() {
        //super.onBackPressed();
        //기존의 뒤로가기 버튼 기능 막기
        if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime=System.currentTimeMillis();
            toast= Toast.makeText(this,"뒤로 버튼 한번더 누르시면 종료됩니다",Toast.LENGTH_SHORT);
            toast.show();
            return;
        }// 뒤로가기버튼을 한번누르면 현재시간값에 현재버튼누른시간 저장
        if(System.currentTimeMillis()<=backKeyPressedTime+2000){
            finish();
            toast.cancel();
        }//위에서 저장한 현재시간값에 2초안에 버튼을 한번 더 누르면 앱을 종료함.
    }
}