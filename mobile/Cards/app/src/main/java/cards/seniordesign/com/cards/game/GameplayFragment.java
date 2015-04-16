package cards.seniordesign.com.cards.game;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cards.seniordesign.com.cards.R;
import cards.seniordesign.com.cards.api.JeezAPIClient;
import cards.seniordesign.com.cards.models.Card;
import cards.seniordesign.com.cards.models.response.StartGameResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameplayFragment extends Fragment {

    public static final String BLACK_CARD = "BLACK_CARD";

    private Card blackCard;
    private GameplayListener listener;

    public static GameplayFragment newInstance(Card blackCard) {
        GameplayFragment fragment = new GameplayFragment();
        Bundle args = new Bundle();
        args.putSerializable(BLACK_CARD, blackCard);
        fragment.setArguments(args);
        return fragment;
    }

    public GameplayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blackCard = (Card) getArguments().getSerializable(BLACK_CARD);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gameplay, container, false);
        addCardButtons((ViewGroup)view.findViewById(R.id.card_hand));
        showBlackCard(view, blackCard);
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

    protected void addCardButtons(final ViewGroup holder) {
        JeezAPIClient.getAPI().getHand(new Callback<List<Card>>() {
            @Override
            public void success(List<Card> cards, Response response) {
                addCardButtons(holder, cards);
            }

            @Override
            public void failure(RetrofitError error) {
                throw new RuntimeException(error);
            }
        });
    }

    protected void addCardButtons(ViewGroup holder, List<Card> cards) {
        Log.i(getClass().getName(), "Adding new cards...");
        for (Card card: cards) {
            Log.i(getClass().getName(), "Adding:" + card.getText());
            addCardButton(holder, card);
        }
    }

    protected void addCardButton(ViewGroup holder, Card whiteCard) {
        Button button = (Button)this.getActivity().getLayoutInflater().inflate(R.layout.white_card, holder, false);
        button.setText(whiteCard.getText());
        if (!listener.isJudge()) {
            button.setOnClickListener(new OnCardClick(whiteCard));
        } else {
            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.game_wcard_unpressable));
        }
        holder.addView(button);
    }

    public void showBlackCard(View view, Card blackCard) {
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
        public boolean isJudge();
    }
}
