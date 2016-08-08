package per.yrj.movietime.model.biz;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import per.yrj.movietime.model.domain.MovieNewsItem;

/**
 * @author yirj.
 *         on 2016/8/8
 */
public class NewMoviesBiz implements INewMoviesBiz {
    @Override
    public void requestData(DataRequestListener<List<MovieNewsItem>> listener) {

    }

    @Override
    public void loadImage(String url, ImageLoader.ImageListener listener) {
        ImageLoader imageLoader = new ImageLoader(mRequestQueue, mImageCache);
        imageLoader.get(url, listener);
    }
}
