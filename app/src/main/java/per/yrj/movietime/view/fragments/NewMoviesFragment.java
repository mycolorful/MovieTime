package per.yrj.movietime.view.fragments;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import per.yrj.movietime.model.domain.Movie;
import per.yrj.movietime.model.http.DouBanRequest;
import per.yrj.movietime.model.retrofit.MovieService;
import per.yrj.movietime.utils.ImageCacheUtil;
import retrofit2.Retrofit;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by YiRenjie on 2016/5/31.
 * 新片上映
 */
public class NewMoviesFragment extends BaseFragment {
    private static final String URL_SUFFIX = "in_theaters";

    private MyAdapter mAdapter;

    @Override
    public void requestData() {
        Retrofit dbRetrofit = DouBanRequest.getInstance();
        dbRetrofit.create(MovieService.class)
                .requestMovie(URL_SUFFIX).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JSONObject>() {
                    @Override
                    public void call(JSONObject movies) {
                        parseData(movies);
                        showLoadingState(STATE_SUCCESS);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        showLoadingState(STATE_ERR);
                    }
                });
    }

    @Override
    public View getSucceedPageView() {
        View v = View.inflate(mContext, R.layout.fra_new_movies, null);
        RecyclerView rvIsRunMovies = (RecyclerView) v.findViewById(R.id.recycler_is_run);
        rvIsRunMovies.setAdapter(mAdapter);
        rvIsRunMovies.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        return v;
    }

    private void parseData(JSONObject response) {
        JSONArray subjects;
        List<Movie> data = new ArrayList<>();
        try {
            subjects = response.getJSONArray("subjects");
            for (int i = 0; i < subjects.length(); i++) {
                Movie movie = new Movie();
                JSONObject object = (JSONObject) subjects.get(i);
                movie.setDetailUrl(object.getString("alt"));
                movie.setPosterUrl(object.getJSONObject("images").getString("large"));
                movie.setTitle(object.getString("title"));
                data.add(movie);
//                Log.i("TAG", movie.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("TAG", e.getMessage());
        }
        mAdapter = new MyAdapter(data);
    }

    private class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Movie> data;
        private ImageCacheUtil imageCache;

        public MyAdapter(List<Movie> data) {
            this.data = data;
            imageCache = new ImageCacheUtil(mContext, "new_movies");
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder holder = new ViewHolder(View.inflate(mContext, R.layout.item_is_run_movie, null));

            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Movie movie = data.get(position);
            ImageLoader imageLoader = new ImageLoader(mRequestQueue, imageCache);
            ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(holder.ivPoster
                    , R.drawable.ic_empty_page, R.drawable.ic_error_page);
            imageLoader.get(movie.getPosterUrl(), imageListener);
            holder.tvMovieName.setText(movie.getTitle());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPoster;
        TextView tvMovieName;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_poster);
            tvMovieName = (TextView) itemView.findViewById(R.id.tv_movie_name);
        }
    }

}
