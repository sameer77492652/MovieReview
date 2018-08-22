package actiknow.com.moviereview.activity;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import actiknow.com.moviereview.R;
import actiknow.com.moviereview.adapter.MoviesAdapter;
import actiknow.com.moviereview.dialog.MovieListDialogFragment;
import actiknow.com.moviereview.model.Movie;
import actiknow.com.moviereview.utils.AppConfigTags;
import actiknow.com.moviereview.utils.AppConfigURL;
import actiknow.com.moviereview.utils.Constants;
import actiknow.com.moviereview.utils.NetworkConnection;
import actiknow.com.moviereview.utils.SetTypeFace;
import actiknow.com.moviereview.utils.UserDetailPref;
import actiknow.com.moviereview.utils.Utils;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private SliderLayout slider;
    CoordinatorLayout clMain;
    Bundle savedInstanceState;
    RecyclerView rvNowPlayingBollywoodMovies;
    RecyclerView rvNowPlayingHollywoodMovies;
    ProgressDialog progressDialog;
    MoviesAdapter adapterBollywood;
    MoviesAdapter adapterHollywood;
    ArrayList<Movie>movieBollywoodList = new ArrayList<>();
    ArrayList<Movie>movieHollywoodList = new ArrayList<>();
    ArrayList<String>bannerList = new ArrayList<>();
    TextView tvViewAllBollywoodMovies;
    TextView tvViewAllHollywoodMovies;
    String api_response;
    UserDetailPref userDetailPref;
    private AccountHeader headerResult = null;
    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initAdapter();
        initApplication();
        initListener();
        initDrawer();
        //initLogin();
    }

    private void initLogin(){
        if(userDetailPref.getStringPref(MainActivity.this, UserDetailPref.USER_EMAIL).equalsIgnoreCase("") && userDetailPref.getStringPref(MainActivity.this, UserDetailPref.USER_NAME).equalsIgnoreCase("")){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private void initListener() {
        tvViewAllBollywoodMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                MovieListDialogFragment dialog = new MovieListDialogFragment().newInstance(api_response);
                dialog.show(ft, "movie");
            }
        });

        adapterBollywood.SetOnItemClickListener(new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                intent.putExtra(AppConfigTags.MOVIE_ID, movieBollywoodList.get(position).getMovie_id());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        adapterHollywood.SetOnItemClickListener(new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                intent.putExtra(AppConfigTags.MOVIE_ID, movieHollywoodList.get(position).getMovie_id());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }
    private void initData(){
        userDetailPref = UserDetailPref.getInstance();
        progressDialog = new ProgressDialog(this);
        clMain = (CoordinatorLayout) findViewById(R.id.clMain);

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    private void initView() {
        slider = (SliderLayout) findViewById(R.id.slider);
        rvNowPlayingBollywoodMovies = (RecyclerView) findViewById(R.id.rvNowPlayingBollywoodMovies);
        tvViewAllBollywoodMovies = (TextView) findViewById(R.id.tvViewAllBollywoodMovies);
        rvNowPlayingHollywoodMovies = (RecyclerView) findViewById(R.id.rvNowPlayingHollywoodMovies);
        tvViewAllHollywoodMovies = (TextView) findViewById(R.id.tvViewAllHollywoodMovies);
    }

    public void initAdapter(){
        adapterBollywood = new MoviesAdapter (MainActivity.this, movieBollywoodList, "Specific");
        rvNowPlayingBollywoodMovies.setAdapter (adapterBollywood);
        rvNowPlayingBollywoodMovies.setHasFixedSize (true);
        rvNowPlayingBollywoodMovies.setNestedScrollingEnabled (false);
        rvNowPlayingBollywoodMovies.setFocusable (false);
        rvNowPlayingBollywoodMovies.setLayoutManager (new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvNowPlayingBollywoodMovies.setItemAnimator (new DefaultItemAnimator());

        adapterHollywood = new MoviesAdapter (MainActivity.this, movieHollywoodList, "Specific");
        rvNowPlayingHollywoodMovies.setAdapter (adapterHollywood);
        rvNowPlayingHollywoodMovies.setHasFixedSize (true);
        rvNowPlayingHollywoodMovies.setNestedScrollingEnabled (false);
        rvNowPlayingHollywoodMovies.setFocusable (false);
        rvNowPlayingHollywoodMovies.setLayoutManager (new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvNowPlayingHollywoodMovies.setItemAnimator (new DefaultItemAnimator());
    }

    private void initApplication(){
        if (NetworkConnection.isNetworkAvailable(MainActivity.this)) {
            //Utils.showProgressDialog(progressDialog, getResources().getString(R.string.progress_dialog_text_initializing), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_INIT, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.URL_INIT,
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
                                        JSONArray jsonArrayMovies = jsonObj.getJSONArray(AppConfigTags.HINDI_MOVIES);
                                        api_response = jsonArrayMovies.toString();
                                        for(int i = 0; i < 10; i++) {
                                            JSONObject jsonObjectMovies = jsonArrayMovies.getJSONObject(i);
                                            movieBollywoodList.add(new Movie(jsonObjectMovies.getInt(AppConfigTags.ID),
                                                    jsonObjectMovies.getInt(AppConfigTags.MOVIE_ID),
                                                    jsonObjectMovies.getString(AppConfigTags.MOVIE_TITLE),
                                                    jsonObjectMovies.getString(AppConfigTags.MOVIE_POSTER_PATH),
                                                    jsonObjectMovies.getString(AppConfigTags.MOVIE_ORIGINAL_LANGUAGE),
                                                    jsonObjectMovies.getString(AppConfigTags.MOVIE_ORIGINAL_TITLE)));

                                            bannerList.add(jsonObjectMovies.getString(AppConfigTags.MOVIE_BANNER
                                            ));
                                        }
                                        JSONArray jsonArrayEnglishMovies = jsonObj.getJSONArray(AppConfigTags.ENGLISH_MOVIES);
                                        for(int i = 0; i < 10; i++) {
                                            JSONObject jsonObjectEnglishMovies = jsonArrayEnglishMovies.getJSONObject(i);
                                            movieHollywoodList.add(new Movie(jsonObjectEnglishMovies.getInt(AppConfigTags.ID),
                                                    jsonObjectEnglishMovies.getInt(AppConfigTags.MOVIE_ID),
                                                    jsonObjectEnglishMovies.getString(AppConfigTags.MOVIE_TITLE),
                                                    jsonObjectEnglishMovies.getString(AppConfigTags.MOVIE_POSTER_PATH),
                                                    jsonObjectEnglishMovies.getString(AppConfigTags.MOVIE_ORIGINAL_LANGUAGE),
                                                    jsonObjectEnglishMovies.getString(AppConfigTags.MOVIE_ORIGINAL_TITLE)));

                                        }
                                        adapterBollywood.notifyDataSetChanged();
                                        adapterHollywood.notifyDataSetChanged();


                                        initSlider();




                                    } else {
                                        Utils.showSnackBar(MainActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar(MainActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(MainActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar(MainActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put(AppConfigTags.APP_VERSION, "1.0");
                    params.put (AppConfigTags.DEVICE, "ANDROID");
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
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

    private void initSlider() {
//        slider.removeAllSliders ();
        for (int i = 0; i < 3; i++) {
            SpannableString s = new SpannableString(bannerList.get(i));
            DefaultSliderView defaultSliderView = new DefaultSliderView(this);
            defaultSliderView
                    .image(bannerList.get(i))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            defaultSliderView.bundle(new Bundle());
            defaultSliderView.getBundle().putString("url", bannerList.get(i));
            slider.addSlider(defaultSliderView);
        }

        slider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);
        slider.setPresetTransformer(SliderLayout.Transformer.Default);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(5000);
        slider.addOnPageChangeListener(this);
        slider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
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

    private void initDrawer() {
        IProfile profile = new IProfile() {
            @Override
            public Object withName(String name) {
                return null;
            }

            @Override
            public StringHolder getName() {
                return null;
            }

            @Override
            public Object withEmail(String email) {
                return null;
            }

            @Override
            public StringHolder getEmail() {
                return null;
            }

            @Override
            public Object withIcon(Drawable icon) {
                return null;
            }

            @Override
            public Object withIcon(Bitmap bitmap) {
                return null;
            }

            @Override
            public Object withIcon(@DrawableRes int iconRes) {
                return null;
            }

            @Override
            public Object withIcon(String url) {
                return null;
            }

            @Override
            public Object withIcon(Uri uri) {
                return null;
            }

            @Override
            public Object withIcon(IIcon icon) {
                return null;
            }

            @Override
            public ImageHolder getIcon() {
                return null;
            }

            @Override
            public Object withSelectable(boolean selectable) {
                return null;
            }

            @Override
            public boolean isSelectable() {
                return false;
            }

            @Override
            public Object withIdentifier(long identifier) {
                return null;
            }

            @Override
            public long getIdentifier() {
                return 0;
            }
        };

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                if (uri != null) {
                    Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
                }
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.clear(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.colorPrimary).sizeDp(56);
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_white_1000);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder(ctx, tag);
            }
        });
            headerResult = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withCompactStyle(false)
                    .withTypeface(SetTypeFace.getTypeface(MainActivity.this))
                    .withTypeface(SetTypeFace.getTypeface(this))
                    .withPaddingBelowHeader(false)
                    .withSelectionListEnabled(false)
                    .withSelectionListEnabledForSingleProfile(false)
                    .withProfileImagesVisible(false)
                    .withOnlyMainProfileImageVisible(false)
                    .withDividerBelowHeader(true)
                    .withHeaderBackground(R.color.primary_dark)
                    .withSavedInstance(savedInstanceState)
                    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                        @Override
                        public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                            //Intent intent = new Intent (MainActivity.this, MyProfileActivity.class);
                            //startActivity (intent);
                            return false;
                        }
                    })
                    .build();
            headerResult.addProfiles(new ProfileDrawerItem()
                    .withName(userDetailPref.getStringPref(MainActivity.this, UserDetailPref.USER_NAME))
                    .withEmail(userDetailPref.getStringPref(MainActivity.this, UserDetailPref.USER_EMAIL)));
            result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
//                .withToolbar (toolbar)
//                .withItemAnimator (new AlphaCrossFadeAnimator ())

                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home).withIdentifier(1).withTypeface(SetTypeFace.getTypeface(MainActivity.this)),
                        new PrimaryDrawerItem().withName("My Account").withIcon(FontAwesome.Icon.faw_audio_description).withIdentifier(2).withSelectable(false).withTypeface(SetTypeFace.getTypeface(MainActivity.this)),
                        new PrimaryDrawerItem().withName("Faqs").withIcon(FontAwesome.Icon.faw_info).withIdentifier(3).withSelectable(false).withTypeface(SetTypeFace.getTypeface(MainActivity.this)),
                        new PrimaryDrawerItem().withName("Refer").withIcon(FontAwesome.Icon.faw_phone).withIdentifier(4).withSelectable(false).withTypeface(SetTypeFace.getTypeface(MainActivity.this)),
                        new PrimaryDrawerItem().withName("Earned Points").withIcon(FontAwesome.Icon.faw_phone).withIdentifier(5).withSelectable(false).withTypeface(SetTypeFace.getTypeface(MainActivity.this)),
                        new PrimaryDrawerItem().withName("Winner List").withIcon(FontAwesome.Icon.faw_phone).withIdentifier(8).withSelectable(false).withTypeface(SetTypeFace.getTypeface(MainActivity.this)),
                        //new PrimaryDrawerItem().withName("Change Password").withIcon(FontAwesome.Icon.faw_key).withIdentifier(5).withSelectable(false).withTypeface(SetTypeFace.getTypeface(MainActivity.this)),
                        new PrimaryDrawerItem().withName("Enquiry").withIcon(FontAwesome.Icon.faw_sign_out).withIdentifier(7).withSelectable(false).withTypeface(SetTypeFace.getTypeface(MainActivity.this)),
                        new PrimaryDrawerItem().withName("Sign Out").withIcon(FontAwesome.Icon.faw_sign_out).withIdentifier(6).withSelectable(false).withTypeface(SetTypeFace.getTypeface(MainActivity.this))
                )
                .withSavedInstance(savedInstanceState)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case 3:
                                /*Intent intentContactUs = new Intent(MainActivity.this, ContactUsActivity.class);
                                startActivity(intentContactUs);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                */
                                break;


                        }
                        return false;
                    }
                })
                .build();
//        result.getActionBarDrawerToggle ().setDrawerIndicatorEnabled (false);
    }

}
