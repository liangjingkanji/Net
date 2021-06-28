package okhttp;

import java.lang.reflect.Field;
import java.util.Map;

import kotlin.jvm.JvmStatic;
import okhttp3.Headers;
import okhttp3.Request;

@SuppressWarnings("KotlinInternalInJava")
public class OkHttpUtils {

    /**
     * 标签集合
     */
    @JvmStatic
    public static Map<Class<?>, Object> tags(Request.Builder builder) {
        return builder.getTags$okhttp();
    }

    /**
     * 通过反射返回Request的标签可变集合
     */
    @JvmStatic
    public static Map<Class<?>, Object> tags(Request request) throws NoSuchFieldException, IllegalAccessException {
        Field field = request.getTags$okhttp().getClass().getDeclaredField("m");
        field.setAccessible(true);
        Object tags = field.get(request.getTags$okhttp());
        return (Map<Class<?>, Object>) tags;
    }

    /**
     * 全部的请求头
     */
    @JvmStatic
    public static Headers.Builder headers(Request.Builder builder) {
        return builder.getHeaders$okhttp();
    }
}
