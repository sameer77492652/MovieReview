package actiknow.com.moviereview.model;

import java.util.ArrayList;

public class Question {
    int ques_id, ques_type;
    String ques_text;
    private ArrayList<Option> options;

    public Question(int ques_id, int ques_type, String ques_text, ArrayList<Option> options) {
        this.ques_id = ques_id;
        this.ques_type = ques_type;
        this.ques_text = ques_text;
        this.options = options;
    }

    public Question() {

    }

    public int getQues_id() {
        return ques_id;
    }

    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }

    public int getQues_type() {
        return ques_type;
    }

    public void setQues_type(int ques_type) {
        this.ques_type = ques_type;
    }

    public String getQues_text() {
        return ques_text;
    }

    public void setQues_text(String ques_text) {
        this.ques_text = ques_text;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Option> options) {
        this.options = options;
    }
}
