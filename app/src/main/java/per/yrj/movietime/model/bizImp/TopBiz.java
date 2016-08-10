package per.yrj.movietime.model.bizImp;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import per.yrj.movietime.model.biz.DataRequestListener;
import per.yrj.movietime.model.biz.ITopBiz;
import per.yrj.movietime.model.domain.TopMovie;
import per.yrj.movietime.model.http.DouBanRequest;
import per.yrj.movietime.model.retrofit.MovieService;
import retrofit2.Retrofit;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author yirj.
 *         on 2016/8/10
 */
public class TopBiz extends BaseBiz implements ITopBiz {
    private static final String URL_SUFFIX = "us_box";

    public TopBiz(Context context) {
        super(context);
    }

    @Override
    public void requestData(final DataRequestListener listener) {

        Retrofit retrofit = DouBanRequest.getInstance();
        retrofit.create(MovieService.class).requestMovie(URL_SUFFIX)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JSONObject>() {
                    @Override
                    public void call(JSONObject jsonObject) {
                        listener.onRequestSucceed(parseData(jsonObject));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listener.onError(throwable.getMessage());
                    }
                });
    }

    private List<TopMovie> parseData(JSONObject response) {
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
            return data;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
