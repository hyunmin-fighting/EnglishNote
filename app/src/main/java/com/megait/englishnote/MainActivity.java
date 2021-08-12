package com.megait.englishnote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public void initDb(){
        // db초기화
        SQLiteDatabase db = openOrCreateDatabase("707.db", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS voca("
                + "idx INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "eng TEXT,"
                + "kor TEXT"+
                ")");

        // db검색
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDb();


        // SharedPreference로 데이터 저장
//        SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
//        int vocaArrSize = preferences.getInt("size", 0);
////        Log.d("test", "data :   " + vocaArrSize);
//
//        if(vocaArrSize!=0){
//            NoteInputActivity.vocaArr.clear();
//            for (int i = 0; i < vocaArrSize; i++){
//                String eng = preferences.getString("eng"+i, "");
//                String kor = preferences.getString("kor"+i, "");
////                Log.d("test", "engData : " + eng + ", " + "korData : " + kor);
//                NoteInputActivity.vocaArr.add(new Voca(eng, kor));
//            }
//        }



        // 메뉴얼 입력
//        NoteInputActivity.vocaArr.clear();
//        NoteInputActivity.vocaArr.add(new Voca("january", "1월"));
//        NoteInputActivity.vocaArr.add(new Voca("february", "2월"));
//        NoteInputActivity.vocaArr.add(new Voca("march", "3월"));
//        NoteInputActivity.vocaArr.add(new Voca("april", "4월"));
//        NoteInputActivity.vocaArr.add(new Voca("may", "5월"));
//        NoteInputActivity.vocaArr.add(new Voca("june", "6월"));
//        NoteInputActivity.vocaArr.add(new Voca("july", "7월"));
//        NoteInputActivity.vocaArr.add(new Voca("august", "8월"));
//        NoteInputActivity.vocaArr.add(new Voca("september", "9월"));
//        NoteInputActivity.vocaArr.add(new Voca("october", "10월"));
//        NoteInputActivity.vocaArr.add(new Voca("november", "11월"));
//        NoteInputActivity.vocaArr.add(new Voca("december", "12월"));

        findViewById(R.id.btn_0).setOnClickListener(new View.OnClickListener() {
            @Override
            // 게임 시작 버튼
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            // 단어 추가 버튼
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NoteInputActivity.class));
            }
        });

        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            // 종료 버튼
            public void onClick(View view) {
                finish();
                SystemClock.sleep(1000);
            }
        });

    }

}