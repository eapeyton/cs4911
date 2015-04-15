package cards.seniordesign.com.cards.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

        socket = new JeezSocket(this, currentUser, currentRoom);

        PreGameFragment pregame = PreGameFragment.newInstance(currentUser, currentRoom);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.content_frame, pregame).commit();
        }
    }

    public void goToGameplay(View view) {
        GameplayFragment gameplay = GameplayFragment.newInstance(currentUser, currentRoom);
        getFragmentManager().beginTransaction().replace(R.id.content_frame, gameplay).addToBackStack(null).commit();
        socket.startGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.close();
    }

}
