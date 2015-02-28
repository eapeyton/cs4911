package cards.seniordesign.com.cards;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Typeface blendaFont = Typeface.createFromAsset(getAssets(), "Blenda.otf");
        TextView splashText = (TextView) findViewById(R.id.splash_text);
        splashText.setTypeface(blendaFont);
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
