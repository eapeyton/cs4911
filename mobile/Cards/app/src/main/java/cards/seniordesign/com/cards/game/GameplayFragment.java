package cards.seniordesign.com.cards.game;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import cards.seniordesign.com.cards.Args;
import cards.seniordesign.com.cards.R;
import cards.seniordesign.com.cards.models.Room;
import cards.seniordesign.com.cards.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameplayFragment extends Fragment {
    private Room currentRoom;
    private User currentUser;

    public static GameplayFragment newInstance(User currentUser, Room currentRoom) {
        GameplayFragment fragment = new GameplayFragment();
        Bundle args = new Bundle();
        args.putParcelable(Args.CURRENT_USER, currentUser);
        args.putParcelable(Args.CURRENT_ROOM, currentRoom);
        fragment.setArguments(args);
        return fragment;
    }

    public GameplayFragment() {
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
        View view = inflater.inflate(R.layout.fragment_gameplay, container, false);
        addCardButtons(view);
        return view;
    }

    protected void addCardButtons(View view) {
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.card_hand);

        for (String cardText:getResources().getStringArray(R.array.cards)) {
            addCardButton(layout,cardText);
        }

    }

    protected void addCardButton(ViewGroup layout, String cardText) {
        Button button = (Button)this.getActivity().getLayoutInflater().inflate(R.layout.white_card, layout, false);
        button.setText(cardText);
        layout.addView(button);
    }


}
