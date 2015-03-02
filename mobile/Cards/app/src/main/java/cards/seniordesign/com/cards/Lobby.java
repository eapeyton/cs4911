package cards.seniordesign.com.cards;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Lobby extends Activity {

    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private String[] mDrawerNames;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private int scale_size;
    private Typeface lobby_name_font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        lobby_name_font = Typeface.createFromAsset(getAssets(), "Seravek.ttc");
        scale_size = (int) getResources().getDimension(R.dimen.lobby_item_height);



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
            selectItem(0);
        }

        addSampleLobbies();

    }

    public void addSampleLobbies() {
        LinearLayout lobby_holder = (LinearLayout) findViewById(R.id.lobby_holder);

        for (String sampleText: getResources().getStringArray(R.array.lobby_samples)) {
            addSampleLobby(lobby_holder, sampleText);
        }
    }

    public void addSampleLobby(ViewGroup layout, String sampleText) {

        int splitIndex = sampleText.indexOf("\n");
        Button button = (Button) getLayoutInflater().inflate(R.layout.lobby_item, layout, false);
        Spannable span = new SpannableString(sampleText);
        span.setSpan(new StyleSpan(Typeface.BOLD), 0, splitIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(0.5f), splitIndex, sampleText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        button.setTypeface(lobby_name_font);
        button.setText(span);


        List<Bitmap> bms = new ArrayList<>();
        bms.add(getScaledBitmap(R.drawable.lobby_rikin));
        bms.add(getScaledBitmap(R.drawable.lobby_ben));
        bms.add(getScaledBitmap(R.drawable.lobby_hunter));
        bms.add(getScaledBitmap(R.drawable.lobby_eric));
        bms.add(getScaledBitmap(R.drawable.lobby_ben2));
        bms.add(getScaledBitmap(R.drawable.lobby_rikin2));
        bms.add(getScaledBitmap(R.drawable.lobby_rand1));
        bms.add(getScaledBitmap(R.drawable.lobby_rand2));
        bms.add(getScaledBitmap(R.drawable.lobby_rand3));


        Bitmap combined = Bitmap.createBitmap(scale_size * bms.size(), scale_size, bms.get(0).getConfig());
        Canvas combCanvas = new Canvas(combined);

        Collections.shuffle(bms);

        for(int i=0; i < bms.size(); i++) {
            combCanvas.drawBitmap(bms.get(i), i * scale_size, 0, null);
        }

        BitmapDrawable replace = new BitmapDrawable(getResources(), combined);
        replace.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        LayerDrawable unpressed = (LayerDrawable) getResources().getDrawable(R.drawable.lobby_item_unpressed);
        LayerDrawable pressed = (LayerDrawable) getResources().getDrawable(R.drawable.lobby_item_pressed);

        unpressed.setDrawableByLayerId(R.id.lobby_bg_img, replace);
        pressed.setDrawableByLayerId(R.id.lobby_bg_img, replace);

        StateListDrawable lobbyItemBg = new StateListDrawable();
        lobbyItemBg.addState(new int[] {-android.R.attr.state_pressed}, unpressed);
        lobbyItemBg.addState(new int[] {android.R.attr.state_pressed}, pressed);

        button.setBackgroundDrawable(lobbyItemBg);
        layout.addView(button);
    }

    public Bitmap getScaledBitmap(int drawable) {
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), drawable), scale_size, scale_size, false);
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
        return super.onOptionsItemSelected(item);
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
}
