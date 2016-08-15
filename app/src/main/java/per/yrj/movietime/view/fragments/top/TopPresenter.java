package per.yrj.movietime.view.fragments.top;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

import per.yrj.movietime.R;
import per.yrj.movietime.listener.DataRequestListener;
import per.yrj.movietime.domain.TopMovie;
import per.yrj.movietime.view.fragments.BaseFragment;
import per.yrj.movietime.view.fragments.IBaseFragment;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author yirj.
 *         on 2016/8/10
 */
public class TopPresenter {
    private TopBiz mTopBiz;
    private IBaseFragment mBaseFragment;
    private Context mContext;
    private RecyclerView.Adapter mAdapter;

    public TopPresenter(IBaseFragment baseFragment, Context context) {
        mBaseFragment = baseFragment;
        mTopBiz = new TopBiz(context);
        mContext = context;
    }

    public void requestData() {
        Observable.create(new Observable.OnSubscribe<List<TopMovie>>() {
            @Override
            public void call(Subscriber<? super List<TopMovie>> subscriber) {
                try {
                    subscriber.onNext(mTopBiz.requestData());
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<TopMovie>>() {
                    @Override
                    public void call(List<TopMovie> topMovies) {
                        mAdapter = new MyAdapter(topMovies);
                        mBaseFragment.showLoadingState(BaseFragment.STATE_SUCCESS);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mBaseFragment.showLoadingState(BaseFragment.STATE_ERR);
                    }
                });

    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    private class MyAdapter extends RecyclerView.Adapter<TopFragment.MyViewHolder> {
        private List<TopMovie> data;

        public MyAdapter(List<TopMovie> data) {
            this.data = data;
        }

        @Override
        public TopFragment.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TopFragment.MyViewHolder holder = new TopFragment.MyViewHolder(View.inflate(mContext, R.layout.item_top_movie, null));
            return holder;
        }

        @Override
        public void onBindViewHolder(final TopFragment.MyViewHolder holder, int position) {
            TopMovie movie = data.get(position);
            holder.tvName.setText(movie.getTitle());
            holder.tvBox.setText(movie.getBox() + "");
            holder.tvRank.setText(movie.getRank() + "");
            mTopBiz.loadImage(movie.getPosterUrl(), new DataRequestListener<Bitmap>() {
                @Override
                public void onRequest() {

                }

                @Override
                public void onRequestSucceed(Bitmap data) {
                    holder.ivPoster.setImageBitmap(data);
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
