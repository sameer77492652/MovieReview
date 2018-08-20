package actiknow.com.moviereview.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import actiknow.com.moviereview.R;
import actiknow.com.moviereview.activity.MovieDetailActivity;
import actiknow.com.moviereview.adapter.MoviesAdapter;
import actiknow.com.moviereview.model.Movie;
import actiknow.com.moviereview.utils.AppConfigTags;
import actiknow.com.moviereview.utils.Utils;

public class MovieListDialogFragment extends DialogFragment {
    RecyclerView rvMovies;
    ArrayList<Movie> movieList = new ArrayList<> ();
    LinearLayoutManager linearLayoutManager;
    MoviesAdapter moviesAdapter;

    ImageView ivCancel;
    ImageView ivSearch;
    TextView tvTitle;
    RelativeLayout rlSearch;
    EditText etSearch;
    ProgressDialog progressDialog;
    int category_id = 0;
    CoordinatorLayout clMain;
    String response = "";

    public static MovieListDialogFragment newInstance (String response) {
        MovieListDialogFragment fragment = new MovieListDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (AppConfigTags.RESPONSE, response);
        fragment.setArguments (args);
        return fragment;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setStyle (DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    @Override
    public void onActivityCreated (Bundle arg0) {
        super.onActivityCreated (arg0);
        Window window = getDialog ().getWindow ();
        window.getAttributes ().windowAnimations = R.style.DialogAnimation;
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor (ContextCompat.getColor (getActivity (), R.color.text_color_white));
        }
    }

    @Override
    public void onResume () {
        super.onResume ();
        getDialog ().setOnKeyListener (new DialogInterface.OnKeyListener () {
            @Override
            public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction () != KeyEvent.ACTION_UP)
                        return true;
                    else {
                        if (rlSearch.getVisibility () == View.VISIBLE) {
                            final Handler handler = new Handler ();
                            handler.postDelayed (new Runnable () {
                                @Override
                                public void run () {
                                    ivSearch.setVisibility (View.VISIBLE);
                                    etSearch.setText ("");
                                }
                            }, 300);
                            final Handler handler2 = new Handler ();
                            handler2.postDelayed (new Runnable () {
                                @Override
                                public void run () {
                                    final InputMethodManager imm = (InputMethodManager) getActivity ().getSystemService (Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow (getView ().getWindowToken (), 0);
                                }
                            }, 600);
                            rlSearch.setVisibility (View.GONE);
                        } else {
                            getDialog ().dismiss ();
                        }
                        //Hide your keyboard here!!!!!!
                        return true; // pretend we've processed it
                    }
                } else
                    return false; // pass on to be processed as normal
            }
        });
    }
    
    @Override
    public void onStart () {
        super.onStart ();
        Dialog d = getDialog ();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow ().setLayout (width, height);
        }
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate (R.layout.fragment_dialog_movie_list, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        setData ();
        return root;
    }
    
    private void initView (View root) {
        clMain = (CoordinatorLayout) root.findViewById (R.id.clMain);
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        rvMovies = (RecyclerView) root.findViewById (R.id.rvMovies);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        ivSearch = (ImageView) root.findViewById (R.id.ivSearch);
        etSearch = (EditText) root.findViewById (R.id.etSearch);
        rlSearch = (RelativeLayout) root.findViewById (R.id.rlSearch);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        response = bundle.getString (AppConfigTags.RESPONSE);
    }
    
    private void initData () {
        progressDialog = new ProgressDialog(getActivity());
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
        moviesAdapter = new MoviesAdapter(getActivity(), movieList, "List");
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2, StaggeredGridLayoutManager.VERTICAL);
        rvMovies.setAdapter (moviesAdapter);
        rvMovies.setHasFixedSize (true);
        rvMovies.setLayoutManager (layoutManager);
        moviesAdapter.SetOnItemClickListener(new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra(AppConfigTags.MOVIE_ID, movieList.get(position).getMovie_id());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
    
    private void initListener () {
        /*productCategoryListAdapter.SetOnItemClickListener(new AllProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String type) {
                Intent intentFeatured = new Intent(getActivity(), ProductDetailActivity.class);
                intentFeatured.putExtra(AppConfigTags.PRODUCT_ID, categoryProductList.get(position).getProduct_id());
                startActivity(intentFeatured);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });*/




        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (rlSearch.getVisibility () == View.VISIBLE) {
                    final Handler handler = new Handler ();
                    handler.postDelayed (new Runnable () {
                        @Override
                        public void run () {
                            ivSearch.setVisibility (View.VISIBLE);
                            etSearch.setText ("");
                        }
                    }, 600);
                    final Handler handler2 = new Handler ();
                    handler2.postDelayed (new Runnable () {
                        @Override
                        public void run () {
                            final InputMethodManager imm = (InputMethodManager) getActivity ().getSystemService (Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow (getView ().getWindowToken (), 0);
                        }
                    }, 300);
                    rlSearch.setVisibility (View.GONE);
                } else {
                    getDialog ().dismiss ();
                }
            }
        });
        ivSearch.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                final Handler handler = new Handler ();
                handler.postDelayed (new Runnable () {
                    @Override
                    public void run () {
                        ivSearch.setVisibility (View.GONE);
                        etSearch.requestFocus ();
                    }
                }, 300);
                final Handler handler2 = new Handler ();
                handler2.postDelayed (new Runnable () {
                    @Override
                    public void run () {
                        final InputMethodManager imm = (InputMethodManager) getActivity ().getSystemService (Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                }, 600);
                rlSearch.setVisibility (View.VISIBLE);
            }
        });
    }
    
    private void setData () {
        try {
            JSONArray jsonArrayMovies = new JSONArray(response);
            for(int i = 0; i < jsonArrayMovies.length(); i++) {
                JSONObject jsonObjectMovies = jsonArrayMovies.getJSONObject(i);
                movieList.add(new Movie(jsonObjectMovies.getInt(AppConfigTags.ID),
                        jsonObjectMovies.getInt(AppConfigTags.MOVIE_ID),
                        jsonObjectMovies.getString(AppConfigTags.MOVIE_TITLE),
                        jsonObjectMovies.getString(AppConfigTags.MOVIE_POSTER_PATH),
                        jsonObjectMovies.getString(AppConfigTags.MOVIE_ORIGINAL_LANGUAGE),
                        jsonObjectMovies.getString(AppConfigTags.MOVIE_ORIGINAL_TITLE)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}