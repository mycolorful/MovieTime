package per.yrj.movietime.view.fragments.top;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import per.yrj.movietime.domain.TopMovie;
import per.yrj.movietime.http.DouBanRequest;
import per.yrj.movietime.retrofit.service.MovieService;
import per.yrj.movietime.view.fragments.BaseBiz;

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
    public List<TopMovie> requestData() throws IOException {

        JSONObject data = DouBanRequest.getInstance().create(MovieService.class)
                .requestMovie(URL_SUFFIX)
                .execute().body();
        return parseData(data);
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
