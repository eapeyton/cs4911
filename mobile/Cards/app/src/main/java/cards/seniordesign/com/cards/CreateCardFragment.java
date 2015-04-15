package cards.seniordesign.com.cards;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInstaller;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;

import cards.seniordesign.com.cards.api.JeezAPIClient;
import cards.seniordesign.com.cards.models.Card;
import cards.seniordesign.com.cards.models.User;
import cards.seniordesign.com.cards.models.response.AddCardResponse;

import retrofit.RetrofitError;
import retrofit.Callback;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateCardFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private EditText editor;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateCardFragment newInstance(User currentUser, Card.CardType cType) {
        CreateCardFragment fragment = new CreateCardFragment();
        Bundle args = new Bundle();
        args.putParcelable(Args.CURRENT_USER, currentUser);
        args.putSerializable("CARD_TYPE", cType);
        fragment.setArguments(args);
        return fragment;
    }

    public CreateCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_card, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();

        // set the background to black if need be
        Card.CardType currCardType = (Card.CardType) getArguments().getSerializable("CARD_TYPE");

        if (currCardType == Card.CardType.BLACK) {
            EditText newTextView = (EditText)view.findViewById(R.id.new_card_text);
            newTextView.setBackgroundColor(Color.BLACK);
            newTextView.setTextColor(Color.WHITE);
        }

        // set up a listener on the text field
        editor = (EditText) view.findViewById(R.id.new_card_text);
        editor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                // Reached the end of the form
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addCard();
                    handled = true;
                }
                return handled;
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.closeCreateCard();
        mListener = null;
    }

    private void addCard() {
        View view = getView();
        editor = (EditText) view.findViewById(R.id.new_card_text);
        User currUser = getArguments().getParcelable(Args.CURRENT_USER);
        Card.CardType currCardType = (Card.CardType) getArguments().getSerializable("CARD_TYPE");

        Card newCard = new Card();
        newCard.setText(editor.getText().toString());
        newCard.setType(currCardType);
        newCard.setUserId(currUser.getId());

        // Make api call
        JeezAPIClient.getAPI().addCard(newCard, new Callback<AddCardResponse>() {
            @Override
            public void success(AddCardResponse addRoomResponse, Response response) {
                Log.i("AddCard", "Card added successfully");
                mListener.exitCreateCard(editor);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("AddCard", error.getResponse().toString());
                showErrorDialog("Failed to add card.");
            }
        });
    }

    private void showErrorDialog(String s) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage(s)
                .setNeutralButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                .create();
        dialog.show();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void exitCreateCard(EditText editor);
        public void closeCreateCard();
    }
}
