package com.cluttereddesk.spotifystreamer;

import android.app.Activity;
import android.content.Intent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Matt on 6/24/15.
 */
public class SpotifySession {

    private static SpotifySession instance = null;

    private List<Artist> artists = null;
    private List<Track> tracks = null;

    public static SpotifySession instance() {
        if (instance == null) {
            synchronized (SpotifySession.class) {
                instance = new SpotifySession();
            }
        }
        return instance;
    }


    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public void showTracksForArtist(final Activity activity, final Intent tracksIntent) {

        SpotifyApi spotifyApi = new SpotifyApi();

        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("country", "US");

        String id = tracksIntent.getStringExtra("id");

        spotifyApi.getService().getArtistTopTrack(id, searchParams, new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, Response response) {

                List<Track> trackList;

                if (tracks != null && tracks.tracks != null)
                    trackList = tracks.tracks;
                else
                    trackList = new ArrayList<Track>();

                SpotifySession.instance().setTracks(trackList);
                if (activity != null) {
                    activity.startActivity(tracksIntent);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }
}
