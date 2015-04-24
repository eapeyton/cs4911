package cards.seniordesign.com.cards;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cards.seniordesign.com.cards.models.Card;
import cards.seniordesign.com.cards.models.User;

public class PreCreateCardFragment extends Fragment implements View.OnClickListener {
    private User currentUser;

    public static PreCreateCardFragment newInstance(User currentUser) {
        PreCreateCardFragment fragment = new PreCreateCardFragment();
        Bundle args = new Bundle();
        args.putParcelable(Args.CURRENT_USER, currentUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = (User) getArguments().getParcelable(Args.CURRENT_USER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pre_create_card, container, false);
        Button whiteButton = (Button)view.findViewById(R.id.create_white_card);
        whiteButton.setOnClickListener(this);
        Button blackButton = (Button)view.findViewById(R.id.create_black_card);
        blackButton.setOnClickListener(this);
        getActivity().getActionBar().setTitle("Which Color?");
        return view;
    }

    public void onClick(View v) {
        Card.CardType ctype = Card.CardType.BLACK;
        if (v.getId() == R.id.create_white_card) {
            ctype = Card.CardType.WHITE;
        }
        Fragment createCardFragment = CreateCardFragment.newInstance(currentUser, ctype);
        getActivity().getActionBar().setTitle("Set the text");
        getFragmentManager().beginTransaction()
                .add(R.id.container, createCardFragment)
                .addToBackStack(null)
                .commit();
    }

}