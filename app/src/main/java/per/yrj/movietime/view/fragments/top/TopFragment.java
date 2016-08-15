package per.yrj.movietime.view.fragments.top;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import per.yrj.movietime.R;
import per.yrj.movietime.view.fragments.BaseFragment;


/**
 * Created by YiRenjie on 2016/5/21.
 */
public class TopFragment extends BaseFragment {
    private TopPresenter mTopPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopPresenter = new TopPresenter(this, getContext());
    }

    @Override
    public void requestData() {
        mTopPresenter.requestData();
    }


    @Override
    public View getSucceedPageView() {
        View v = View.inflate(mContext, R.layout.fra_top, null);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.rv_top);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(mTopPresenter.getAdapter());
        return v;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivPoster;
        public TextView tvName;
        public TextView tvBox;
        public TextView tvRank;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_poster);
            tvName = (TextView) itemView.findViewById(R.id.tv_movie_name);
            tvBox = (TextView) itemView.findViewById(R.id.tv_box);
            tvRank = (TextView) itemView.findViewById(R.id.iv_rank);
        }
    }
}
