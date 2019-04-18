package e.ar_g.flickrclient_18_03;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import e.ar_g.flickrclient_18_03.model.Result;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

  private final String API_KEY = "ebddd78f9a35a068bf338ec461059b5f";
  private final Executor executor = Executors.newSingleThreadExecutor();
  private final Handler handler = new Handler(Looper.getMainLooper());
  private Runnable displayResult;
  private TextView tvText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    tvText = findViewById(R.id.tvText);
    getPhotosViaOkHttp();
  }

  private void getPhotosViaHttpURLConnection() {
    executor.execute(new Runnable() {
      @Override
      public void run() {
        InputStream inputStream = null;
        try {
          URL url = new URL("https://www.flickr.com/services/rest/?method=flickr.photos.getRecent&"+
            "api_key="+ API_KEY +"&"+
            "format=json&"+
            "nojsoncallback=1");
          HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
          inputStream = urlConnection.getInputStream();
          final String json = getStringFormatInputStream(inputStream);

          deserializeAndShow(json);
        } catch (MalformedURLException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }  finally {
          if (inputStream != null) {
            try {
              inputStream.close();
            } catch (IOException ignored) {}
          }

        }
      }
    });
  }

  private void deserializeAndShow(String json) {
    Gson gson = new Gson();
    Result result = gson.fromJson(json, Result.class);

    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < result.getPhotos().getPhoto().size() && i < 5; i++) {
      builder.append(result.getPhotos().getPhoto().get(i).getTitle());
      builder.append("---");
    }

    final String titles = builder.toString();

    displayResult = new Runnable() {
      @Override
      public void run() {
        tvText.setText(titles);
      }
    };
    handler.post(displayResult);
  }

  private void getPhotosViaOkHttp() {
    final OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
      .url("https://www.flickr.com/services/rest/?"+
        "method=flickr.photos.getRecent&"+
        "api_key="+API_KEY +"&"+
        "format=json&"+
        "nojsoncallback=1")
      .get()
      .build();

    client.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(Call call, IOException e) {
        e.printStackTrace();
      }

      @Override public void onResponse(Call call, Response response) throws IOException {
        ResponseBody body = response.body();
        deserializeAndShow(body.string());
      }
    });

  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (displayResult != null) {
      handler.removeCallbacks(displayResult);
    }
  }

  public static String getStringFormatInputStream(InputStream stream) throws IOException {
    int n = 0;
    char[] buffer = new char[1024 * 4];
    InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
    StringWriter writer = new StringWriter();
    while(-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
    return writer.toString();
  }
}
