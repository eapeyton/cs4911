package cards.seniordesign.com.cards;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;

import cards.seniordesign.com.cards.api.JeezConverter;
import cards.seniordesign.com.cards.api.JeezSocket;
import cards.seniordesign.com.cards.models.Room;
import cards.seniordesign.com.cards.models.User;


public class Game extends Activity {

    public static final String CURRENT_ROOM = "CURRENT_ROOM";

    private Room currentRoom;
    private User currentUser;

    private JeezSocket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);
        currentRoom = getIntent().getExtras().getParcelable(CURRENT_ROOM);
        currentUser = getIntent().getExtras().getParcelable(MainActivity.CURRENT_USER);
        addCardButtons();
    }

    @Override
    protected void onStart() {
        super.onStart();
        socket = new JeezSocket(this, currentUser, currentRoom);
    }

    @Override
    protected void onStop() {
        super.onStop();
        socket.close();
    }

    protected void addCardButtons() {
        LinearLayout layout = (LinearLayout)findViewById(R.id.card_hand);

        for (String cardText:getResources().getStringArray(R.array.cards)) {
            addCardButton(layout,cardText);
        }

    }

    protected void addCardButton(ViewGroup layout, String cardText) {
        Button button = (Button)this.getLayoutInflater().inflate(R.layout.white_card,layout,false);
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
