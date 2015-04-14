package cards.seniordesign.com.cards.game;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cards.seniordesign.com.cards.Args;
import cards.seniordesign.com.cards.R;
import cards.seniordesign.com.cards.models.Room;
import cards.seniordesign.com.cards.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreGameFragment extends Fragment {
    private Room currentRoom;
    private User currentUser;
    private boolean isHost;

    public static PreGameFragment newInstance(User currentUser, Room currentRoom, boolean isHost) {
        PreGameFragment fragment = new PreGameFragment();
        Bundle args = new Bundle();
        args.putParcelable(Args.CURRENT_USER, currentUser);
        args.putParcelable(Args.CURRENT_ROOM, currentRoom);
        args.putBoolean(Args.IS_HOST, isHost);
        fragment.setArguments(args);
        return fragment;
    }

    public PreGameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = getArguments().getParcelable(Args.CURRENT_USER);
        currentRoom = getArguments().getParcelable(Args.CURRENT_ROOM);
        isHost = getArguments().getBoolean(Args.IS_HOST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pre_game, container, false);
        if (isHost) {
            TextView waitingText = (TextView) view.findViewById(R.id.waiting_for_host);
            waitingText.setVisibility(View.GONE);
        } else {
            Button startGame = (Button) view.findViewById(R.id.start_game);
            startGame.setVisibility(View.GONE);
        }
        return view;
    }


}
