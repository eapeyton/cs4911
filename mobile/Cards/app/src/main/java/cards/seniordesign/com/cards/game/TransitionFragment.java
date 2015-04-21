package cards.seniordesign.com.cards.game;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cards.seniordesign.com.cards.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransitionFragment extends Fragment {


    public TransitionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transition, container, false);
        Button whiteCard = (Button) view.findViewById(R.id.white_card);
        whiteCard.setText("Everyone has played a card.\nNow the judge will pick his favorite.");
        return view;
    }


}
