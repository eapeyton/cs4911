package cards.seniordesign.com.cards.game;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cards.seniordesign.com.cards.Args;
import cards.seniordesign.com.cards.R;
import cards.seniordesign.com.cards.api.JeezAPIClient;
import cards.seniordesign.com.cards.models.Card;
import cards.seniordesign.com.cards.models.Room;
import cards.seniordesign.com.cards.models.User;
import cards.seniordesign.com.cards.models.response.StartGameResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameplayFragment extends Fragment {

    public static final String START_GAME = "START_GAME";

    private StartGameResponse gameData;
    private GameplayListener listener;


    public static GameplayFragment newInstance(StartGameResponse response) {
        GameplayFragment fragment = new GameplayFragment();
        Bundle args = new Bundle();
        args.putSerializable(START_GAME, response);
        fragment.setArguments(args);
        return fragment;
    }

    public GameplayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameData = (StartGameResponse) getArguments().getSerializable(START_GAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gameplay, container, false);
        addCardButtons(view);
        setBlackCard(view, gameData.blackCard);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (GameplayListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement GameplayListener");
        }
    }

    protected void addCardButtons(View view) {
        final LinearLayout layout = (LinearLayout)view.findViewById(R.id.card_hand);

        JeezAPIClient.getAPI().getHand(new Callback<List<Card>>() {
            @Override
            public void success(List<Card> cards, Response response) {
                for (Card card: cards) {
                    addCardButton(layout, card);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                throw new RuntimeException(error);
            }
        });
    }

    protected void addCardButton(ViewGroup layout, Card whiteCard) {
        Button button = (Button)this.getActivity().getLayoutInflater().inflate(R.layout.white_card, layout, false);
        button.setText(whiteCard.getText());
        button.setOnClickListener(new OnCardClick(whiteCard));
        layout.addView(button);
    }

    public void setBlackCard(View view, Card blackCard) {
        TextView layoutCard = (TextView) view.findViewById(R.id.black_card);
        layoutCard.setText(blackCard.getText());
    }

    public class OnCardClick implements View.OnClickListener {
        private Card card;

        public OnCardClick(Card card) {
            this.card = card;
        }

        @Override
        public void onClick(View v) {
            listener.playCard(card);
        }
    }

    public interface GameplayListener {
        public void playCard(Card card);
    }
}
