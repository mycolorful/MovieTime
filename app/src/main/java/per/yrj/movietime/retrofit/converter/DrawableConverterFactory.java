package per.yrj.movietime.retrofit.converter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author yirj.
 *         on 2016/8/9
 */
public class DrawableConverterFactory extends Converter.Factory {
    private DrawableConverterFactory() {
        super();
    }

    public static DrawableConverterFactory create(){
        return new DrawableConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new BitmapResponseConverter<Bitmap>();
    }

    private class BitmapResponseConverter<T> implements Converter<ResponseBody, T>{

        @Override
        public T convert(ResponseBody value) throws IOException {
            InputStream is = value.byteStream();
            System.out.println("test"+is);
            return (T) BitmapFactory.decodeStream(is);
        }
    }

}
