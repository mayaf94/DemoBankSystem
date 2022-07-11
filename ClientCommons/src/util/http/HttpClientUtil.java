package util.http;


import okhttp3.*;

import java.util.function.Consumer;

public class HttpClientUtil {

    private final static SimpleCookieManager SimpleCookieManager = new SimpleCookieManager();
    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(SimpleCookieManager)
                    .followRedirects(false)
                    .build();

  public static void setCookieManagerLoggingFacility(Consumer<String> logConsumer) {
      SimpleCookieManager.setLogData(logConsumer);
    }

  public static void removeCookiesOf(String domain) {
      SimpleCookieManager.removeCookiesOf(domain);
    }

    public static void runAsync(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }
}
