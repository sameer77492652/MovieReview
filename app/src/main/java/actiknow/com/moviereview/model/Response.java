package actiknow.com.moviereview.model;

public class Response {
    int question_id;
    String answer;

    public Response(int question_id, String answer) {
        this.question_id = question_id;
        this.answer = answer;
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
}
