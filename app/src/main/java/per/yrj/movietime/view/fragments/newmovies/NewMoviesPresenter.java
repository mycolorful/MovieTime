package per.yrj.movietime.view.fragments.newmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

import per.yrj.movietime.R;
import per.yrj.movietime.domain.Movie;
import per.yrj.movietime.listener.DataRequestListener;
import per.yrj.movietime.view.fragments.BaseFragment;
import per.yrj.movietime.view.fragments.IBaseFragment;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
        Observable.create(new Observable.OnSubscribe<List<Movie>>() {
            @Override
            public void call(Subscriber<? super List<Movie>> subscriber) {
                try {
                    subscriber.onNext((List<Movie>) newMoviesBiz.requestData());
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Movie>>() {
                    @Override
                    public void call(List<Movie> movies) {
                        mAdapter = new MyAdapter(movies);
                        mFragment.showLoadingState(BaseFragment.STATE_SUCCESS);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
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
