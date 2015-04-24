package cards.seniordesign.com.cards;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

import java.util.Random;
import java.util.UUID;

import cards.seniordesign.com.cards.api.JeezAPIClient;
import cards.seniordesign.com.cards.game.Game;
import cards.seniordesign.com.cards.lobby.Lobby;
import cards.seniordesign.com.cards.models.User;
import retrofit.Callback;
import retrofit.RetrofitError;

public class MainActivity extends FragmentActivity {

    private static final int SPLASH = 0;
    private static final int SELECTION = 1;
    private static final int SETTINGS = 2;
    private static final int FRAGMENT_COUNT = SETTINGS +1;

    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
    private boolean isResumed = false;
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback =
            new Session.StatusCallback() {
                @Override
                public void call(Session session,
                                 SessionState state, Exception exception) {
                    onSessionStateChange(session, state, exception);
                }
            };
    private MenuItem settings;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        fragments[SPLASH] = fm.findFragmentById(R.id.splashFragment);
        fragments[SELECTION] = fm.findFragmentById(R.id.selectionFragment);
        fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);

        FragmentTransaction transaction = fm.beginTransaction();
        for (Fragment fragment: fragments) {
            transaction.hide(fragment);
        }
        transaction.commit();

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        Typeface blendaFont = Typeface.createFromAsset(getAssets(), "Blenda.otf");
        TextView splashText = (TextView) findViewById(R.id.splash_title);
        TextView selectionSplashText = (TextView) findViewById(R.id.selection_splash_title);
        splashText.setTypeface(blendaFont);
        selectionSplashText.setTypeface(blendaFont);
    }

    public void goToGame(View view) {
        goToActivity(Game.class);
    }

    public void goToLobby(View view) {
        goToActivity(Lobby.class);
    }

    public void goToEditor(View view) {
        goToActivity(Editor.class);
    }

    public void goToActivity(Class<? extends Activity> activity) {
        Intent intent = new Intent(this, activity);
        if (currentUser == null) {
            loginRandom();
            //loginWith(Session.getActiveSession());
        }
        while (currentUser == null);
        Log.i(this.getClass().getName(), "Current User Set:" + currentUser.getId());
        intent.putExtra(Args.CURRENT_USER, currentUser);
        startActivity(intent);
    }

    private void showFragment(int fragmentIndex, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (i == fragmentIndex) {
                transaction.show(fragments[i]);
            } else {
                transaction.hide(fragments[i]);
            }
        }
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
        isResumed = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        uiHelper.onPause();
        isResumed = false;
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        // Only make changes if the activity is visible
        if (isResumed) {
            FragmentManager manager = getSupportFragmentManager();
            // Get the number of entries in the back stack
            int backStackSize = manager.getBackStackEntryCount();
            // Clear the back stack
            for (int i = 0; i < backStackSize; i++) {
                manager.popBackStack();
            }
            if (state.isOpened()) {
                // If the session state is open:
                // Show the authenticated fragment
                //loginWith(session);
                loginRandom();
                goToActivity(Lobby.class);
                //showFragment(SELECTION, false);
            } else if (state.isClosed()) {
                // If the session state is closed:
                // Show the login fragment
                showFragment(SPLASH, false);
            }
        }
    }

    private void loginWith(final Session session) {
        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser graphUser, Response response) {
                        if (session == Session.getActiveSession()) {
                            if (graphUser != null) {
                                loginWith(new User(graphUser, session.getAccessToken()));
                            }
                        }
                    }
                });
        request.executeAsync();
    }

    private void loginRandom() {
        Random rand = new Random();

        User randomUser = new User();
        randomUser.setFbToken(UUID.randomUUID().toString());
        randomUser.setFbId(Integer.toString(rand.nextInt(100000) + 100000));
        randomUser.setName("FakeUser" + rand.nextInt(100000));
        randomUser.setPic("www.facebook.com/fakepic");

        loginWith(randomUser);
    }

    private void loginWith(User requestUser) {
        JeezAPIClient.setToken(requestUser.getFbToken());
        JeezAPIClient.getAPI().userLogin(requestUser, new Callback<User>() {
            @Override
            public void success(User responseUser, retrofit.client.Response response) {
                currentUser = responseUser;
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(this.getClass().getName(), error.toString());
            }
        });
    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Session session = Session.getActiveSession();

        if (session != null && session.isOpened()) {
            // if the session is already open,
            // try to show the selection fragment
            loginRandom();
            goToActivity(Lobby.class);
            //showFragment(SELECTION, false);
        } else {
            // otherwise present the splash screen
            // and ask the person to login.
            showFragment(SPLASH, false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // only add the menu when the selection fragment is showing
        if (fragments[SELECTION].isVisible()) {
            if (menu.size() == 0) {
                settings = menu.add(R.string.settings);
            }
            return true;
        } else {
            menu.clear();
            settings = null;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.equals(settings)) {
            showFragment(SETTINGS, true);
            return true;
        }
        return false;
    }
}
