package e.ar_g.flickrclient_18_03;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import e.ar_g.flickrclient_18_03.model.PhotoItem;

class FeedViewHolder extends RecyclerView.ViewHolder {
  private ImageView ivPhoto;
  public FeedViewHolder(@NonNull View itemView) {
    super(itemView);
    ivPhoto = itemView.findViewById(R.id.ivPhoto);
  }

  public void bind(PhotoItem photoItem, int imageWidth) {
    String url = String.format(
      "https://farm%s.staticflickr.com/%s/%s_%s.jpg",
      photoItem.getFarm(),
      photoItem.getServer(),
      photoItem.getId(),
      photoItem.getSecret()
    );
    ivPhoto.getLayoutParams().width = imageWidth - ivPhoto.getPaddingLeft();
    ivPhoto.getLayoutParams().height = imageWidth - ivPhoto.getPaddingTop();
    int position = getAdapterPosition();
    int defaultPadding = (int) itemView.getContext().getResources().getDimension(R.dimen.item_padding);
    int paddingLeft = (position % 2 == 0) ? defaultPadding : 0;
    ivPhoto.setPadding(paddingLeft, defaultPadding, defaultPadding, 0);
    ivPhoto.requestLayout();

    RequestOptions options = new RequestOptions();
    options.transforms(new CenterCrop(), new RoundedCorners(32));

    Glide.with(ivPhoto)
      .load(url)
      .apply(options)
      .into(ivPhoto);
  }
}
