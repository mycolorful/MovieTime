package per.yrj.movietime.view.fragments.movienews;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import per.yrj.movietime.domain.MovieNewsItem;
import per.yrj.movietime.http.NewsRequest;
import per.yrj.movietime.retrofit.service.NewsService;
import per.yrj.movietime.view.fragments.BaseBiz;

/**
 * @author yirj.
 *         on 2016/8/8
 */
public class MovieNewsBiz extends BaseBiz implements IMovieNewsBiz {

    public MovieNewsBiz(Context context){
        super(context);
    }

    @Override
    public List<MovieNewsItem> requestData() throws IOException {
        String response = NewsRequest.getInstance().create(NewsService.class)
                    .getNews().execute().body();

        return parseData(response);

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


}
