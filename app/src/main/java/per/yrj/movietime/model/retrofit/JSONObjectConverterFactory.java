package per.yrj.movietime.model.retrofit;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author yirj.
 *         on 2016/8/4
 */
public class JSONObjectConverterFactory extends Converter.Factory {
    private JSONObjectConverterFactory() {
        super();
    }

    public static JSONObjectConverterFactory create(){
        return new JSONObjectConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new JsonObjectResponseConverter<JSONObject>();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return super.stringConverter(type, annotations, retrofit);
    }

    private class JsonObjectResponseConverter<T> implements Converter<ResponseBody, T>{

        @Override
        public T convert(ResponseBody value) throws IOException {
            try {
                return (T) new JSONObject(value.string());
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
