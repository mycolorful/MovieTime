package per.yrj.movietime.view.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import per.yrj.movietime.R;
import per.yrj.movietime.model.domain.TopMovie;
import per.yrj.movietime.model.http.DouBanRequest;
import per.yrj.movietime.model.retrofit.MovieService;
import per.yrj.movietime.utils.ImageCacheUtil;
import retrofit2.Retrofit;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by YiRenjie on 2016/5/21.
 */
public class TopFragment extends BaseFragment {
    private static final String URL_SUFFIX = "us_box";
    private RecyclerView.Adapter mAdapter;

    @Override
    public void requestData() {

        Retrofit retrofit = DouBanRequest.getInstance();
        retrofit.create(MovieService.class).requestMovie(URL_SUFFIX)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JSONObject>() {
                    @Override
                    public void call(JSONObject jsonObject) {
                        parseData(jsonObject);
                        showLoadingState(STATE_SUCCESS);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        showLoadingState(STATE_ERR);
                    }
                });
    }

    private void parseData(JSONObject response) {
        List<TopMovie> data = new ArrayList<>();
        try {
            JSONArray jsonArray = response.getJSONArray("subjects");
            for (int i = 0; i < jsonArray.length(); i++) {
                TopMovie topMovie = new TopMovie();
                JSONObject object = (JSONObject) jsonArray.get(i);
                topMovie.setBox(object.getInt("box"));
                topMovie.setRank(object.getInt("rank"));
                JSONObject subject = object.getJSONObject("subject");
                topMovie.setDetailUrl(subject.getString("alt"));
                topMovie.setPosterUrl(subject.getJSONObject("images").getString("large"));
                topMovie.setTitle(subject.getString("title"));
                data.add(topMovie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdapter = new MyAdapter(data);
    }

    @Override
    public View getSucceedPageView() {
        View v = View.inflate(mContext, R.layout.fra_top, null);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.rv_top);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(mAdapter);
        return v;
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<TopMovie> data;
        private ImageCacheUtil imageCache;

        public MyAdapter(List<TopMovie> data) {
            this.data = data;
            imageCache = new ImageCacheUtil(mContext, "top_movies");
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(View.inflate(mContext, R.layout.item_top_movie, null));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            TopMovie movie = data.get(position);
            holder.tvName.setText(movie.getTitle());
            holder.tvBox.setText(movie.getBox() + "");
            holder.tvRank.setText(movie.getRank() + "");
            ImageLoader imageLoader = new ImageLoader(mRequestQueue, imageCache);
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.ivPoster
                    , R.drawable.ic_empty_page, R.drawable.ic_error_page);
            imageLoader.get(movie.getPosterUrl(), listener);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivPoster;
            TextView tvName;
            TextView tvBox;
            TextView tvRank;

            public MyViewHolder(View itemView) {
                super(itemView);
                ivPoster = (ImageView) itemView.findViewById(R.id.iv_poster);
                tvName = (TextView) itemView.findViewById(R.id.tv_movie_name);
                tvBox = (TextView) itemView.findViewById(R.id.tv_box);
                tvRank = (TextView) itemView.findViewById(R.id.iv_rank);
            }
        }
    }
}
