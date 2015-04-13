package cards.seniordesign.com.cards.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import cards.seniordesign.com.cards.Args;
import cards.seniordesign.com.cards.R;
import cards.seniordesign.com.cards.api.JeezSocket;
import cards.seniordesign.com.cards.models.Room;
import cards.seniordesign.com.cards.models.User;


public class Game extends Activity {

    private Room currentRoom;
    private User currentUser;

    private JeezSocket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);
        currentRoom = getIntent().getExtras().getParcelable(Args.CURRENT_ROOM);
        currentUser = getIntent().getExtras().getParcelable(Args.CURRENT_USER);
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
