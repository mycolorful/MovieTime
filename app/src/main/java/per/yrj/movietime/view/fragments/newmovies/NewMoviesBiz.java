package per.yrj.movietime.view.fragments.newmovies;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import per.yrj.movietime.domain.Movie;
import per.yrj.movietime.http.DouBanRequest;
import per.yrj.movietime.retrofit.service.MovieService;
import per.yrj.movietime.view.fragments.BaseBiz;

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
    public List<Movie> requestData() throws IOException {
        JSONObject data = DouBanRequest.getInstance().create(MovieService.class)
                .requestMovie(URL_SUFFIX).execute().body();
        return parseData(data);
    }

    private List<Movie> parseData(JSONObject response) {
        if (response == null) {
            return null;
        }
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
