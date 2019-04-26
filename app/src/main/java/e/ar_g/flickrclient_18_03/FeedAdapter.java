package e.ar_g.flickrclient_18_03;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import e.ar_g.flickrclient_18_03.model.PhotoItem;

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {
    private final List<PhotoItem> photos;
    private final int imageWidth;
    private final OnFeedClickListener listener;

    public FeedAdapter(List<PhotoItem> photos, int imageWidth, OnFeedClickListener listener) {
        this.photos = photos;
        this.imageWidth = imageWidth;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.feed_item, viewGroup, false);

        final FeedViewHolder feedViewHolder = new FeedViewHolder(view);
        feedViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int position = feedViewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onFeedClick(photos.get(position));
                }
            }
        });

        return feedViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder feedViewHolder, int i) {
        feedViewHolder.bind(photos.get(i), imageWidth);

    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}
