package per.yrj.movietime.model.http;

import per.yrj.movietime.model.retrofit.JSONObjectConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @author yirj.
 *         on 2016/8/4
 */
public class DouBanRequest {

    private DouBanRequest(){

    }

    public static Retrofit getInstance(){
        return Singleton.Instance;
    }

    private static class Singleton{
        public static final Retrofit Instance = new Retrofit.Builder()
                .baseUrl("http://api.douban.com/v2/movie/")
                .addConverterFactory(JSONObjectConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
    }
}
