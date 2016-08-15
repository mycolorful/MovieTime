package per.yrj.movietime.retrofit.service;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author yirj.
 *         on 2016/8/4
 */
public interface MovieService {
    @GET("{movieType}")
    Call<JSONObject> requestMovie(@Path("movieType") String movieType);
}
