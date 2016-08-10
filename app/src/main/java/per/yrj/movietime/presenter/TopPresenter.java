package per.yrj.movietime.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import per.yrj.movietime.R;
import per.yrj.movietime.model.biz.DataRequestListener;
import per.yrj.movietime.model.bizImp.TopBiz;
import per.yrj.movietime.model.domain.TopMovie;
import per.yrj.movietime.view.fragments.BaseFragment;
import per.yrj.movietime.view.fragments.IBaseFragment;
import per.yrj.movietime.view.fragments.TopFragment;

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
        mTopBiz.requestData(new DataRequestListener<List<TopMovie>>() {
            @Override
            public void onRequest() {

            }

            @Override
            public void onRequestSucceed(List<TopMovie> data) {
                mAdapter = new MyAdapter(data);
                mBaseFragment.showLoadingState(BaseFragment.STATE_SUCCESS);
            }

            @Override
            public void onError(String msg) {
                mBaseFragment.showLoadingState(BaseFragment.STATE_ERR);
            }
        });
    }

    public RecyclerView.Adapter getAdapter(){
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
