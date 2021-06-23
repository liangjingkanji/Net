package okhttp;

import java.lang.reflect.Field;
import java.util.Map;

import okhttp3.Request;

@SuppressWarnings("KotlinInternalInJava")
public class OkHttpUtils {

    public static Map<Class<?>, Object> tags(Request.Builder builder) {
        return builder.getTags$okhttp();
    }

    public static Map<Class<?>, Object> tags(Request request) throws NoSuchFieldException, IllegalAccessException {
        Field field = request.getTags$okhttp().getClass().getDeclaredField("m");
        field.setAccessible(true);
        Object tags = field.get(request.getTags$okhttp());
        return (Map<Class<?>, Object>) tags;
    }
}
