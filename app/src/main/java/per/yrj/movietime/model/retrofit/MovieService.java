package per.yrj.movietime.model.retrofit;

import org.json.JSONObject;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author yirj.
 *         on 2016/8/4
 */
public interface MovieService {
    @GET("{movieType}")
    Observable<JSONObject> requestMovie(@Path("movieType") String movieType);
}
