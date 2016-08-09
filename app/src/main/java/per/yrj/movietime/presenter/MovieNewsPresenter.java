package per.yrj.movietime.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import per.yrj.movietime.R;
import per.yrj.movietime.model.biz.DataRequestListener;
import per.yrj.movietime.model.biz.IMovieNewsBiz;
import per.yrj.movietime.model.biz.MovieNewsBiz;
import per.yrj.movietime.model.domain.MovieNewsItem;
import per.yrj.movietime.view.activities.NewsActivity;
import per.yrj.movietime.view.fragments.BaseFragment;
import per.yrj.movietime.view.fragments.IBaseFragment;
import per.yrj.movietime.view.fragments.MovieNewsFragment;

/**
 * @author yirj.
 *         on 2016/8/8
 */
public class MovieNewsPresenter {
    private Context mContext;
    private RecyclerView.Adapter mAdapter;
    private IMovieNewsBiz movieNewsBiz;
    private IBaseFragment mFragment;

    public MovieNewsPresenter(IBaseFragment fragment,Context context) {
        mFragment = fragment;
        mContext = context;
        movieNewsBiz = new MovieNewsBiz(context);
    }

    public void requestData() {
        movieNewsBiz.requestData(new DataRequestListener<List<MovieNewsItem>>() {
            @Override
            public void onRequest() {
            }

            @Override
            public void onRequestSucceed(List<MovieNewsItem> data) {
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
