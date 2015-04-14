package cards.seniordesign.com.cards.api;

import android.app.Activity;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import cards.seniordesign.com.cards.Dialog;
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

    private Activity activity;
    private User currentUser;
    private Room currentRoom;
    private Socket webSocket;

    public JeezSocket(Activity activity, User currentUser, Room currentRoom) {
        this.activity = activity;
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
        webSocket.on(HOST_STARTED_GAME, new NotifyListener("The game has started."));
        webSocket.on(USER_NOT_HOST, new NotifyListener("The user is not the host."));
        webSocket.on(GAME_BEING_PLAYED, new NotifyListener("The game is already being played."));

        webSocket.connect();
        webSocket.emit(SETUP_SOCKET, JeezConverter.toJson(currentUser));
    }

    private ResponseListener<User> onUserJoined = new ResponseListener<User>(User.class) {
        @Override
        public void callOnUi(User response) {
            Dialog.showNotification(activity, currentUser.getName() + " has joined the room.");
        }
    };

    private ResponseListener<StartGameResponse> onGameStarted = new ResponseListener<StartGameResponse>(StartGameResponse.class) {
        @Override
        public void callOnUi(StartGameResponse response) {
            Dialog.showNotification(activity, "Game has started.");
        }
    };

    public void startGame() {
        webSocket.emit(START_GAME, new JSONObject());
    }

    public void close() {
        webSocket.disconnect();
        webSocket.off(USER_JOINED, onUserJoined);
    }

    private abstract class UiListener implements Emitter.Listener {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
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
            Dialog.showNotification(activity, message);
        }
    }


}
