package cards.seniordesign.com.cards.api;

import android.os.Handler;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import cards.seniordesign.com.cards.Dialog;
import cards.seniordesign.com.cards.game.Game;
import cards.seniordesign.com.cards.models.Card;
import cards.seniordesign.com.cards.models.Room;
import cards.seniordesign.com.cards.models.User;
import cards.seniordesign.com.cards.models.response.JudgeWaitingResponse;
import cards.seniordesign.com.cards.models.response.NewRoundResponse;
import cards.seniordesign.com.cards.models.response.RoundReviewResponse;
import cards.seniordesign.com.cards.models.response.StartGameResponse;

/**
 * Created by Eric on 4/12/15.
 */
public class JeezSocket {

    private static final String SETUP_SOCKET = "setup socket for user";
    private static final String USER_JOINED = "user joined";
    private static final String START_GAME = "start game";
    private static final String HOST_STARTED_GAME = "host started game";
    private static final String USER_NOT_HOST = "user is not host";
    private static final String GAME_BEING_PLAYED = "game is already being played";
    private static final String PLAY_CARD = "play card";
    private static final String USER_HAS_PLAYED = "user has played";
    private static final String WAITING_FOR_JUDGE = "waiting for judge";
    private static final String CHOOSE_WINNING = "choose winning card";
    private static final String ROUND_REVIEW = "round review";
    private static final String NEW_ROUND = "new round";
    private static final String GAME_REVIEW = "game review";
    private static final String PRE_GAME = "pre-game";

    private Game game;
    private User currentUser;
    private Room currentRoom;
    private Socket webSocket;

    public JeezSocket(Game game, User currentUser, Room currentRoom) {
        this.game = game;
        this.currentUser = currentUser;
        this.currentRoom = currentRoom;
        setupWebSocket();
    }

    private void setupWebSocket() {
        try {
            webSocket = IO.socket("http://ah-jeez.herokuapp.com");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        webSocket.on(USER_JOINED, onUserJoined);
        webSocket.on(HOST_STARTED_GAME, onGameStarted);
        webSocket.on(USER_NOT_HOST, new NotifyListener("The user is not the host."));
        webSocket.on(GAME_BEING_PLAYED, new NotifyListener("The game is already being played."));
        webSocket.on(USER_HAS_PLAYED, new GameNotifyListener("User played card"));
        webSocket.on(ROUND_REVIEW, onRoundReview);
        webSocket.on(NEW_ROUND, onNewRound);
        webSocket.on(GAME_REVIEW, new GameNotifyListener(GAME_REVIEW));
        webSocket.on(PRE_GAME, new GameNotifyListener(PRE_GAME));
        webSocket.on(WAITING_FOR_JUDGE, onWaitingForJudge);

        webSocket.connect();
        webSocket.emit(SETUP_SOCKET, JeezConverter.toJson(currentUser));
    }

    private ResponseListener<User> onUserJoined = new ResponseListener<User>(User.class) {
        @Override
        public void callOnUi(User response) {
            game.showPlayerJoined(response);
        }
    };

    private ResponseListener<StartGameResponse> onGameStarted = new ResponseListener<StartGameResponse>(StartGameResponse.class) {
        @Override
        public void callOnUi(StartGameResponse response) {
            game.goToGameplay(response.judge, response.blackCard);
            Dialog.showDelayedGameNotification(game, "The game has started");
        }
    };

    private ResponseListener<JudgeWaitingResponse> onWaitingForJudge = new ResponseListener<JudgeWaitingResponse>(JudgeWaitingResponse.class) {
        @Override
        public void callOnUi(JudgeWaitingResponse response) {
            game.showPlayedCards(response.playedCards);
        }
    };

    private ResponseListener<RoundReviewResponse> onRoundReview = new ResponseListener<RoundReviewResponse>(RoundReviewResponse.class) {
        @Override
        public void callOnUi(RoundReviewResponse response) {
            Dialog.showGameNotification(game, response.winner.getName() + " is the winner!");
        }
    };

    private ResponseListener<NewRoundResponse> onNewRound = new ResponseListener<NewRoundResponse>(NewRoundResponse.class) {
        @Override
        public void callOnUi(NewRoundResponse response) {
            game.goToGameplay(response.judge, response.blackCard);
            Dialog.showDelayedGameNotification(game, "New round!");
        }
    };

    public void startGame() {
        webSocket.emit(START_GAME, new JSONObject());
    }

    public void close() {
        webSocket.disconnect();
        webSocket.off(USER_JOINED, onUserJoined);
    }

    public void playCard(Card card) {
        try {
            JSONObject cardObj =  new JSONObject();
            cardObj.put("cardId", card.getId());
            webSocket.emit(PLAY_CARD, cardObj);
            Log.i(this.getClass().getName(), "Played card:" + cardObj.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void pickCard(Card.PlayedCard pickedCard) {
        try {
            JSONObject cardObj = new JSONObject();
            cardObj.put("winner", pickedCard.userId.toString());
            cardObj.put("winningCard", pickedCard.cardId.toString());
            Log.i(getClass().getName(), cardObj.toString());
            webSocket.emit(CHOOSE_WINNING, cardObj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private abstract class UiListener implements Emitter.Listener {
        @Override
        public void call(final Object... args) {
            game.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callOnUi(args);
                }
            });
        }

        public abstract void callOnUi(Object... args);
    }

    private abstract class ResponseListener<T> extends UiListener {
        private Class<T> clazz;

        public ResponseListener(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public void callOnUi(Object... args) {
            JSONObject obj = (JSONObject) args[0];
            T responseObj = JeezConverter.fromJson(obj, clazz);
            callOnUi(responseObj);
        }

        public abstract void callOnUi(T response);
    }

    private class NotifyListener extends UiListener {
        protected String message;
        public NotifyListener(String message) {
            this.message = message;
        }

        @Override
        public void callOnUi(Object... args) {
            Dialog.showNotification(game, message);
        }
    }

    private class GameNotifyListener extends NotifyListener {
        public GameNotifyListener(String message) {
            super(message);
        }

        @Override
        public void callOnUi(Object... args) {
            Dialog.showGameNotification(game, message);
        }
    }


}
