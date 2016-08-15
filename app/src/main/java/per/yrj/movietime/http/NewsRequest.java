package per.yrj.movietime.http;

import per.yrj.movietime.retrofit.converter.StringConverterFactory;
import retrofit2.Retrofit;

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
                .addConverterFactory(StringConverterFactory.create()).build();
    }
}
