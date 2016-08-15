package per.yrj.movietime.view.fragments.movienews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

import per.yrj.movietime.R;
import per.yrj.movietime.listener.DataRequestListener;
import per.yrj.movietime.domain.MovieNewsItem;
import per.yrj.movietime.view.activities.news.NewsActivity;
import per.yrj.movietime.view.fragments.BaseFragment;
import per.yrj.movietime.view.fragments.IBaseFragment;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author yirj.
 *         on 2016/8/8
 */
public class MovieNewsPresenter {
    private Context mContext;
    private RecyclerView.Adapter mAdapter;
    private IMovieNewsBiz movieNewsBiz;
    private IBaseFragment mFragment;

    public MovieNewsPresenter(IBaseFragment fragment, Context context) {
        mFragment = fragment;
        mContext = context;
        movieNewsBiz = new MovieNewsBiz(context);
    }

    public void requestData() {
        Observable.create(new Observable.OnSubscribe<List<MovieNewsItem>>() {
            @Override
            public void call(Subscriber<? super List<MovieNewsItem>> subscriber) {
                try {
                    subscriber.onNext((List<MovieNewsItem>) movieNewsBiz.requestData());
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<MovieNewsItem>>() {
                    @Override
                    public void call(List<MovieNewsItem> movieNewsItems) {
                        if (movieNewsItems != null) {
                            mAdapter = new MyAdapter(movieNewsItems);
                            mFragment.showLoadingState(BaseFragment.STATE_SUCCESS);
                        }else {
                            mFragment.showLoadingState(BaseFragment.STATE_EMPTY);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mFragment.showLoadingState(BaseFragment.STATE_ERR);
                    }
                });

    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    private class MyAdapter extends RecyclerView.Adapter<MovieNewsFragment.MyViewHolder> {
        private List<MovieNewsItem> data;

        public MyAdapter(List<MovieNewsItem> data) {
            this.data = data;
        }

        @Override
        public MovieNewsFragment.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MovieNewsFragment.MyViewHolder holder = new MovieNewsFragment.MyViewHolder(View.inflate(mContext, R.layout.item_movie_news, null));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MovieNewsFragment.MyViewHolder holder, final int position) {
            MovieNewsItem item = data.get(position);
            holder.tvTitle.setText(item.getTitle());
            holder.tvPubTime.setText(item.getTime());
            holder.tvDesc.setText(item.getDesc());
            movieNewsBiz.loadImage(item.getImgUrl(), new DataRequestListener<Bitmap>() {
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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NewsActivity.class);
                    intent.putExtra("url", data.get(position).getHref());
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

    }
}
