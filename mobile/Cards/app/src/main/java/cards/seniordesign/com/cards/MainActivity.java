package cards.seniordesign.com.cards;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Typeface blendaFont = Typeface.createFromAsset(getAssets(), "Blenda.otf");
        TextView splashText = (TextView) findViewById(R.id.splash_text);
        splashText.setTypeface(blendaFont);

/*        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://ah-jeez.herokuapp.com")
                .build();

        JeezAPI api = restAdapter.create(JeezAPI.class);
        api.getCards(new Callback<List<Card>>() {
            @Override
            public void success(List<Card> cards, Response response) {
                System.out.println(cards.size());
            }

            @Override
            public void failure(RetrofitError error) {
                System.err.println(error);
            }
        });*/
    }

    public void goToGame(View view) {
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }

    public void goToLobby(View view) {
        Intent intent = new Intent(this, Lobby.class);
        startActivity(intent);
    }



    

}
