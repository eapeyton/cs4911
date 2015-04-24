package cards.seniordesign.com.cards.game;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cards.seniordesign.com.cards.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransitionFragment extends Fragment {

    public static final String MESSAGE = "MESSAGE";

    public static void transitionToMessage(final FragmentManager manager, String message) {
        transitionToFragment(manager, null, message);
    }

    public static void transitionToFragment(final FragmentManager manager, final Fragment fragment, String message) {
        manager.beginTransaction()
                .setCustomAnimations(R.animator.fade_in_long, R.animator.fade_out_long)
                .replace(R.id.content_frame, TransitionFragment.newInstance(message))
                .commit();

        if (fragment != null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    manager.beginTransaction()
                            .setCustomAnimations(R.animator.fade_in_long, R.animator.fade_out_long)
                            .replace(R.id.content_frame, fragment)
                            .commit();
                }
            }, 2000);
        }
    }

    private static TransitionFragment newInstance(String message) {
        TransitionFragment fragment = new TransitionFragment();
        Bundle args = new Bundle();
        args.putString(MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    public TransitionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transition, container, false);
        Button whiteCard = (Button) view.findViewById(R.id.white_card);
        whiteCard.setGravity(Gravity.CENTER);
        whiteCard.setText(getArguments().getString(MESSAGE));
        return view;
    }

}
