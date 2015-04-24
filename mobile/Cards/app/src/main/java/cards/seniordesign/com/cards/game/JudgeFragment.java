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

import java.util.ArrayList;
import java.util.List;

import cards.seniordesign.com.cards.R;
import cards.seniordesign.com.cards.models.Card;

/**
 * A simple {@link Fragment} subclass.
 */
public class JudgeFragment extends Fragment {

    private static final String PLAYED_CARDS = "PLAYED_CARDS";
    private static final String BLACK_CARD = "BLACK_CARD";

    private List<Card.PlayedCard> playedCards;
    private Card blackCard;
    private JudgeListener listener;

    public static JudgeFragment newInstance(List<Card.PlayedCard> playedCards, Card blackCard) {
        ArrayList<Card.PlayedCard> cards = new ArrayList<Card.PlayedCard>(playedCards);

        JudgeFragment fragment = new JudgeFragment();
        Bundle args = new Bundle();
        args.putSerializable(PLAYED_CARDS, cards);
        args.putSerializable(BLACK_CARD, blackCard);
        fragment.setArguments(args);
        return fragment;
    }

    public JudgeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playedCards = (List<Card.PlayedCard>) getArguments().getSerializable(PLAYED_CARDS);
        blackCard = (Card) getArguments().getSerializable(BLACK_CARD);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_judge, container, false);
        if (listener.isJudge()) {
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.game_border));
        }
        showBlackCard(view);
        ViewGroup holder = (ViewGroup) view.findViewById(R.id.card_hand);
        showPlayedCardsOn(holder);
        showTitle(view);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (JudgeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement JudgeListener");
        }
    }

    public void showBlackCard(View view) {
        TextView layoutCard = (TextView) view.findViewById(R.id.black_card);
        layoutCard.setText(blackCard.getText());
    }

    private void showPlayedCardsOn(ViewGroup holder) {
        for (Card.PlayedCard playedCard: playedCards) {
            showCard(holder, playedCard);
        }
    }

    protected void showCard(ViewGroup holder, Card.PlayedCard playedCard) {
        Button button = (Button)this.getActivity().getLayoutInflater().inflate(R.layout.white_card, holder, false);
        button.setText(playedCard.Card.getText());
        if (listener.isJudge()) {
            button.setOnClickListener(new OnJudgeClick(playedCard));
        } else {
            int padding = button.getPaddingTop();
            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.game_wcard_unpressable));
            button.setPadding(padding, padding, padding, padding);
        }
        holder.addView(button);
    }

    private void showTitle(View view) {
        TextView title = (TextView) view.findViewById(R.id.judge_title);
        if (listener.isJudge()) {
            title.setText("Pick a winner");
        } else {
            title.setText("Played Cards");
        }
    }

    private class OnJudgeClick implements View.OnClickListener {
        private Card.PlayedCard playedCard;

        public OnJudgeClick(Card.PlayedCard playedCard) {
            this.playedCard = playedCard;
        }

        @Override
        public void onClick(View v) {
            Log.i(getClass().getName(), "Judge picked card:" + playedCard.cardId);
            listener.pickCard(playedCard);
        }
    }

    public interface JudgeListener {
        public void pickCard(Card.PlayedCard pickedCard);
        public boolean isJudge();
    }
}
