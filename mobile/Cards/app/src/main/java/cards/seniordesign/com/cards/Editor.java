package cards.seniordesign.com.cards;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import cards.seniordesign.com.cards.models.Card;
import cards.seniordesign.com.cards.models.User;


public class Editor extends Activity implements CreateCardFragment.OnFragmentInteractionListener, View.OnClickListener {
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = getIntent().getExtras().getParcelable(Args.CURRENT_USER);

        if (savedInstanceState == null) {
            setContentView(R.layout.activity_editor);

            Button whiteButton = (Button)findViewById(R.id.create_white_card);
            whiteButton.setOnClickListener(this);
            Button blackButton = (Button)findViewById(R.id.create_black_card);
            blackButton.setOnClickListener(this);
            getActionBar().setTitle("Which Color?");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        Card.CardType ctype = Card.CardType.BLACK;
        if (v.getId() == R.id.create_white_card) {
            ctype = Card.CardType.WHITE;
        }
        Fragment createCardFragment = CreateCardFragment.newInstance(currentUser, ctype);
        getActionBar().setTitle("Set the text");
        getFragmentManager().beginTransaction()
                .add(R.id.container, createCardFragment)
                .addToBackStack(null)
                .commit();
    }

    public void exitCreateCard(EditText editor) {
        getFragmentManager().popBackStackImmediate();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editor.getWindowToken(), 0);
        getActionBar().setTitle("Which Color?");
    }

    public void closeCreateCard() {
        getActionBar().setTitle(this.getTitle());
    }
}
