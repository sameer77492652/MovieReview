package actiknow.com.moviereview.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import actiknow.com.moviereview.R;
import actiknow.com.moviereview.model.Response;
import actiknow.com.moviereview.utils.Utils;

public class ResponseAdapter extends RecyclerView.Adapter<ResponseAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;

    private Activity activity;
    private ArrayList<Response> responseList = new ArrayList<Response>();
    String type;
    ProgressBar progressDialog;

    public ResponseAdapter(Activity activity, ArrayList<Response> responseList) {
        this.activity = activity;
        this.responseList = responseList;
        Utils.showLog(Log.ERROR, "responseList",""+responseList, true);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView;
        sView = mInflater.inflate(R.layout.list_item_response, parent, false);
        return new ViewHolder(sView);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final Response response = responseList.get(position);
        progressDialog = new ProgressBar(activity);
        Utils.setTypefaceToAllViews(activity, holder.tvQuestion);
        holder.tvQuestion.setText(response.getQuestion());
        holder.tvAnswer.setText(response.getAnswer());

    }




    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvQuestion;
        TextView tvAnswer;

        public ViewHolder(View view) {
            super(view);
            tvQuestion = (TextView) view.findViewById (R.id.tvQuestion);
            tvAnswer = (TextView) view.findViewById (R.id.tvAnswer);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getLayoutPosition());
        }
    }


}