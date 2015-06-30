package com.cluttereddesk.spotifystreamer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Matt on 6/13/15.
 */
public class TrackSearchResultAdapter extends ArrayAdapter<Track> {
    public TrackSearchResultAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = super.getView(position, convertView, parent);
        }
        Track theTrack = this.getItem(position);

        Image theImage = null;

        if (theTrack.album.images != null && theTrack.album.images.size() > 1)
            theImage = theTrack.album.images.get(1);

        ((TextView) convertView.findViewById(R.id.album_name)).setText(theTrack.album.name);

        ((TextView) convertView.findViewById(R.id.song_name)).setText(theTrack.name);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.song_image);
        if (theImage != null) {
            Picasso.with(getContext()).load(theImage.url).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.no_image_available);
        }
        return convertView;
    }
}
