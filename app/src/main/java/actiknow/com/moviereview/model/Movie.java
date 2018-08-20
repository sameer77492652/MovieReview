package actiknow.com.moviereview.model;

public class Movie {
    private int id, movie_id;
    private String movie_title, movie_poster_path, movie_language, movie_original_title;


    public Movie(int id, int movie_id, String movie_title, String movie_poster_path, String movie_language, String movie_original_title) {
        this.id = id;
        this.movie_id = movie_id;
        this.movie_title = movie_title;
        this.movie_poster_path = movie_poster_path;
        this.movie_language = movie_language;
        this.movie_original_title = movie_original_title;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public void setMovie_title(String movie_title) {
        this.movie_title = movie_title;
    }

    public String getMovie_poster_path() {
        return movie_poster_path;
    }

    public void setMovie_poster_path(String movie_poster_path) {
        this.movie_poster_path = movie_poster_path;
    }

    public String getMovie_language() {
        return movie_language;
    }

    public void setMovie_language(String movie_language) {
        this.movie_language = movie_language;
    }

    public String getMovie_original_title() {
        return movie_original_title;
    }

    public void setMovie_original_title(String movie_original_title) {
        this.movie_original_title = movie_original_title;
    }
}
