package com.example.cindywang.wellnesssurvey.answerList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.cindywang.wellnesssurvey.R;

import java.util.ArrayList;

/**
 * Created by cindywang on 12/14/14.
 */
public class answerListViewAdapter extends BaseAdapter {
    private static final int TYPE_RATE = 0;
    private static final int TYPE_SINGLESELECT = 1;
    private static final int TYPE_BOOLEAN = 2;
    private static final int TYPE_OTHER = 3;
    private static final int TYPE_MAX_COUNT = 4;

    private static ArrayList<answerListItem> mAnswerListItems;
    private LayoutInflater mInflater;
    private static Context mContext;

    public answerListViewAdapter(Context context, ArrayList<answerListItem> listItems){
        mInflater = LayoutInflater.from(context);
        mAnswerListItems = listItems;

        mContext = context;
    }
    @Override
    public int getCount() {
        return mAnswerListItems.size();
    }

    @Override
    public Object getItem(int pos) {
        return mAnswerListItems.get(pos);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }
    @Override
    public int getItemViewType(int position){
        String answerType = mAnswerListItems.get(position).getAnswerType();
        if (answerType.equals("rate")){
            return TYPE_RATE;
        } else if(answerType.equals("singleSelect")) {
            return TYPE_SINGLESELECT;
        } else if (answerType.equals("boolean")){
            return TYPE_BOOLEAN;
        } else {
            return TYPE_OTHER;
        }
    }
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        //add a final int that can be accessed from inside the listener
        final int mListPos = pos;
        ViewHolder holder = null;
        int type = getItemViewType(pos);
        if (convertView == null){
            holder = new ViewHolder();
            switch (type){
                case TYPE_RATE:
                    convertView = mInflater.inflate(R.layout.answerlist_item_rate, null);
                    holder.questionText = (TextView)convertView.findViewById(R.id.rateQuestion);
                    holder.rateSeekBar = (SeekBar)convertView.findViewById(R.id.rateSeekBar);
                    break;
                case TYPE_OTHER:
                    convertView = mInflater.inflate(R.layout.answerlist_item_rate, null);
                    holder.questionText = (TextView)convertView.findViewById(R.id.rateQuestion);
                    break;
                case TYPE_SINGLESELECT:
                    convertView = mInflater.inflate(R.layout.answerlist_item_singleselect, null);
                    holder.questionText = (TextView) convertView.findViewById(R.id.singleSelectQuestion);
                    holder.singleSelectSpinner = (Spinner) convertView.findViewById(R.id.singleSelectSpinner);
                    break;
                case TYPE_BOOLEAN:
                    convertView = mInflater.inflate(R.layout.answerlist_item_boolean, null);
                    holder.questionText = (TextView) convertView.findViewById(R.id.booleanQuestion);
                    holder.booleanSwitch = (Switch) convertView.findViewById(R.id.booleanSwitch);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.questionText.setText(mAnswerListItems.get(pos).getQuestion());
        switch (type){
            case TYPE_RATE:
                //configure seekBar here
                holder.rateSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        mAnswerListItems.get(mListPos).setAnswerNum(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                //configure the position of seekBar because of android view recycling
                if (mAnswerListItems.get(pos).getAnswerNum()>=0)
                    holder.rateSeekBar.setProgress(mAnswerListItems.get(pos).getAnswerNum());
                break;
            case TYPE_SINGLESELECT:
                //configure spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, mAnswerListItems.get(pos).getConfig());
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.singleSelectSpinner.setAdapter(dataAdapter);
                holder.singleSelectSpinner.setOnItemSelectedListener(new CustomSpinnerOnItemSelectedListener(mAnswerListItems, pos));
                if (mAnswerListItems.get(pos).getAnswer() != null)
                    setSpinnerSelection(holder.singleSelectSpinner, pos);
                break;
            case TYPE_BOOLEAN:
                //configure switch
                holder.booleanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked)
                            mAnswerListItems.get(mListPos).setAnswer("YES");
                        else
                            mAnswerListItems.get(mListPos).setAnswer("NO");
                    }
                });
                break;
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView questionText;
        SeekBar rateSeekBar;
        Spinner singleSelectSpinner;
        Switch booleanSwitch;
    }

    //set the selected spinner data if already selected
    private void setSpinnerSelection(Spinner spinner, int dataPosition){
        answerListItem currentData = mAnswerListItems.get(dataPosition);
        int spinnerPos = 0;
        String selectedAnswer = currentData.getAnswer();
        //loop through the configuration to find the selected position
        for (String currentStr: currentData.getConfig()){
            if (currentStr.equals(selectedAnswer)){
                spinner.setSelection(spinnerPos);
                break;
            }
            spinnerPos++;
        }
    }
}
