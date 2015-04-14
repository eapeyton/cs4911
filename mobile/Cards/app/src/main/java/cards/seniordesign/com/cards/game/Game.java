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
import cards.seniordesign.com.cards.models.Card;
import cards.seniordesign.com.cards.models.Room;
import cards.seniordesign.com.cards.models.User;
import cards.seniordesign.com.cards.models.response.StartGameResponse;


public class Game extends Activity implements GameplayFragment.GameplayListener {

    private Room currentRoom;
    private User currentUser;
    private boolean isHost;

    private JeezSocket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);
        currentRoom = getIntent().getExtras().getParcelable(Args.CURRENT_ROOM);
        currentUser = getIntent().getExtras().getParcelable(Args.CURRENT_USER);
        isHost = getIntent().getExtras().getBoolean(Args.IS_HOST);

        socket = new JeezSocket(this, currentUser, currentRoom);

        PreGameFragment pregame = PreGameFragment.newInstance(currentUser, currentRoom);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.content_frame, pregame).commit();
        }
    }

    public void startGame(View view) {
        socket.startGame();
    }

    public void goToGameplay(StartGameResponse response) {
        GameplayFragment gameplay = GameplayFragment.newInstance(response);
        getFragmentManager().beginTransaction().replace(R.id.content_frame, gameplay).addToBackStack(null).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.close();
    }


    @Override
    public void playCard(Card card) {
        socket.playCard(card);
    }
}
