package cards.seniordesign.com.cards;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Typeface blendaFont = Typeface.createFromAsset(getAssets(), "Blenda.otf");
        TextView hello = (TextView) findViewById(R.id.helloworld);
        hello.setTypeface(blendaFont);
    }

    public void goToGame(View view) {
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }



    

}
