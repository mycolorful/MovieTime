package per.yrj.movietime.model.biz;

/**
 * @author yirj.
 *         on 2016/8/8
 */
public interface DataRequestListener<T> {
    void onRequest();
    void onRequestSucceed(T data);
    void onError(String msg);
}
