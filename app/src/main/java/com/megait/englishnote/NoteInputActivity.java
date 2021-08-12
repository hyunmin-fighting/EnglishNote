package com.megait.englishnote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteInputActivity extends AppCompatActivity {

    EditText et_eng, et_kor;
    TextView tv_search;
    Button btn_save, btn_search, btn_goOrigin;
    AlertDialog dialog;

    String eng;
    String kor;

    ListView lv_note;
//    ArrayAdapter<String> adapter;
    MyAdapter adapter;

    ArrayList<String> dataArr = new ArrayList<>();

    int itemIdx;
    boolean isEnterMode = true;
    SQLiteDatabase db;

    class MyHolder {
        TextView eng_tv;
        TextView kor_tv;
        ImageView iv;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_input);

        et_eng = findViewById(R.id.et_eng);
        et_kor = findViewById(R.id.et_kor);
        tv_search = findViewById(R.id.tv_search);
        btn_save = findViewById(R.id.btn_save);
        btn_search = findViewById(R.id.btn_search);
        btn_goOrigin = findViewById(R.id.btn_goOrigin);
        lv_note = findViewById(R.id.lv_note);

//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataArr);
        adapter = new MyAdapter(this);
        lv_note.setAdapter(adapter);

        btn_goOrigin.setVisibility(View.GONE);
        tv_search.setVisibility(View.GONE);

//        showVoca();




        // 입력 버튼 클릭 리스너
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            // 단어 추가 버튼
            public void onClick(View v) {
                eng = et_eng.getText().toString();
                kor = et_kor.getText().toString();
                Log.d("aabb","isEnterMode: "+isEnterMode);

                if (isEnterMode) {
                    // 입력 모드
                    Voca voca = new Voca(eng, kor);
                    Storage.vocaArr.add(voca);
//                    vocaArr.add(0, voca);
                    saveVocaDB();
                    getVocaDB();

                } else {
                    // 수정 모드
                    Voca voca = Storage.vocaArr.get(itemIdx);
                    voca.setEng(eng);
                    voca.setKor(kor);
                    btn_save.setText("입력");
                    isEnterMode = true;
                    saveVocaDB();
                    getVocaDB();
                }

                et_kor.setText("");
                et_eng.setText("");

//                showVoca();
            }
        });

        // 찾기 버튼 클릭 리스너
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int findNum = 0;
                dataArr.clear();
                adapter.notifyDataSetChanged();

                btn_goOrigin.setVisibility(View.VISIBLE);
                tv_search.setVisibility(View.VISIBLE);

                for (int i = 0; i < Storage.vocaArr.size(); i++) {
                    if (et_eng.length() > 0 && Storage.vocaArr.get(i).getEng().contains(et_eng.getText().toString())) {
                        dataArr.add(Storage.vocaArr.get(i).getEng() + ":" + Storage.vocaArr.get(i).getKor());
                        findNum++;
                    } else if (et_kor.length() > 0 && Storage.vocaArr.get(i).getKor().contains(et_kor.getText().toString())) {
                        dataArr.add(Storage.vocaArr.get(i).getEng() + ":" + Storage.vocaArr.get(i).getKor());
                        findNum++;
                    } else {
                        Log.d("test", "데이터가없음");
                    }
                }
                adapter.notifyDataSetChanged();
                tv_search.setText("총 " + findNum + "개의 단어가 검색되었습니다.");
            }
        });

        // 돌아가기 버튼 리스너
        btn_goOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showVoca();
                btn_goOrigin.setVisibility(View.GONE);
                tv_search.setVisibility(View.GONE);

                et_kor.setText("");
                et_eng.setText("");
            }
        });

        // ListView의 Item 클릭 리스너
        lv_note.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                itemIdx = i;

                // 기본 다이얼로그
                AlertDialog.Builder ab = new AlertDialog.Builder((NoteInputActivity.this));
                ab.setTitle("안녕하세요. 기본 다이얼로그입니다.");
                ab.setIcon(R.mipmap.ic_launcher);
                ab.setMessage("수정 or 삭제");
                // 수정 버튼
                ab.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        et_eng.setText(Storage.vocaArr.get(itemIdx).getEng());
                        et_kor.setText(Storage.vocaArr.get(itemIdx).getKor());
                        btn_save.setText("수정");
                        isEnterMode = false;
                    }
                });
                // 삭제 버튼
                ab.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        eng = Storage.vocaArr.get(itemIdx).getEng();
                        kor = Storage.vocaArr.get(itemIdx).getKor();
                        Storage.vocaArr.remove(itemIdx);
//                        showVoca();
//                        dbSerch();
//                        Log.d("test", "findNum : " + findNum);
//                        db.execSQL("DELETE FROM member where idx = "+findNum) ;
                        saveVocaDB();
                        getVocaDB();
                    }
                });

                ab.setCancelable(false);
                ab.show();
            }
        });

        // ListView의 Item Long 클릭 리스너
        lv_note.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                itemIdx = i;
                // 커스텀 다이얼 로그
                View myView = getLayoutInflater().inflate(R.layout.popup, null, false);
                AlertDialog.Builder ab = new AlertDialog.Builder((NoteInputActivity.this));

                ab.setView(myView);
                ab.setTitle("수정&삭제");
                ab.setMessage("뭐할래?");

                // 커스텀 다이얼 로그의 수정 버튼 클릭 시
                Button btn_modify = myView.findViewById(R.id.btn_modify);
                btn_modify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        et_eng.setText(Storage.vocaArr.get(itemIdx).getEng());
                        et_kor.setText(Storage.vocaArr.get(itemIdx).getKor());
                        btn_save.setText("수정");
                        isEnterMode = false;

                        dialog.dismiss();
                    }
                });
                // 커스텀 다이얼 로그의 삭제 버튼 클릭 시
                Button btn_delete = myView.findViewById(R.id.btn_delete);
                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Storage.vocaArr.remove(itemIdx);

//                        showVoca();
                        dialog.dismiss();
//
//                        dbSerch();
//                        Log.d("test", "findNum : " + findNum);
//                        db.execSQL("DELETE FROM member where idx = " + findNum) ;
                        saveVocaDB();
                        getVocaDB();
               }
                });

                ab.setCancelable(false);
                dialog = ab.show();
                return true;
            }
        });

    }       // OnCreate() 종료

    private void saveVocaDB() {
        SQLiteDatabase db = openOrCreateDatabase("707.db", Context.MODE_PRIVATE, null);
        db.execSQL("DELETE FROM voca") ;
        for (int i = 0; i < Storage.vocaArr.size(); i++) {
            Voca v = Storage.vocaArr.get(i);
            String sql = "INSERT INTO voca (eng, kor) VALUES ('" + v.getEng() + "','" + v.getKor() + "')";
            db.execSQL(sql);
        }
        db.close();
    }

    private void getVocaDB() {
        SQLiteDatabase db = openOrCreateDatabase("707.db", Context.MODE_PRIVATE, null);

        Cursor c = db.rawQuery("SELECT * FROM voca", null);
        c.moveToFirst();
        Storage.vocaArr.clear();
        while (c.isAfterLast() == false) {
            Voca voca = new Voca(c.getString(1), c.getString(2));
            Storage.vocaArr.add(voca);
            c.moveToNext();
        }
        c.close();
        db.close();
        adapter.notifyDataSetChanged();
    }



    class MyAdapter extends ArrayAdapter {
        LayoutInflater lif;

        public MyAdapter(Activity context) {
            super(context, R.layout.item, Storage.vocaArr);
            lif = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return Storage.vocaArr.size();
        }

        @Override
        public Object getItem(int position) {
            return Storage.vocaArr.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyHolder viewHolder;
            if (convertView == null) {
                convertView = lif.inflate(R.layout.item, parent, false);
                viewHolder = new MyHolder();
                viewHolder.eng_tv = convertView.findViewById(R.id.eng_tv);
                viewHolder.kor_tv = convertView.findViewById(R.id.kor_tv);
                viewHolder.iv = convertView.findViewById(R.id.iv);

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (MyHolder)convertView.getTag();
            }

            viewHolder.eng_tv.setText(Storage.vocaArr.get(position).getEng());
            viewHolder.kor_tv.setText(Storage.vocaArr.get(position).getKor());

            return convertView;
        }
    }


//    private void showVoca() {
//
////        SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
////        SharedPreferences.Editor editor = preferences.edit();
////        editor.clear();
//        dataArr.clear();
//
//        for (int i = 0; i < Storage.vocaArr.size(); i++) {
//            dataArr.add(Storage.vocaArr.get(i).getEng() + ":" + Storage.vocaArr.get(i).getKor());
////            editor.putString("eng" + i, vocaArr.get(i).getEng());
////            editor.putString("kor" + i, vocaArr.get(i).getKor());
//        }
//        adapter.notifyDataSetChanged();
////        editor.putInt("size", vocaArr.size());
////        editor.commit();
//
//    }

//    public void dbSerch(){
//        // db검색
//        Cursor c = db.rawQuery("SELECT * FROM member", null);
//        c.moveToFirst();
//        while (c.isAfterLast() == false) {
//
//            int idx_pos = c.getColumnIndex("idx");
//            int eng_pos = c.getColumnIndex("eng");
//            int kor_pos = c.getColumnIndex("kor");
//
//            int idx = c.getInt(idx_pos);
//            String chkeng = c.getString(eng_pos);
//            String chkkor = c.getString(kor_pos);
//
//            if (eng.equals(chkeng) && kor.equals(chkkor)) {
//                findNum = idx;
//                break;
//            }
//            c.moveToNext();
//        }
//    }

}


//        // 키보드가 올라올때 View사이즈를 자동으로 조절
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);