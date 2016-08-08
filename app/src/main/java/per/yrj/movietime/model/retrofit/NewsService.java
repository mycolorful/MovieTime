package per.yrj.movietime.model.retrofit;

import retrofit2.http.GET;
import rx.Observable;

/**
 * @author yirj.
 *         on 2016/8/8
 */
public interface NewsService {
    @GET("all/index.html")
    Observable<String> getNews();
}
