package actiknow.com.moviereview.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import actiknow.com.moviereview.R;
import actiknow.com.moviereview.model.Option;
import actiknow.com.moviereview.model.Question;
import actiknow.com.moviereview.model.Response;
import actiknow.com.moviereview.utils.SetTypeFace;
import actiknow.com.moviereview.utils.Utils;

public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private ArrayList<Question> questionList;
    private ArrayList<String> checkBoxValue = new ArrayList<> ();
    private ArrayList<Response> response_list = new ArrayList<>();

    TextView tvReview;
    RecyclerView rvSurveyList;
    String star;
    TextView tvQuestion;
    EditText etComment;

    public QuestionAdapter(Activity activity, ArrayList<Question> questionList, TextView tvReview, RecyclerView rvSurveyList){
        this.activity = activity;
        this.questionList = questionList;
        this.tvReview = tvReview;
        this.rvSurveyList = rvSurveyList;
    }
    
    @Override
    public int getItemViewType (int position) {
        final Question question = questionList.get (position);
        tvReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            response_list.add(new Response(questionList.get(questionList.size() - 1).getQues_id(), etComment.getText().toString(), questionList.get(questionList.size() - 1).getQues_text()));
            if(questionList.size() - 1 != response_list.size()){
                /*for(int i = 0; i < questionList.size() - 1; i++){
                    if(!String.valueOf(response_list.get(i).getQuestion_id()).contains(String.valueOf(i+1))){
                        Utils.showLog(Log.ERROR, "QuestionId", ""+response_list.get(i).getQuestion_id() +"-"+ i+1, true);
                        rvSurveyList.scrollToPosition(i);
                        Utils.showToast(activity, "Please give answer for this question", false);
                        break;
                    }
                }*/
                Utils.showToast(activity, "Please give answer for all the question", false);
            }else{
                Intent intent = new Intent("custom-message");
                intent.putParcelableArrayListExtra("responseList",response_list);
                Utils.showLog(Log.ERROR, "responseList",""+response_list, true);
                LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
            }

            }
        });


        switch (question.getQues_type()) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            default:
                return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView;
        switch (viewType) {
            case 1:
                sView = mInflater.inflate (R.layout.list_item_ques_option, parent, false);
                return new ViewHolder1 (sView);
            case 2:
                sView = mInflater.inflate (R.layout.list_item_textfield, parent, false);
                return new ViewHolder2 (sView);
            case 3:
                sView = mInflater.inflate (R.layout.list_item_question_type_checkbox, parent, false);
                return new ViewHolder3 (sView);
            case 4:
                sView = mInflater.inflate (R.layout.list_item_rating, parent, false);
                return new ViewHolder4 (sView);
            default:
                sView = mInflater.inflate (R.layout.list_item_ques_option, parent, false);
                return new ViewHolder3 (sView);
        }
    }

    @Override
    public void onBindViewHolder (final RecyclerView.ViewHolder holder, final int position) {
        final Question question = questionList.get (position);
        switch (holder.getItemViewType ()) {
            case 1:
                final ViewHolder1 holder1 = (ViewHolder1) holder;
                Utils.setTypefaceToAllViews (activity, holder1.tvQuestion);
                holder1.tvQuestion.setText (question.getQues_text ());
                holder1.tvOptionA.setText(question.getOptions().get(0).getOption_text());
                holder1.tvOptionB.setText(question.getOptions().get(1).getOption_text());
                holder1.tvOptionC.setText(question.getOptions().get(2).getOption_text());
                holder1.tvOptionD.setText(question.getOptions().get(3).getOption_text());
                holder1.tvOptionA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        holder1.tvOptionA.setBackground(activity.getResources().getDrawable(R.drawable.rounded_corner_green));
                        holder1.tvOptionB.setBackground(activity.getResources().getDrawable(R.drawable.rounded_corner));
                        holder1.tvOptionC.setBackground(activity.getResources().getDrawable(R.drawable.rounded_corner));
                        holder1.tvOptionD.setBackground(activity.getResources().getDrawable(R.drawable.rounded_corner));
                        int i = 0;
                        if(response_list.size() > 0) {
                            for (Response response : response_list) {
                                if (response.getQuestion_id() == question.getQues_id()) {
                                    response_list.remove(i);
                                }
                                i++;
                            }
                        }

                        response_list.add(new Response(question.getQues_id(), question.getOptions().get(0).getOption_text(), question.getQues_text()));

                    }
                });

                holder1.tvOptionB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder1.tvOptionA.setBackground(activity.getResources().getDrawable(R.drawable.rounded_corner));
                        holder1.tvOptionB.setBackground(activity.getResources().getDrawable(R.drawable.rounded_corner_green));
                        holder1.tvOptionC.setBackground(activity.getResources().getDrawable(R.drawable.rounded_corner));
                        holder1.tvOptionD.setBackground(activity.getResources().getDrawable(R.drawable.rounded_corner));
                        int i = 0;
                        if(response_list.size() > 0) {
                            for (Response response : response_list) {
                                if (response.getQuestion_id() == question.getQues_id()) {
                                    response_list.remove(i);
                                }
                                i++;
                            }
                        }
                        response_list.add(new Response(question.getQues_id(), question.getOptions().get(1).getOption_text(), question.getQues_text()));

                    }
                });

                holder1.tvOptionC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder1.tvOptionA.setBackground(activity.getResources().getDrawable(R.drawable.rounded_corner));
                        holder1.tvOptionB.setBackground(activity.getResources().getDrawable(R.drawable.rounded_corner));
                        holder1.tvOptionC.setBackground(activity.getResources().getDrawable(R.drawable.rounded_corner_green));
                        holder1.tvOptionD.setBackground(activity.getResources().getDrawable(R.drawable.rounded_corner));
                        int i = 0;
                        if(response_list.size() > 0) {
                            for (Response response : response_list) {
                                if (response.getQuestion_id() == question.getQues_id()) {
                                    response_list.remove(i);
                                }
                                i++;
                            }
                        }
                        response_list.add(new Response(question.getQues_id(), question.getOptions().get(2).getOption_text(), question.getQues_text()));

                    }
                });

                holder1.tvOptionD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder1.tvOptionA.setBackground(activity.getResources().getDrawable(R.drawable.rounded_corner));
                        holder1.tvOptionB.setBackground(activity.getResources().getDrawable(R.drawable.rounded_corner));
                        holder1.tvOptionC.setBackground(activity.getResources().getDrawable(R.drawable.rounded_corner));
                        holder1.tvOptionD.setBackground(activity.getResources().getDrawable(R.drawable.rounded_corner_green));
                        int i = 0;
                        if(response_list.size() > 0) {
                            for (Response response : response_list) {
                                if (response.getQuestion_id() == question.getQues_id()) {
                                    response_list.remove(i);
                                }
                                i++;
                            }
                        }
                        response_list.add(new Response(question.getQues_id(), question.getOptions().get(3).getOption_text(), question.getQues_text()));
                    }
                });
                break;

            case 2:
                ViewHolder2 holder2 = (ViewHolder2) holder;
                Utils.setTypefaceToAllViews (activity, tvQuestion);
                tvQuestion.setText (question.getQues_text ());

                break;
            case 3:
                ViewHolder3 holder3 = (ViewHolder3) holder;
                Utils.setTypefaceToAllViews (activity, holder3.tvQuestion);
                holder3.tvQuestion.setText (question.getQues_text ());
                for(int i = 0; i < question.getOptions ().size (); i++){
                    final Option option = question.getOptions().get(i);
                    final CheckBox cbOption = new CheckBox(activity);
                    cbOption.setId(i);
                    cbOption.setText(option.getOption_text());
                    cbOption.setTypeface(SetTypeFace.getTypeface(activity));
                    cbOption.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    cbOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b){
                            for(int i = 0; i < checkBoxValue.size(); i++){
                                if(checkBoxValue.get(i).equalsIgnoreCase(option.getOption_text())){
                                   checkBoxValue.remove(i);
                                }
                            }
                            checkBoxValue.add(option.getOption_text());
                        }else{
                            for(int i = 0; i < checkBoxValue.size(); i++){
                                if(checkBoxValue.get(i).equalsIgnoreCase(option.getOption_text())){
                                    checkBoxValue.remove(i);
                                }
                            }
                        }
                        Utils.showLog(Log.ERROR, "CheckBoxValue", ""+checkBoxValue.toString(),true);
                        int i = 0;
                        if(response_list.size() > 0) {
                            for (Response response : response_list) {
                                if (response.getQuestion_id() == question.getQues_id()) {
                                    response_list.remove(i);
                                }
                                i++;
                            }
                        }
                        response_list.add(new Response(question.getQues_id(), checkBoxValue.toString(), question.getQues_text()));
                        }

                    });

                    holder3.llOptions.addView(cbOption);
                }
                break;
            case 4:
                ViewHolder4 holder4 = (ViewHolder4) holder;
                Utils.setTypefaceToAllViews (activity, holder4.tvQuestion);
                holder4.tvQuestion.setText (question.getQues_text ());
                holder4.tvRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        star = String.valueOf(ratingBar.getRating());
                        int i = 0;
                        if(response_list.size() > 0) {
                            for (Response response : response_list) {
                                if (response.getQuestion_id() == question.getQues_id()) {
                                    response_list.remove(i);
                                }
                                i++;
                            }
                        }
                        response_list.add(new Response(question.getQues_id(), star, question.getQues_text()));
                        Utils.showToast(activity, "Option ID : " + ratingBar.getRating(), false);
                    }
                });



                break;

            default:
                ViewHolder3 holder3d = (ViewHolder3) holder;
                Utils.setTypefaceToAllViews (activity, holder3d.tvQuestion);
                holder3d.tvQuestion.setText (question.getQues_text ());

                break;
        }


    }

    @Override
    public int getItemCount () {
        return questionList.size ();
    }

    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
    
    public class ViewHolder1 extends RecyclerView.ViewHolder{
        TextView tvQuestion;
        TextView tvOptionA;
        TextView tvOptionB;
        TextView tvOptionC;
        TextView tvOptionD;

        public ViewHolder1 (View view) {
            super (view);
            tvQuestion = (TextView) view.findViewById (R.id.tvQuestion);
            tvOptionA = (TextView) view.findViewById (R.id.tvOptionA);
            tvOptionB = (TextView) view.findViewById (R.id.tvOptionB);
            tvOptionC = (TextView) view.findViewById (R.id.tvOptionC);
            tvOptionD = (TextView) view.findViewById (R.id.tvOptionD);
        }
    }
    
    public class ViewHolder2 extends RecyclerView.ViewHolder{
        
        public ViewHolder2 (View view) {
            super (view);
            tvQuestion = (TextView) view.findViewById (R.id.tvQuestion);
            etComment = (EditText) view.findViewById (R.id.etComment);

        }
    }
    
    public class ViewHolder3 extends RecyclerView.ViewHolder{
        TextView tvQuestion;
        LinearLayout llOptions;
        
        public ViewHolder3 (View view) {
            super (view);
            tvQuestion = (TextView) view.findViewById (R.id.tvQuestion);
            llOptions = (LinearLayout)view.findViewById(R.id.llOptions);
            //view.setOnClickListener (this);
        }
    }

    public class ViewHolder4 extends RecyclerView.ViewHolder{
        TextView tvQuestion;
        RatingBar tvRating;

        public ViewHolder4 (View view) {
            super (view);
            tvQuestion = (TextView) view.findViewById (R.id.tvQuestion);
            tvRating = (RatingBar) view.findViewById(R.id.tvRating);


        }
    }
}