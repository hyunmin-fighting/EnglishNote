package com.megait.englishnote;

public class Voca {

    private String eng;
    private String kor;

    public Voca(){}

    public Voca(String eng, String kor){
        this.eng = eng;
        this.kor = kor;
    }


    public String getEng() {
        return eng;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public String getKor() {
        return kor;
    }

    public void setKor(String kor) {
        this.kor = kor;
    }


}
