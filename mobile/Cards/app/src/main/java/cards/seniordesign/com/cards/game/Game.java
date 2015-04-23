package cards.seniordesign.com.cards.game;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import java.util.List;

import cards.seniordesign.com.cards.Args;
import cards.seniordesign.com.cards.Dialog;
import cards.seniordesign.com.cards.R;
import cards.seniordesign.com.cards.api.JeezSocket;
import cards.seniordesign.com.cards.models.Card;
import cards.seniordesign.com.cards.models.Player;
import cards.seniordesign.com.cards.models.Room;
import cards.seniordesign.com.cards.models.User;
import cards.seniordesign.com.cards.models.response.NewRoundResponse;
import cards.seniordesign.com.cards.models.response.RoundReviewResponse;
import cards.seniordesign.com.cards.models.response.StartGameResponse;


public class Game extends Activity implements GameplayFragment.GameplayListener, JudgeFragment.JudgeListener {

    private Room currentRoom;
    private User currentUser;
    private boolean isHost;
    private boolean isJudge = false;
    private Card lastWinningCard;

    private Card blackCard;

    private JeezSocket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);

        if (savedInstanceState == null) {
            currentRoom = getIntent().getExtras().getParcelable(Args.CURRENT_ROOM);
            currentUser = getIntent().getExtras().getParcelable(Args.CURRENT_USER);
            isHost = getIntent().getExtras().getBoolean(Args.IS_HOST);

            socket = new JeezSocket(this, currentUser, currentRoom);

            PreGameFragment pregame = PreGameFragment.newInstance(currentUser, currentRoom, isHost);
            getFragmentManager().beginTransaction().add(R.id.content_frame, pregame).commit();
        }
    }

    public void startGame(View view) {
        socket.startGame();
    }

    public void announceWinner(Card winningCard, User winner) {
        TransitionFragment.transitionToMessage(getFragmentManager(), winningCard.getText() + " is the winner!\n" +
            winner.getName() + " earns one point.");
    }

    public void announceGameWinner(Card winningCard, User winner) {
        TransitionFragment.transitionToMessage(getFragmentManager(), winningCard.getText() + " is the winner!\n" +
            winner.getName() + " wins the game! ");
    }

    public void goToGameplay(Card blackCard, Player judge) {
        this.blackCard = blackCard;

        GameplayFragment gameplayFragment = GameplayFragment.newInstance(blackCard);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.fade_in_long, R.animator.fade_out_long)
                .replace(R.id.content_frame, gameplayFragment)
                .commit();

        if (judge.getUserId().equals(currentUser.getId())) {
            isJudge = true;
            Dialog.showDelayedGameNotification(this, "New round! You are the new judge.");
        } else {
            Dialog.showDelayedGameNotification(this, "New round! " + judge.getName() + " is the new judge.");
            isJudge = false;
        }
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
        TransitionFragment.transitionToFragment(getFragmentManager(), judgeFragment, "Everyone has played a card.\nNow the judge will pick his favorite.");
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

    public void showPlayerJoined(User player) {
        Dialog.showGameNotification(this, player.getName() + " has joined.");
    }

    public void setLastWinningCard(Card lastWinningCard) {
        this.lastWinningCard = lastWinningCard;
    }
}
