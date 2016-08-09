package per.yrj.movietime.model.retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author yirj.
 *         on 2016/8/9
 */
public class StringConverterFactory extends Converter.Factory {

    private StringConverterFactory() {
        super();
    }

    public static StringConverterFactory create(){
        return new StringConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new StringConverter<String>();
    }

    private class StringConverter<T> implements Converter<ResponseBody, T>{

        @Override
        public T convert(ResponseBody value) throws IOException {
            return (T) value.string();
        }
    }

}
