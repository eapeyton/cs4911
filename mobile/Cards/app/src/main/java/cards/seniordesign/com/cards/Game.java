package cards.seniordesign.com.cards;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


public class Game extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        addCardButtons();

    }

    protected void addCardButtons() {
        LinearLayout layout = (LinearLayout)findViewById(R.id.card_hand);

        for (String cardText:getResources().getStringArray(R.array.cards)) {
            addCardButton(layout,cardText);
        }

    }

    protected void addCardButton(ViewGroup layout, String cardText) {
        Button button = new Button(this);

        button.setBackgroundResource(R.drawable.card_background);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int)getResources().getDimensionPixelOffset(R.dimen.max_card_size),
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f);
        int card_margin = (int) getResources().getDimension(R.dimen.card_margin);
        params.setMargins(card_margin, card_margin, card_margin, card_margin);
        button.setLayoutParams(params);
        button.setGravity(Gravity.LEFT);

        int card_padding = (int) getResources().getDimensionPixelOffset(R.dimen.card_padding);
        button.setPadding(card_padding,card_padding,card_padding,card_padding);
        button.setMaxWidth((int) getResources().getDimensionPixelOffset(R.dimen.max_card_size));
        button.setAllCaps(false);
        button.setText(cardText);

        layout.addView(button);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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
