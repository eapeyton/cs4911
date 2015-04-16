package cards.seniordesign.com.cards.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import java.util.List;

import cards.seniordesign.com.cards.Args;
import cards.seniordesign.com.cards.R;
import cards.seniordesign.com.cards.api.JeezSocket;
import cards.seniordesign.com.cards.models.Card;
import cards.seniordesign.com.cards.models.Player;
import cards.seniordesign.com.cards.models.Room;
import cards.seniordesign.com.cards.models.User;
import cards.seniordesign.com.cards.models.response.StartGameResponse;


public class Game extends Activity implements GameplayFragment.GameplayListener, JudgeFragment.JudgeListener {

    private Room currentRoom;
    private User currentUser;
    private boolean isHost;
    private boolean isJudge = false;

    private Card blackCard;

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

        PreGameFragment pregame = PreGameFragment.newInstance(currentUser, currentRoom, isHost);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.content_frame, pregame).commit();
        }
    }

    public void startGame(View view) {
        socket.startGame();
    }

    public void goToGameplay(Player judge, Card blackCard) {
        this.blackCard = blackCard;
        if (judge.getUserId().equals(currentUser.getId())) {
            isJudge = true;
        } else {
            isJudge = false;
        }
        GameplayFragment gameplayFragment = GameplayFragment.newInstance(blackCard);
        getFragmentManager().beginTransaction().replace(R.id.content_frame, gameplayFragment).addToBackStack(null).commit();
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

    public void showPlayedCards(List<Card.PlayedCard> playedCards) {
        JudgeFragment judgeFragment = JudgeFragment.newInstance(playedCards, blackCard);
        getFragmentManager().beginTransaction().replace(R.id.content_frame, judgeFragment).addToBackStack(null).commit();
    }

    @Override
    public void pickCard(Card.PlayedCard pickedCard) {
        socket.pickCard(pickedCard);
    }

    public boolean isJudge() {
        return isJudge;
    }

    public void setIsJudge(boolean isJudge) {
        this.isJudge = isJudge;
    }
}
