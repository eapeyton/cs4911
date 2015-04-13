package cards.seniordesign.com.cards.api;

import android.app.Activity;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;

import cards.seniordesign.com.cards.Dialog;
import cards.seniordesign.com.cards.models.Room;
import cards.seniordesign.com.cards.models.User;

/**
 * Created by Eric on 4/12/15.
 */
public class JeezSocket {

    private Activity activity;
    private User currentUser;
    private Room currentRoom;
    private Socket webSocket;


    public static final String SETUP_SOCKET = "setup socket for user";
    public static final String USER_JOINED = "user joined";


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
        webSocket.connect();
        webSocket.emit(SETUP_SOCKET, JeezConverter.toJson(currentUser));
    }

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject obj = (JSONObject) args[0];
                    User user = JeezConverter.fromJson(obj, User.class);
                    Dialog.showNotification(activity, user.getName() + " has joined the room.");
                }
            });
        }
    };

    public void close() {
        webSocket.disconnect();
        webSocket.off(USER_JOINED, onUserJoined);
    }
}
