package per.yrj.movietime.retrofit.service;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author yirj.
 *         on 2016/8/8
 */
public interface NewsService {
    @GET("all/index.html")
    Call<String> getNews();
}
