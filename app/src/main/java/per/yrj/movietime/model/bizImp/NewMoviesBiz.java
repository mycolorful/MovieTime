package per.yrj.movietime.model.bizImp;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import per.yrj.movietime.model.biz.DataRequestListener;
import per.yrj.movietime.model.biz.INewMoviesBiz;
import per.yrj.movietime.model.domain.Movie;
import per.yrj.movietime.model.http.DouBanRequest;
import per.yrj.movietime.model.retrofit.MovieService;
import retrofit2.Retrofit;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author yirj.
 *         on 2016/8/8
 */
public class NewMoviesBiz extends BaseBiz implements INewMoviesBiz {
    private static final String URL_SUFFIX = "in_theaters";

    public NewMoviesBiz(Context context) {
        super(context);
    }

    @Override
    public void requestData(final DataRequestListener listener) {
        Retrofit dbRetrofit = DouBanRequest.getInstance();
        dbRetrofit.create(MovieService.class)
                .requestMovie(URL_SUFFIX).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JSONObject>() {
                    @Override
                    public void call(JSONObject movies) {
                        listener.onRequestSucceed(parseData(movies));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listener.onError(throwable.getMessage());
                    }
                });
    }

    private List<Movie> parseData(JSONObject response) {
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
            return null;
        }
        return data;
    }

}
