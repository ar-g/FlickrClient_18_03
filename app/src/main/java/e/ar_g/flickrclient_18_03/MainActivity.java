package e.ar_g.flickrclient_18_03;

import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

import e.ar_g.flickrclient_18_03.api.FlickrApi;
import e.ar_g.flickrclient_18_03.model.PhotoItem;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static e.ar_g.flickrclient_18_03.api.FlickrApi.API_KEY;

public class MainActivity extends AppCompatActivity {
  private RecyclerView recyclerView;
  private EditText etSearch;
  private CompositeDisposable compositeDisposable = new CompositeDisposable();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    recyclerView = findViewById(R.id.recyclerView);
    etSearch = findViewById(R.id.etSearch);

    GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
    recyclerView.setLayoutManager(layoutManager);

    observeTextChanges();
  }

  private void observeTextChanges() {
    Observable<String> textChangesStream = Observable.create(emitter -> {
      TextWatcher watcher = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override public void afterTextChanged(Editable s) {
          if (!emitter.isDisposed()) {
            emitter.onNext(s.toString());
          }
        }
      };
      etSearch.addTextChangedListener(watcher);

      emitter.setCancellable(() -> etSearch.removeTextChangedListener(watcher));
    });

    FlickrApi flickrApi = App.getApp(this).getFlickrApi();

    compositeDisposable.add(
      textChangesStream
        .observeOn(Schedulers.io())
        .map(query -> query.trim())
        .filter(query -> query.length() > 3)
        .debounce(500, TimeUnit.MILLISECONDS)
        .switchMap(query ->
          flickrApi.searchPhotos("flickr.photos.search", API_KEY, "json", 1, query)
        )
        .map(result -> result.getPhotos().getPhoto())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(photos -> populateAdapter(photos), throwable -> showSnackBar(throwable))
    );
  }
  private void populateAdapter(List<PhotoItem> photos) {
    Display display = getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    int imageWidth = size.x / 3;

    FeedAdapter adapter = new FeedAdapter(photos, imageWidth, new OnFeedClickListener() {
      @Override public void onFeedClick(PhotoItem photoItem) {
        Toast.makeText(MainActivity.this, photoItem.getTitle(), Toast.LENGTH_SHORT).show();
      }
    });
    recyclerView.setAdapter(adapter);
  }

  private void showSnackBar(Throwable throwable) {
    Snackbar.make(etSearch, throwable.getLocalizedMessage(), Snackbar.LENGTH_INDEFINITE)
      .setAction("Повторить запрос", v -> {
        FlickrApi flickrApi = App.getApp(this).getFlickrApi();

        compositeDisposable.add(
          flickrApi.searchPhotos("flickr.photos.search", API_KEY, "json", 1, etSearch.getText().toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(result -> result.getPhotos().getPhoto())
            .subscribe(photos -> populateAdapter(photos), t -> showSnackBar(t)));

        observeTextChanges();
      })
      .show();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    compositeDisposable.clear();
  }
}
