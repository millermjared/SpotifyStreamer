package com.cluttereddesk.spotifystreamer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by Matt on 6/13/15.
 */
public class ArtistSearchResultAdapter extends ArrayAdapter<Artist> {
    public ArtistSearchResultAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = super.getView(position, convertView, parent);
        }
        Artist theArtist = this.getItem(position);

        Image theImage = null;

        if (theArtist.images != null && theArtist.images.size() > 1)
            theImage = theArtist.images.get(1);

        ((TextView) convertView.findViewById(R.id.artist_name)).setText(theArtist.name);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.artist_image);
        if (theImage != null) {
            Picasso.with(getContext()).load(theImage.url).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.no_image_available);
        }
        return convertView;
    }
}
