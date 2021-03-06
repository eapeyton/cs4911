package cards.seniordesign.com.cards.lobby;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.UserSettingsFragment;

import cards.seniordesign.com.cards.Args;
import cards.seniordesign.com.cards.CreateCardFragment;
import cards.seniordesign.com.cards.MainActivity;
import cards.seniordesign.com.cards.PreCreateCardFragment;
import cards.seniordesign.com.cards.R;
import cards.seniordesign.com.cards.game.Game;
import cards.seniordesign.com.cards.models.Room;
import cards.seniordesign.com.cards.models.User;
import cards.seniordesign.com.cards.Editor;


public class Lobby extends FragmentActivity implements AddRoomFragment.AddRoomListener, ListRoomsFragment.ListRoomsListener, CreateCardFragment.CreateCardListener {

    private static final String TAG = "Lobby";

    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private String[] mDrawerNames;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private User currentUser;

    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback =
            new Session.StatusCallback() {
                @Override
                public void call(Session session,
                                 SessionState state, Exception exception) {
                    onSessionStateChange(session, state, exception);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        currentUser = getIntent().getExtras().getParcelable(Args.CURRENT_USER);

        mTitle = mDrawerTitle = getTitle();
        mDrawerNames = Drawers.getDrawerTitles();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.lobby_drawer);
        mDrawerList = (ListView) findViewById(R.id.nav_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mDrawerNames));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setItemChecked(0, true);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Log.i(this.getClass().getName(), "Current User Set:" + currentUser.getId());
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_frame, ListRoomsFragment.newInstance(currentUser))
                    .commit();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mDrawerList.getCheckedItemPosition() == 0) {
            getMenuInflater().inflate(R.menu.menu_lobby, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_add_room:
                openAddRoom();
                return true;
            case android.R.id.home:
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openAddRoom() {
        Fragment addRoomFragment = AddRoomFragment.newInstance(currentUser);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, addRoomFragment).addToBackStack(null).commit();

        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        getActionBar().setTitle("New room");
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        selectItem(Drawers.values()[position]);
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void selectItem(Drawers drawerItem) {
        setTitle(drawerItem.drawerTitle);
        switch (drawerItem) {
            case Lobby:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, ListRoomsFragment.newInstance(currentUser))
                        .addToBackStack(null)
                        .commit();
                break;
            case Editor:
                //invalidateOptionsMenu();
                setTitle("Which color?");
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, PreCreateCardFragment.newInstance(currentUser))
                        .addToBackStack(null)
                        .commit();
                break;
            case Settings:
                break;
            case LogOut:
                //invalidateOptionsMenu();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new UserSettingsFragment())
                        .addToBackStack(null)
                        .commit();

                break;
        }
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    public void showSizePicker(View view) {
        final NumberPicker picker = (NumberPicker) getLayoutInflater().inflate(R.layout.size_picker, null);
        picker.setMinValue(AddRoomFragment.LOWER_SIZE);
        picker.setMaxValue(AddRoomFragment.UPPER_SIZE);
        picker.setWrapSelectorWheel(false);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Room Size:")
                .setView(picker)
                .setPositiveButton("Set",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Button button = (Button) findViewById(R.id.add_room_size);
                                button.setText(Integer.toString(picker.getValue()));
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                .create();
        dialog.show();
    }

    public void goToGameAsHost(Room room, User currentUser) {
        Intent intent = new Intent(this, Game.class);
        intent.putExtra(Args.IS_HOST, true);
        goToGameRoom(intent, room, currentUser);
    }

    public void goToGameAsGuest(Room room, User currentUser) {
        Intent intent = new Intent(this, Game.class);
        intent.putExtra(Args.IS_HOST, false);
        goToGameRoom(intent, room, currentUser);
    }

    private void goToGameRoom(Intent intent, Room room, User currentUser) {
        intent.putExtra(Args.CURRENT_ROOM, room);
        goToActivity(intent, currentUser);
    }

    private void goToActivity(Intent intent, User currentUser) {
        intent.putExtra(Args.CURRENT_USER, currentUser);
        startActivity(intent);
    }

    public enum Drawers {
        Lobby ("Lobby"),
        Editor ("Editor"),
        Settings ("Settings"),
        LogOut ("Log Out");

        private String drawerTitle;

        private Drawers(String drawerTitle) {
            this.drawerTitle = drawerTitle;
        }

        public static String[] getDrawerTitles() {
            String[] titles = new String[Drawers.values().length];
            Drawers[] drawers = Drawers.values();
            for (int i=0; i < titles.length; i++) {
                titles[i] = drawers[i].drawerTitle;
            }
            return titles;
        }
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isClosed()) {
            Intent intent = new Intent(this, MainActivity.class);
            goToActivity(intent, currentUser);
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
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    public void exitCreateCard(EditText editor) {
        getFragmentManager().popBackStackImmediate();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editor.getWindowToken(), 0);
        getActionBar().setTitle("Which Color?");
    }

    public void closeCreateCard() {
        getActionBar().setTitle(getTitle());
    }

}
