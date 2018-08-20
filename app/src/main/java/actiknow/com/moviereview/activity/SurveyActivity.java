package actiknow.com.moviereview.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import actiknow.com.moviereview.R;
import actiknow.com.moviereview.adapter.QuestionAdapter;
import actiknow.com.moviereview.model.Option;
import actiknow.com.moviereview.model.Question;
import actiknow.com.moviereview.utils.AppConfigTags;
import actiknow.com.moviereview.utils.AppConfigURL;
import actiknow.com.moviereview.utils.Constants;
import actiknow.com.moviereview.utils.NetworkConnection;
import actiknow.com.moviereview.utils.Utils;

public class SurveyActivity extends AppCompatActivity{
    RecyclerView rvSurveyList;
    ArrayList<Question> questionList = new ArrayList<>();
    TextView tvReview;
    TextView tvThanku;
    TextView tvShare;
    TextView tvMovieTitle;
    ProgressDialog progressDialog;
    CoordinatorLayout clMain;
    QuestionAdapter adapter;
    ShareDialog shareDialog;
    String movie_title, movie_image;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        initView();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));
        initData();
        initExtras();
        initAdapter();
        initListener();
        getQuestionList();


    }

    private void initExtras() {
        Intent intent = getIntent();
        movie_title = intent.getStringExtra(AppConfigTags.MOVIE_TITLE);
        movie_image = intent.getStringExtra(AppConfigTags.MOVIE_IMAGE);
        tvMovieTitle.setText(movie_title);

    }

    private void initListener(){
        /*tvReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvThanku.setVisibility(View.VISIBLE);
                tvShare.setVisibility(View.VISIBLE);
                tvReview.setVisibility(View.GONE);
                rvSurveyList.setVisibility(View.GONE);
            }
        });*/

        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDialog = new ShareDialog(SurveyActivity.this);
                Utils.showLog(Log.ERROR, "Share","Share", true);
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    try {
                        URL url = new URL(movie_image);
                        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        SharePhoto photo = new SharePhoto.Builder()
                                .setBitmap(image)
                                .build();
                        SharePhotoContent content = new SharePhotoContent.Builder()
                                .addPhoto(photo)
                                //.setShareHashtag("3")
                                .build();
                        shareDialog.show(content);
                    } catch(IOException e) {
                        System.out.println(e);
                    }
                    /*ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("Movie Review App")
                            .setImageUrl(Uri.parse(movie_image))
                            .setContentDescription(
                                    "This is the movie review app. You can share your review about amy movie")
                            .setContentUrl(Uri.parse("https://www.studytutorial.in/android-facebook-integration-and-login-tutorial"))
                            .build();
                    shareDialog.show(linkContent);*/  // Show facebook ShareDialog
                }

            }
        });
    }

    private void initAdapter() {
        adapter = new QuestionAdapter(SurveyActivity.this, questionList, tvReview, rvSurveyList);
        rvSurveyList.setAdapter (adapter);
        rvSurveyList.setHasFixedSize (true);
        rvSurveyList.setNestedScrollingEnabled (false);
        rvSurveyList.setFocusable (false);
        rvSurveyList.setLayoutManager (new LinearLayoutManager(SurveyActivity.this, LinearLayoutManager.VERTICAL, false));

    }

    private void initData() {
        progressDialog = new ProgressDialog(SurveyActivity.this);
    }

    private void initView() {
        rvSurveyList = (RecyclerView)findViewById(R.id.rvSurveyList);
        clMain = (CoordinatorLayout)findViewById(R.id.clMain);
        tvReview = (TextView)findViewById(R.id.tvReview);
        tvThanku = (TextView)findViewById(R.id.tvThanku);
        tvMovieTitle = (TextView)findViewById(R.id.tvMovieTitle);
        tvShare = (TextView) findViewById(R.id.tvShare);
    }

    private void getQuestionList(){
        if (NetworkConnection.isNetworkAvailable(   SurveyActivity.this)) {
            //Utils.showProgressDialog(progressDialog, getResources().getString(R.string.progress_dialog_text_initializing), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_MOVIE_QUESTION, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.GET, AppConfigURL.URL_MOVIE_QUESTION,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utils.showLog(Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean error = jsonObj.getBoolean(AppConfigTags.ERROR);
                                    String message = jsonObj.getString(AppConfigTags.MESSAGE);
                                    if (!error) {
                                        JSONArray jsonArrayQuestion = jsonObj.getJSONArray(AppConfigTags.QUESTIONS);
                                        for(int i = 0; i < jsonArrayQuestion.length(); i++) {
                                            JSONObject jsonObjectQuestion = jsonArrayQuestion.getJSONObject(i);
                                            Question question = new Question();
                                            question.setQues_id(jsonObjectQuestion.getInt(AppConfigTags.QUESTION_ID));
                                            question.setQues_text(jsonObjectQuestion.getString(AppConfigTags.QUESTION_TEXT));
                                            question.setQues_type(jsonObjectQuestion.getInt(AppConfigTags.QUESTION_TYPE));
                                            JSONArray jsonArrayOption = jsonObjectQuestion.getJSONArray(AppConfigTags.OPTIONS);
                                            ArrayList<Option> optionList = new ArrayList<>();
                                            for(int j = 0; j < jsonArrayOption.length(); j++){
                                                JSONObject jsonObjectOption = jsonArrayOption.getJSONObject(j);
                                                Option option = new Option();
                                                option.setOption_id(jsonObjectOption.getInt(AppConfigTags.OPTION_ID));
                                                option.setOption_text(jsonObjectOption.getString(AppConfigTags.OPTION_TEXT));
                                                optionList.add(option);
                                            }
                                            question.setOptions(optionList);
                                            questionList.add(question);
                                        }
                                        adapter.notifyDataSetChanged();

                                    } else {
                                        Utils.showSnackBar(SurveyActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar(SurveyActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(SurveyActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                Utils.showLog(Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss();
                            //swipeRefreshLayout.setRefreshing (false);
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // swipeRefreshLayout.setRefreshing (false);
                            progressDialog.dismiss();
                            Utils.showLog(Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString(), true);
                            Utils.showSnackBar(SurveyActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    return params;
                }

                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    // params.put (AppConfigTags.USER_LOGIN_KEY, "d958b7893874b3a6ba650d421380f5c9");
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }

            };
            Utils.sendRequest(strRequest1, 60);
        } else {
            Utils.showSnackBar(this, clMain, getResources().getString(R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_go_to_settings), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
            });
        }
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String star = intent.getStringExtra("star");
            Toast.makeText(SurveyActivity.this,star,Toast.LENGTH_SHORT).show();
            tvThanku.setVisibility(View.VISIBLE);
            tvShare.setVisibility(View.VISIBLE);
            tvReview.setVisibility(View.GONE);
            rvSurveyList.setVisibility(View.GONE);

        }
    };

}
