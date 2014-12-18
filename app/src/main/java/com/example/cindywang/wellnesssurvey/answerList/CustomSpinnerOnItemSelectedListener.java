package com.example.cindywang.wellnesssurvey.answerList;

import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

/**
 * Created by cindywang on 12/17/14.
 */
public class CustomSpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

    private answerListItem currentAnswerListItem;

    public CustomSpinnerOnItemSelectedListener(ArrayList<answerListItem> answerListItems, int pos){
        currentAnswerListItem = answerListItems.get(pos);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int spinnerPos, long id) {
        String answer = currentAnswerListItem.getConfig().get(spinnerPos);
        currentAnswerListItem.setAnswer(answer);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
