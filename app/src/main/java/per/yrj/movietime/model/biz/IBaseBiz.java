package per.yrj.movietime.model.biz;

import android.graphics.drawable.Drawable;

import java.util.List;

import per.yrj.movietime.model.domain.MovieNewsItem;

/**
 * @author yirj.
 *         on 2016/8/8
 */
public interface IBaseBiz {
    void requestData(DataRequestListener<List<MovieNewsItem>> listener);
    void loadImage(String url, DataRequestListener<Drawable> listener);
}
