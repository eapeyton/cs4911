package cards.seniordesign.com.cards.api;

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
        webSocket.on(USER_HAS_PLAYED, new NotifyListener("User played card"));

        webSocket.connect();
        webSocket.emit(SETUP_SOCKET, JeezConverter.toJson(currentUser));
    }

    private ResponseListener<User> onUserJoined = new ResponseListener<User>(User.class) {
        @Override
        public void callOnUi(User response) {
            Dialog.showNotification(game, response.getName() + " has joined the room.");
        }
    };

    private ResponseListener<StartGameResponse> onGameStarted = new ResponseListener<StartGameResponse>(StartGameResponse.class) {
        @Override
        public void callOnUi(StartGameResponse response) {
            Log.i(getClass().getName(), "Object:" + JeezConverter.toJson(response).toString());
            Dialog.showNotification(game, "Game has started.");
            game.goToGameplay(response);
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
            Log.i(this.getClass().getName(), "Played card:" + card.getId());
            JSONObject cardObj =  new JSONObject();
            cardObj.put("cardId", card.getId());
            webSocket.emit(PLAY_CARD, cardObj);
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
        private String message;
        public NotifyListener(String message) {
            this.message = message;
        }

        @Override
        public void callOnUi(Object... args) {
            Dialog.showNotification(game, message);
        }
    }


}
