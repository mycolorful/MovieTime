package per.yrj.movietime.model.retrofit;

import android.graphics.drawable.Drawable;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author yirj.
 *         on 2016/8/8
 */
public interface DrawableService {
    @GET("{url}")
    Observable<Drawable> loadBitmap(@Path("url") String url);
}
