package e.ar_g.flickrclient_18_03.api;

import e.ar_g.flickrclient_18_03.model.Result;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrApi {
    String API_KEY = "ebddd78f9a35a068bf338ec461059b5f";

    @GET("services/rest/")
    Call<Result> listRepos(
      @Query("method") String method,
      @Query("api_key") String apiKey,
      @Query("format") String format,
      @Query("nojsoncallback") int noJsonCallback
    );

    @GET("services/rest/")
    Observable<Result> searchPhotos(
      @Query("method") String method,
      @Query("api_key") String apiKey,
      @Query("format") String format,
      @Query("nojsoncallback") int noJsonCallback,
      @Query("text") String text
    );
}
