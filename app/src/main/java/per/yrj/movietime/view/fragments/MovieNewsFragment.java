package per.yrj.movietime.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import per.yrj.movietime.R;
import per.yrj.movietime.presenter.MovieNewsPresenter;
import per.yrj.movietime.view.recyclerview.DividerItemDecoration;

/**
 * Created by YiRenjie on 2016/5/21.
 * 影讯
 */
public class MovieNewsFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private MovieNewsPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MovieNewsPresenter(this, getContext());
    }

    @Override
    public void requestData() {
        mPresenter.requestData();
    }

    @Override
    public View getSucceedPageView() {
        View v = View.inflate(mContext, R.layout.fra_movie_news, null);
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_movie_news);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(mPresenter.getAdapter());
        return v;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvPubTime, tvDesc;
        public ImageView ivPoster;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvPubTime = (TextView) itemView.findViewById(R.id.tv_pub_time);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_poster);
        }

    }
}
