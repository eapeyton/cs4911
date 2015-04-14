package cards.seniordesign.com.cards.lobby;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;

import cards.seniordesign.com.cards.Args;
import cards.seniordesign.com.cards.R;
import cards.seniordesign.com.cards.models.User;


public class Lobby extends Activity implements AddRoomFragment.OnFragmentInteractionListener {

    private static final String TAG = "Lobby";

    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private String[] mDrawerNames;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        currentUser = getIntent().getExtras().getParcelable(Args.CURRENT_USER);

        mTitle = mDrawerTitle = getTitle();
        mDrawerNames = getResources().getStringArray(R.array.drawers);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.lobby_drawer);
        mDrawerList = (ListView) findViewById(R.id.nav_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mDrawerNames));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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

        if (savedInstanceState == null) {
            Log.i(this.getClass().getName(), "Current User Set:" + currentUser.getId());
            getFragmentManager().beginTransaction().add(R.id.content_frame, ListRoomsFragment.newInstance(currentUser)).commit();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lobby, menu);
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

    public void exitAddRoom(User currentUser) {
        this.currentUser = currentUser;
        FragmentManager fm = getFragmentManager();
        fm.popBackStackImmediate();
    }

    public void closeAddRoom() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        getActionBar().setTitle(this.getTitle());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // do something later

        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerNames[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
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

}
