package actiknow.com.moviereview.adapter;

import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import actiknow.com.moviereview.R;
import actiknow.com.moviereview.model.Movie;
import actiknow.com.moviereview.utils.Utils;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;

    private Activity activity;
    private ArrayList<Movie> movieList = new ArrayList<Movie>();
    String type;
    ProgressBar progressDialog;

    public MoviesAdapter(Activity activity, ArrayList<Movie> movieList, String type) {
        this.activity = activity;
        this.type = type;
        this.movieList = movieList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView;
        switch (type){
            case "Specific":
                sView = mInflater.inflate(R.layout.list_item_movie, parent, false);
                return new ViewHolder (sView);


            case "List":
                sView = mInflater.inflate(R.layout.list_item_movie_staggered, parent, false);
                return new ViewHolder (sView);


            default:
                sView = mInflater.inflate (R.layout.list_item_movie, parent, false);
                return new ViewHolder (sView);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final Movie movie = movieList.get(position);
        progressDialog = new ProgressBar(activity);
        Utils.setTypefaceToAllViews(activity, holder.tvName);
        holder.tvName.setText(movie.getMovie_title());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            holder.ivImage.setClipToOutline(true);
        }

        Glide.with (activity)
                .load (movie.getMovie_poster_path())
                .listener (new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility (View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility (View.GONE);
                        return false;
                    }
                })
                .into (holder.ivImage);



    }




    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        ImageView ivImage;
        ProgressBar progressBar;

        public ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById (R.id.tvName);
            ivImage = (ImageView) view.findViewById (R.id.ivImage);
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getLayoutPosition());
        }
    }


}