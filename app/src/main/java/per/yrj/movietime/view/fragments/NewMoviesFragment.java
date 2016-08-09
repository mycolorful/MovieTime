package per.yrj.movietime.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import per.yrj.movietime.R;
import per.yrj.movietime.presenter.NewMoviesPresenter;

/**
 * Created by YiRenjie on 2016/5/31.
 * 新片上映
 */
public class NewMoviesFragment extends BaseFragment {
    private NewMoviesPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new NewMoviesPresenter(this, getContext());
    }

    @Override
    public void requestData() {
        mPresenter.requestData();
    }

    @Override
    public View getSucceedPageView() {
        View v = View.inflate(mContext, R.layout.fra_new_movies, null);
        RecyclerView rvIsRunMovies = (RecyclerView) v.findViewById(R.id.recycler_is_run);
        rvIsRunMovies.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        rvIsRunMovies.setAdapter(mPresenter.getAdapter());
        return v;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivPoster;
        public TextView tvMovieName;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_poster);
            tvMovieName = (TextView) itemView.findViewById(R.id.tv_movie_name);
        }
    }

}
