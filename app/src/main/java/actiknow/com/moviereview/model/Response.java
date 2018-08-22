package actiknow.com.moviereview.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Response implements Parcelable{
    int question_id;
    String answer, question;

    public Response(int question_id, String answer, String question) {
        this.question_id = question_id;
        this.answer = answer;
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
