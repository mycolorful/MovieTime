package per.yrj.movietime.model.http;

import per.yrj.movietime.model.retrofit.StringConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @author yirj.
 *         on 2016/8/8
 */
public class NewsRequest {
    private static final String MOVIE_NEWS_URL = "http://news.mtime.com/movie/";

    private NewsRequest(){

    }

    public static Retrofit getInstance(){
        return Singleton.Instance;
    }

    private static class Singleton{
        public static final Retrofit Instance = new Retrofit.Builder()
                .baseUrl(MOVIE_NEWS_URL)
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
    }
}
