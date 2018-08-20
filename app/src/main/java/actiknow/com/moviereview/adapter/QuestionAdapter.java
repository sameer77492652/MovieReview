package actiknow.com.moviereview.adapter;

import android.app.Activity;
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
    private ArrayList<String> answerList = new ArrayList<> ();
    private ArrayList<String> checkBoxValue = new ArrayList<> ();
    private ArrayList<Integer> position_list = new ArrayList<> ();

    private ArrayList<Response> response_list = new ArrayList<>();

    TextView tvReview;
    RecyclerView rvSurveyList;
    int count = 0;
    String star;

    public QuestionAdapter(Activity activity, ArrayList<Question> questionList, TextView tvReview, RecyclerView rvSurveyList){
        this.activity = activity;
        this.questionList = questionList;
        this.tvReview = tvReview;
        this.rvSurveyList = rvSurveyList;
    }
    
    @Override
    public int getItemViewType (int position) {
        Question question = questionList.get (position);
        tvReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Count", ""+response_list.size());
                for(Response response : response_list){
                    Log.e("Response", ""+response.getQuestion_id()+"-"+response.getAnswer());
                }
                /*if(questionList.size() - 1 != position_list.size()){
                    for(int i = 0; i < questionList.size() - 1; i++){
                        if(!position_list.contains(i)){
                            rvSurveyList.scrollToPosition(i);
                            Utils.showToast(activity, "Please give answer for this question", false);
                            break;
                        }
                    }
                }else{
                    Intent intent = new Intent("custom-message");
                    intent.putExtra("star",star);
                    LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                }*/
                Log.e("Answer",""+ answerList);

                Utils.showLog(Log.ERROR,"Clicked", ""+position_list, true);
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
                        for(Response response : response_list){
                            if(response.getQuestion_id() == question.getQues_id()) {
                                response_list.remove(i);
                            }else {
                                response_list.add(new Response(question.getQues_id(), question.getOptions().get(0).getOption_text()));
                            }
                            i++;
                        }

                        /*if(!position_list.contains(position+"-"+position)){
                            position_list.add(position);
                            answerList.add(position+"-"+String.valueOf(question.getOptions().get(0).getOption_text()));
                        }else{
                            position_list.remove(position);
                            answerList.remove(position);
                            position_list.add(position);
                            answerList.add(position+"-"+String.valueOf(question.getOptions().get(0).getOption_text()));
                        }*/
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
                        for(Response response : response_list){
                            if(response.getQuestion_id() == question.getQues_id()) {
                                response_list.remove(i);
                            }else {
                                response_list.add(new Response(question.getQues_id(), question.getOptions().get(0).getOption_text()));
                            }
                            i++;
                        }
                        /*if(!position_list.contains(position)) {
                            position_list.add(position);
                            answerList.add(position+"-"+String.valueOf(question.getOptions().get(1).getOption_text()));
                        }else{
                            position_list.remove(position);
                            answerList.remove(position);
                            position_list.add(position);
                            answerList.add(position+"-"+String.valueOf(question.getOptions().get(0).getOption_text()));
                        }*/
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
                        for(Response response : response_list){
                            if(response.getQuestion_id() == question.getQues_id()) {
                                response_list.remove(i);
                            }else {
                                response_list.add(new Response(question.getQues_id(), question.getOptions().get(0).getOption_text()));
                            }
                            i++;
                        }
                        /*if(!position_list.contains(position)) {
                            position_list.add(position);
                            answerList.add(position+"-"+String.valueOf(question.getOptions().get(2).getOption_text()));
                        }else{
                            position_list.remove(position);
                            answerList.remove(position);
                            position_list.add(position);
                            answerList.add(position+"-"+String.valueOf(question.getOptions().get(0).getOption_text()));
                        }*/
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
                        for(Response response : response_list){
                            if(response.getQuestion_id() == question.getQues_id()) {
                                response_list.remove(i);
                            }else {
                                response_list.add(new Response(question.getQues_id(), question.getOptions().get(0).getOption_text()));
                            }
                            i++;
                        }
                        /*if(!position_list.contains(position)) {
                            position_list.add(position);
                            answerList.add(position+"-"+String.valueOf(question.getOptions().get(3).getOption_text()));
                        }else{
                            position_list.remove(position);
                            answerList.remove(position);
                            position_list.add(position);
                            answerList.add(position+"-"+String.valueOf(question.getOptions().get(3).getOption_text()));

                            Utils.showLog(Log.ERROR, "QuestionList",""+ position_list, true);
                            Utils.showLog(Log.ERROR, "AnswerList",""+ answerList, true);

                          //  answerList.remove(answerList.indexOf(position));
                        }*/
                    }
                });
                break;

            case 2:
                ViewHolder2 holder2 = (ViewHolder2) holder;
                Utils.setTypefaceToAllViews (activity, holder2.tvQuestion);
                holder2.tvQuestion.setText (question.getQues_text ());
                if(holder2.etComment.length() > 0){
                    position_list.add(position);
                }

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
                                count++;
                                if(!position_list.contains(position)) {
                                    position_list.add(position);
                                }

                                checkBoxValue.add(cbOption.getId(), option.getOption_text());
                            }else{
                                count--;
                                if(count == 0){
                                     position_list.remove(position);
                                }
                                if(checkBoxValue.contains(option.getOption_text()))
                                    checkBoxValue.remove(cbOption.getId());
                            }
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
                        if(!position_list.contains(position)){
                            position_list.add(position);
                            answerList.add(star);
                        }else{
                            position_list.remove(position_list.indexOf(position));
                            answerList.remove(position_list.indexOf(position));
                        }
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
        TextView tvQuestion;
        EditText etComment;
        
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