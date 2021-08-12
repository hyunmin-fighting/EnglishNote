package com.megait.englishnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_chg, btn_confirm;
    TextView tv_po, tv_quiz;
    EditText et_ans;
    ProgressBar progressBar;

    int point = 0;
    int idx = 0;
    boolean checkMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        btn_chg = findViewById(R.id.btn_chg);
        btn_confirm = findViewById(R.id.btn_confirm);
        tv_po = findViewById(R.id.tv_po);
        tv_quiz = findViewById(R.id.tv_quiz);
        et_ans = findViewById(R.id.et_ans);
        progressBar = findViewById(R.id.progressBar);

        initialize();

        btn_confirm.setOnClickListener(this);
        btn_chg.setOnClickListener(this);
    }


    int[] turn;

    private void initialize() {

        int quizNum = Storage.vocaArr.size();
        turn = new int[quizNum];
        boolean[] ranNum = new boolean[quizNum];
        Random ran = new Random();

        // Shuffle
        for (int i=0; i < quizNum; i++){
            int tempNum = ran.nextInt(quizNum);

            if(!ranNum[tempNum]){
                turn[i] = tempNum;
                ranNum[tempNum] = true;
            }else{
                i--;
            }
        }
        //Log.d("test", "랜덤 생성 수 확인 : " + Arrays.toString(turn));

        progressBar.setMax(quizNum);
        setQuiz();
    }

    String ans = "";

    void setQuiz(){
        String quiz = "";

        if(checkMode){
            quiz = Storage.vocaArr.get(turn[idx]).getEng();
            ans = Storage.vocaArr.get(turn[idx]).getKor();
        }else{
            quiz = Storage.vocaArr.get(turn[idx]).getKor();
            ans = Storage.vocaArr.get(turn[idx]).getEng();
        }
        tv_quiz.setText(quiz);
        tv_po.setText("점수 : " + point + "점");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_chg:
                checkMode = !checkMode;
                idx = 0;
                point = 0;
                setQuiz();
                break;

            case R.id.btn_confirm:

                String myAns = et_ans.getText().toString().trim();
                if(ans.equals(myAns)){
                    Toast.makeText(getApplicationContext(),"정답입니다.", Toast.LENGTH_SHORT).show();
                    point += 10;
                } else {
                    Toast.makeText(getApplicationContext(),"오답입니다.", Toast.LENGTH_SHORT).show();
                    point -= 10;
                }
//                int curProgress = progressBar.getProgress();
//                curProgress++;
                progressBar.setProgress(idx+1);


                if(Storage.vocaArr.size()-1 == idx){
                    Toast.makeText(getApplicationContext(), "게임종료", Toast.LENGTH_SHORT).show();
                    idx = 0;
                    point = 0;
                    finish();
                }else {
                    idx++;
                    et_ans.setText("");
                    setQuiz();
                }
                break;
        }
    }
}