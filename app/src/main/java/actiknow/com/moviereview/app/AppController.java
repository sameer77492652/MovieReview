package actiknow.com.moviereview.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class AppController extends Application {

	public static final String TAG = AppController.class.getSimpleName();
	private static AppController mInstance;
	private static Context context;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	public static synchronized AppController getInstance () {
		return mInstance;
	}

	public static Context getAppContext () {
		return AppController.context;
	}
    
    @Override
    protected void attachBaseContext (Context context) {
        super.attachBaseContext (context);
        MultiDex.install (this);
	
    }
    
    @Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		AppController.context = getApplicationContext ();
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public <T> void addToRequestQueue(StringRequest req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

}