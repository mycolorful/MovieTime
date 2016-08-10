package per.yrj.movietime.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import per.yrj.movietime.R;
import per.yrj.movietime.model.biz.DataRequestListener;
import per.yrj.movietime.model.biz.INewMoviesBiz;
import per.yrj.movietime.model.bizImp.NewMoviesBiz;
import per.yrj.movietime.model.domain.Movie;
import per.yrj.movietime.view.fragments.BaseFragment;
import per.yrj.movietime.view.fragments.IBaseFragment;
import per.yrj.movietime.view.fragments.NewMoviesFragment;

/**
 * @author yirj.
 *         on 2016/8/9
 */
public class NewMoviesPresenter {
    private Context mContext;
    private RecyclerView.Adapter mAdapter;
    private INewMoviesBiz newMoviesBiz;
    private IBaseFragment mFragment;

    public NewMoviesPresenter(IBaseFragment fragment,Context context) {
        mFragment = fragment;
        mContext = context;
        newMoviesBiz = new NewMoviesBiz(context);
    }

    public void requestData() {
        newMoviesBiz.requestData(new DataRequestListener<List<Movie>>() {
            @Override
            public void onRequest() {
            }

            @Override
            public void onRequestSucceed(List<Movie> data) {
                mAdapter = new MyAdapter(data);
                mFragment.showLoadingState(BaseFragment.STATE_SUCCESS);
            }

            @Override
            public void onError(String msg) {
                mFragment.showLoadingState(BaseFragment.STATE_ERR);
            }
        });

    }

    public RecyclerView.Adapter getAdapter(){
        return mAdapter;
    }

    private class MyAdapter extends RecyclerView.Adapter<NewMoviesFragment.ViewHolder> {
        private List<Movie> data;

        public MyAdapter(List<Movie> data) {
            this.data = data;
        }

        @Override
        public NewMoviesFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            NewMoviesFragment.ViewHolder holder = new NewMoviesFragment.ViewHolder(View.inflate(mContext, R.layout.item_is_run_movie, null));
            return holder;
        }

        @Override
        public void onBindViewHolder(final NewMoviesFragment.ViewHolder holder, int position) {
            Movie movie = data.get(position);
            holder.tvMovieName.setText(movie.getTitle());
            newMoviesBiz.loadImage(movie.getPosterUrl(), new DataRequestListener<Bitmap>() {
                @Override
                public void onRequest() {

                }

                @Override
                public void onRequestSucceed(Bitmap bitmap) {
                    holder.ivPoster.setImageBitmap(bitmap);
                }

                @Override
                public void onError(String msg) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }


}
