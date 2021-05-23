package com.example.wonbaeteamtest;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.wonbaeteamtest.MainActivity.mData;//메인 엑티비티의 mData를 사용하기 위한 임포트
import static com.example.wonbaeteamtest.MainActivity.arraylist;//메인 엑티비티의 arraylist를 사용하기 위한 임포트

public class ListFragment extends Fragment {
    public interface OnListSelectedListener {
        public void onListSelected(int i);
    }
    private ListView mList;
    private MyAdapter mAdapter;

    public ListFragment() {}//기본생성자
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rv = inflater.inflate(R.layout.fragment_list, container, false);
        /*리스트뷰 초기회*/
        mList = (ListView) rv.findViewById(R.id.shetList);
        mAdapter = new MyAdapter(getActivity(), mData);
        mList.setAdapter(mAdapter);

        //리스트뷰 클릭 이벤트
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Activity activity = getActivity();
                ((OnListSelectedListener) activity).onListSelected(position);
            }
        });

        return rv;
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
}
