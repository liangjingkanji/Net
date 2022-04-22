package okhttp3;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import kotlin.jvm.JvmStatic;
import okhttp3.internal.cache.DiskLruCache;

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
        Map<Class<?>, Object> tagsOkhttp = request.getTags$okhttp();
        if (tagsOkhttp.isEmpty()) {
            Field tagsField = request.getClass().getDeclaredField("tags");
            tagsField.setAccessible(true);
            LinkedHashMap<Class<?>, Object> tags = new LinkedHashMap<>();
            tagsField.set(request, tags);
            return tags;
        }
        Field tagsField = tagsOkhttp.getClass().getDeclaredField("m");
        tagsField.setAccessible(true);
        Object tags = tagsField.get(tagsOkhttp);
        return (Map<Class<?>, Object>) tags;
    }

    /**
     * 全部的请求头
     */
    @JvmStatic
    public static Headers.Builder headers(Request.Builder builder) {
        return builder.getHeaders$okhttp();
    }

    @JvmStatic
    public static Headers.Builder addLenient(Headers.Builder builder, String line) {
        return builder.addLenient$okhttp(line);
    }

    @JvmStatic
    public static DiskLruCache diskLruCache(Cache cache) {
        return cache.getCache$okhttp();
    }
}
