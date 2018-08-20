package actiknow.com.moviereview.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import actiknow.com.moviereview.R;
import actiknow.com.moviereview.utils.AppConfigTags;
import actiknow.com.moviereview.utils.AppConfigURL;
import actiknow.com.moviereview.utils.Constants;
import actiknow.com.moviereview.utils.NetworkConnection;
import actiknow.com.moviereview.utils.Utils;

/**
 * Created by sud on 2/7/18.
 */

public class MovieDetailActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    TextView tvMovieTitle;
    TextView tvReleaseDate;
    TextView tvAboutMovie;
    TextView tvReview;
    TextView tvToolbarMovieTitle;
    ImageView ivMovie;
    int movie_id = 0;
    ProgressDialog progressDialog;

    ArrayList<String>imageList = new ArrayList<>();

    CoordinatorLayout clMain;

    String image;
    String movie_name;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        initView();
        getExtras();
        initData();
        initListener();
        getMovieDetails();
    }

    private void initListener() {
        tvReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieDetailActivity.this, SurveyActivity.class);
                intent.putExtra(AppConfigTags.MOVIE_TITLE, movie_name);
                intent.putExtra(AppConfigTags.MOVIE_IMAGE, image);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        progressDialog = new ProgressDialog(this);
    }

    private void getExtras() {
        Intent intent = getIntent ();
        movie_id = intent.getIntExtra(AppConfigTags.MOVIE_ID, 0);
        Utils.showLog(Log.ERROR, "MovieId",""+movie_id, true);
    }

    private void initView() {
        ivMovie = (ImageView) findViewById(R.id.ivMovie);
        clMain = (CoordinatorLayout)findViewById(R.id.clMain);
        tvMovieTitle = (TextView)findViewById(R.id.tvMovieTitle);
        tvToolbarMovieTitle = (TextView)findViewById(R.id.tvToolbarMovieTitle);
        tvAboutMovie = (TextView)findViewById(R.id.tvAboutMovie);
        tvReleaseDate = (TextView)findViewById(R.id.tvReleaseDate);
        tvReview = (TextView)findViewById(R.id.tvReview);
    }

    private void getMovieDetails () {
        if (NetworkConnection.isNetworkAvailable (MovieDetailActivity.this)) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_loading), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_MOVIE_DETAIL+"/"+movie_id, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.GET, AppConfigURL.URL_MOVIE_DETAIL+"/"+movie_id,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        tvMovieTitle.setText(jsonObj.getString(AppConfigTags.MOVIE_TITLE));
                                        tvToolbarMovieTitle.setText(jsonObj.getString(AppConfigTags.MOVIE_TITLE));
                                        tvAboutMovie.setText(jsonObj.getString(AppConfigTags.MOVIE_OVERVIEW));
                                        tvReleaseDate.setText(jsonObj.getString(AppConfigTags.MOVIE_RELEASE_DATE));
                                        image = jsonObj.getString(AppConfigTags.MOVIE_BANNER);
                                        movie_name = jsonObj.getString(AppConfigTags.MOVIE_TITLE);
                                        Glide.with (MovieDetailActivity.this)
                                                .load (jsonObj.getString(AppConfigTags.MOVIE_BANNER))
                                                .listener (new RequestListener<String, GlideDrawable>() {
                                                    @Override
                                                    public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                        //progressBar.setVisibility (View.GONE);
                                                        return false;
                                                    }

                                                    @Override
                                                    public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                        //progressBar.setVisibility (View.GONE);
                                                        return false;
                                                    }
                                                })
                                                .into (ivMovie);
                                    } else {
                                        Utils.showSnackBar (MovieDetailActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (MovieDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (MovieDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String(response.data), true);
                            }
                            Utils.showSnackBar (MovieDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
