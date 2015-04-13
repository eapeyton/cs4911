package cards.seniordesign.com.cards.game;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import cards.seniordesign.com.cards.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameplayFragment extends Fragment {


    public GameplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gameplay, container, false);
    }

    protected void addCardButtons() {
        LinearLayout layout = (LinearLayout)getView().findViewById(R.id.card_hand);

        for (String cardText:getResources().getStringArray(R.array.cards)) {
            addCardButton(layout,cardText);
        }

    }

    protected void addCardButton(ViewGroup layout, String cardText) {
        Button button = (Button)this.getActivity().getLayoutInflater().inflate(R.layout.white_card,layout,false);
        button.setText(cardText);
        layout.addView(button);
    }


}
