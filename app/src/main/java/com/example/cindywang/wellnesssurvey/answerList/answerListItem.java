package com.example.cindywang.wellnesssurvey.answerList;

import java.util.List;

/**
 * Created by cindywang on 12/14/14.
 */
public class answerListItem {
    private String question;
    private String answerType;

    private List<String> config;

    private String answer;
    private int answerNum;

    public answerListItem(String q, String a){
        this.question = q;
        this.answerType = a;

        this.config = null;
        this.answer = null;
        this.answerNum = -1;
    }
    public void setQuestion(String q) {this.question = q;}
    public void setAnswerType(String a) {this.answerType = a;}
    public String getQuestion() {return this.question;}
    public String getAnswerType() {return this.answerType;}

    public void setAnswer(String a){this.answer = a;}
    public String getAnswer() {return this.answer;}
    public void setAnswerNum(int num){this.answerNum = num;}
    public int getAnswerNum() {return this.answerNum;}
    public void setConfig(List<String> array){this.config = array;}
    public List<String> getConfig() {return this.config;}
}
