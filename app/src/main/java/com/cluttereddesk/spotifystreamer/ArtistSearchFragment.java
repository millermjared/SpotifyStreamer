package com.cluttereddesk.spotifystreamer;

import android.content.Intent;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ArtistSearchFragment extends Fragment {

    private static final String LOG_TAG = "ArtistSearchFragment";

    public ArtistSearchFragment() {
    }

    private ArtistSearchResultAdapter artistListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragView = inflater.inflate(R.layout.fragment_main, container, false);


        ListView searchResults = (ListView) fragView.findViewById(R.id.artist_search_results);

        artistListAdapter = new ArtistSearchResultAdapter(this.getActivity(), R.layout.individual_artist_search_result, R.id.artist_name);
        searchResults.setAdapter(artistListAdapter);

        if (SpotifySession.instance().getArtists() != null) {
            artistListAdapter.addAll(SpotifySession.instance().getArtists());
        }

        searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent tracksIntent = new Intent(getActivity(), ArtistTracksActivity.class);

                Artist selected = SpotifySession.instance().getArtists().get(position);

                tracksIntent.putExtra("name", selected.name);
                tracksIntent.putExtra("id", selected.id);

                SpotifySession.instance().showTracksForArtist(getActivity(), tracksIntent);
            }
        });


        EditText searchBox = (EditText) fragView.findViewById(R.id.search_box);

        searchBox.addTextChangedListener(new TextWatcher() {

            Timer timer = null;

            public void afterTextChanged(Editable s) {

                if (timer != null) {
                    timer.cancel();
                }

                timer = new Timer();

                final String artistName = s.toString();
                //delay the search by half a second to allow complete entry of the search term
                try {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            searchSpotify(artistName);
                        }
                    }, 500);
                } catch (IllegalStateException ise) {
                    //the timer was cancelled by other user input, so don't run this search.
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void searchSpotify(String artistName) {
                if (!"".equals(artistName.trim())) {
                    SpotifyApi spotifyApi = new SpotifyApi();
                    spotifyApi.getService().searchArtists(artistName, new Callback<ArtistsPager>() {
                        @Override
                        public void success(ArtistsPager artistsPager, Response response) {


                            List<Artist> artistList;

                            if (artistsPager != null && artistsPager.artists != null && artistsPager.artists.items != null)
                                artistList = artistsPager.artists.items;
                            else
                                artistList = new ArrayList<Artist>();

                            SpotifySession.instance().setArtists(artistList);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    artistListAdapter.clear();
                                    if (SpotifySession.instance().getArtists().size() > 0) {
                                        artistListAdapter.addAll(SpotifySession.instance().getArtists());
                                    } else {
                                        Toast noResults = Toast.makeText(getActivity(), "No artist matches your criteria, please refine the search.", Toast.LENGTH_SHORT);
                                        noResults.show();
                                    }
                                }
                            });


                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }
            }
        });


        return fragView;


    }

}
