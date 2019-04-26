package e.ar_g.flickrclient_18_03;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.IOException;

import e.ar_g.flickrclient_18_03.api.FlickrApi;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
  private FlickrApi flickrApi;

  public FlickrApi getFlickrApi() {
    if (flickrApi == null) {
      OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(new Interceptor() {
          @Override public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Log.d(App.class.getSimpleName(), "URL --> " + request.url());
            Log.d(App.class.getSimpleName(), "Headers --> " + request.headers().toString());
            return chain.proceed(request);
          }
        })
        .build();

      Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://www.flickr.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

      flickrApi = retrofit.create(FlickrApi.class);
    }
    return flickrApi;
  }

  public static App getApp(Context context) {
    return ((App) context.getApplicationContext());
  }
}
