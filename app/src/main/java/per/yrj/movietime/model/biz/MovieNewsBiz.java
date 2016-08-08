package per.yrj.movietime.model.biz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import per.yrj.movietime.model.domain.MovieNewsItem;
import per.yrj.movietime.model.http.NewsRequest;
import per.yrj.movietime.model.retrofit.DrawableService;
import per.yrj.movietime.model.retrofit.JSONObjectConverterFactory;
import per.yrj.movietime.model.retrofit.NewsService;
import per.yrj.movietime.utils.ImageCacheUtil;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author yirj.
 *         on 2016/8/8
 */
public class MovieNewsBiz implements IMovieNewsBiz {
    private RequestQueue mRequestQueue;
    private ImageCacheUtil mImageCache;

    public MovieNewsBiz(Context context){
        //初始化RequestQueue
		mRequestQueue = Volley.newRequestQueue(context);
        mImageCache = new ImageCacheUtil(context, "movie_news");
    }


    @Override
    public void requestData(final DataRequestListener<List<MovieNewsItem>> listener) {

        NewsRequest.getInstance().create(NewsService.class)
                .getNews().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        //解析数据、显示页面
                        listener.onRequestSucceed(parseData(s));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listener.onError(throwable.getMessage());
                    }
                });

    }

    private List<MovieNewsItem> parseData(String response) {
        //RecyclerView所需要的数据。
        List<MovieNewsItem> data = new ArrayList<>();

        //使用正则表达式将信息从html中提取出来。
        Pattern pattern = Pattern.compile("<div class=\"news-cont\">.+?<a.+?class=\"(none)?\">");
        Matcher m = pattern.matcher(response);
        while (m.find()) {

            //提取时间
            Pattern timePat = Pattern.compile("\"news-time\">.*?<");
            Matcher timeMatcher = timePat.matcher(m.group());
            timeMatcher.find();
            MovieNewsItem item = new MovieNewsItem();
            String time = timeMatcher.group();
            time = time.split("[><]")[1];
            item.setTime(time);
//            System.out.println(time);

            //提取详细页面的链接
            Pattern hrefPat = Pattern.compile("[a-zA-z]+://[^\\s]*?.html");
            Matcher hrefMatcher = hrefPat.matcher(m.group());
            hrefMatcher.find();
            item.setHref(hrefMatcher.group());
//            System.out.println(hrefMatcher.group());

            //提取图片链接
            Pattern picPat = Pattern.compile("[a-zA-z]+://[^\\s]*?.jpg");
            Matcher picMatcher = picPat.matcher(m.group());
            picMatcher.find();
            item.setImgUrl(picMatcher.group());
//            System.out.println(picMatcher.group());

            //提取标题
            Pattern titlePat = Pattern.compile(">.{0,5}?[\\u4e00-\\u9fa5].*?</a");
            Matcher titleMatcher = titlePat.matcher(m.group());
            titleMatcher.find();
            String title = titleMatcher.group();
            title = title.substring(1, title.length() - 3);
            title = title.replace("&#183;", "·");
            title = title.replace("&quot;", "\"");
            item.setTitle(title);
//            System.out.println(title);

            //提取简介
            Pattern descPat = Pattern.compile("<p>.*?[\\u4e00-\\u9fa5].*?</p>");
            Matcher descMatcher = descPat.matcher(m.group());
            descMatcher.find();
            String desc = descMatcher.group();
            desc = desc.substring(3, desc.length() - 4);
            desc = desc.replace("&#183;", "·");
            desc = desc.replace("&quot;", "\"");
            desc = desc.split("[<>]")[2];
            item.setDesc(desc);
//            System.out.println(desc);

            data.add(item);
        }
        return data;
    }

    @Override
    public void loadImage(String url, DataRequestListener<Drawable> listener){
        new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(JSONObjectConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build()
                .create(DrawableService.class)
                .loadBitmap(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Drawable>() {
                    @Override
                    public void call(Drawable drawable) {
                        
                    }
                })
    }

}
