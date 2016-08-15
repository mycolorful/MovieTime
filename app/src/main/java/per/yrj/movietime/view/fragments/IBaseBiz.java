package per.yrj.movietime.view.fragments;

import android.graphics.Bitmap;

import java.io.IOException;

import per.yrj.movietime.listener.DataRequestListener;

/**
 * @author yirj.
 *         on 2016/8/8
 */
public interface IBaseBiz {
    Object requestData() throws IOException;
    void loadImage(String url, DataRequestListener<Bitmap> listener);
}
