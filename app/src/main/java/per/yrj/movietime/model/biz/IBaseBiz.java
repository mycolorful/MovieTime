package per.yrj.movietime.model.biz;

import android.graphics.Bitmap;

/**
 * @author yirj.
 *         on 2016/8/8
 */
public interface IBaseBiz {
    void requestData(DataRequestListener listener);
    void loadImage(String url, DataRequestListener<Bitmap> listener);
}
