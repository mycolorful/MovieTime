package per.yrj.movietime.model.retrofit;

import android.graphics.Bitmap;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author yirj.
 *         on 2016/8/8
 */
public interface BitmapService {
    @GET
    Observable<Bitmap> loadBitmap(@Url String url);
}
