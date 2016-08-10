package per.yrj.movietime.model.bizImp;

import android.content.Context;
import android.graphics.Bitmap;

import per.yrj.movietime.model.biz.DataRequestListener;
import per.yrj.movietime.model.biz.IBaseBiz;
import per.yrj.movietime.model.retrofit.BitmapService;
import per.yrj.movietime.model.retrofit.DrawableConverterFactory;
import per.yrj.movietime.utils.ImageCacheUtil;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author yirj.
 *         on 2016/8/9
 */
public abstract class BaseBiz implements IBaseBiz {
    private ImageCacheUtil mImageCache;

    public BaseBiz(Context context) {
        mImageCache = new ImageCacheUtil(context, "movie_news");
    }

    @Override
    public void loadImage(final String url, final DataRequestListener<Bitmap> listener) {
        if (mImageCache.getBitmap(url) != null) {
            listener.onRequestSucceed(mImageCache.getBitmap(url));
            return;
        }
        new Retrofit.Builder()
                .baseUrl("http://www.baidu.com") // 随便设置的一个网址
                .addConverterFactory(DrawableConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build()
                .create(BitmapService.class)
                .loadBitmap(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        listener.onRequestSucceed(bitmap);
                        mImageCache.putBitmap(url, bitmap);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listener.onError(throwable.getMessage());
                    }
                });
    }
}
