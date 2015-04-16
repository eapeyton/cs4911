package cards.seniordesign.com.cards.lobby;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cards.seniordesign.com.cards.Args;
import cards.seniordesign.com.cards.Dialog;
import cards.seniordesign.com.cards.R;
import cards.seniordesign.com.cards.api.JeezAPIClient;
import cards.seniordesign.com.cards.models.Room;
import cards.seniordesign.com.cards.models.User;
import cards.seniordesign.com.cards.models.response.AddRoomResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link cards.seniordesign.com.cards.lobby.AddRoomFragment.AddRoomListener} interface
 * to handle interaction events.
 * Use the {@link AddRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRoomFragment extends Fragment {

    private AddRoomListener listener;
    public static final int LOWER_SIZE = 2;
    public static final int UPPER_SIZE = 20;
    private User currentUser;

    public static AddRoomFragment newInstance(User currentUser) {
        AddRoomFragment fragment = new AddRoomFragment();
        Bundle args = new Bundle();
        args.putParcelable(Args.CURRENT_USER, currentUser);
        fragment.setArguments(args);
        return fragment;
    }

    public AddRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_add_room, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_accept:
                validateAndAddRoom();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void validateAndAddRoom() {
        View view = getView();
        String name = ((TextView) view.findViewById(R.id.add_room_name)).getText().toString();
        Integer size = Integer.parseInt(((Button) view.findViewById(R.id.add_room_size)).getText().toString());

        if (name.isEmpty()) {
            Dialog.showError(getActivity(), "Room name must not be empty");
        } else if (isInvalidSize(size)) {
            Dialog.showError(getActivity(),String.format("Size must be between %d and %d", LOWER_SIZE, UPPER_SIZE));
        } else {
            addRoom(name, size);
        }
    }

    private void addRoom(String name, Integer size) {
        Room newRoom = new Room();
        newRoom.setName(name);
        newRoom.setMaxPlayers(size);
        JeezAPIClient.getAPI().addRoom(newRoom, new Callback<AddRoomResponse>() {
            @Override
            public void success(AddRoomResponse addRoomResponse, Response response) {
                Log.i("AddRoom", "Room added successfully");
                currentUser.setRoomId(addRoomResponse.room.getId());
                listener.goToGameAsHost(addRoomResponse.room, currentUser);
            }

            @Override
            public void failure(RetrofitError error) {
                Dialog.showError(getActivity(), "Failed to add room. Perhaps you are already inside of one?");
            }
        });
    }

    private boolean isInvalidSize(int roomSize) {
        if (roomSize < LOWER_SIZE || roomSize > UPPER_SIZE) {
            return true;
        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        currentUser = getArguments().getParcelable(Args.CURRENT_USER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_room, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (AddRoomListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface AddRoomListener {
        public void goToGameAsHost(Room room, User currentUser);
    }

}
