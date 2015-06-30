package com.cluttereddesk.spotifystreamer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import kaaes.spotify.webapi.android.models.Track;

public class ArtistTracksActivity extends ActionBarActivity {

    private TrackSearchResultAdapter tracksListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_tracks);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        String artistName = this.getIntent().getStringExtra("name");
        ab.setSubtitle(artistName);

        String id = getIntent().getStringExtra("id");

        ListView tracksResults = (ListView) this.findViewById(R.id.artist_tracks_results);

        tracksListAdapter = new TrackSearchResultAdapter(this, R.layout.track_search_result, R.id.song_name);

        tracksListAdapter.clear();
        tracksListAdapter.addAll(SpotifySession.instance().getTracks());

        if (SpotifySession.instance().getTracks().size() == 0) {
            Toast noResults = Toast.makeText(this, "Spotify doesn't have a track listing for this artist.  Please choose another.", Toast.LENGTH_LONG);
            noResults.show();
        }

        tracksResults.setAdapter(tracksListAdapter);


        tracksResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent playTrackIntent = new Intent(ArtistTracksActivity.this, PlayTrackActivity.class);

                Track selected = SpotifySession.instance().getTracks().get(position);

                playTrackIntent.putExtra("name", selected.name);
                playTrackIntent.putExtra("id", selected.id);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_artist_tracks, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}