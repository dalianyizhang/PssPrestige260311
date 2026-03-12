package utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class NetworkUtils {
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();

    public static boolean downloadFile(String url, String savePath) {
        Request request = new Request.Builder().url(url).get().build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    InputStream is = body.byteStream();
                    return FileUtils.saveInputStreamToFile(is, savePath);
                }
            } else {
                System.err.println("下载失败，HTTP状态码: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
