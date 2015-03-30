package cards.seniordesign.com.cards;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import cards.seniordesign.com.cards.api.JeezAPI;
import cards.seniordesign.com.cards.models.Room;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListRoomsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListRoomsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListRoomsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int scale_size;
    private Typeface lobby_name_font;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListRoomsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListRoomsFragment newInstance(String param1, String param2) {
        ListRoomsFragment fragment = new ListRoomsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ListRoomsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lobby_name_font = Typeface.createFromAsset(getActivity().getAssets(), "Seravek.ttc");
        scale_size = (int) getResources().getDimension(R.dimen.lobby_item_height);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_rooms, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        JeezAPI.API.getRooms(new Callback<List<Room>>(){
            @Override
            public void success(List<Room> rooms, Response response) {
                for(Room room: rooms) {
                    LinearLayout lobby_holder = (LinearLayout) getView().findViewById(R.id.lobby_holder);
                    if (!room.isEmpty()) {
                        addLobby(lobby_holder, room);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void addLobby(ViewGroup lobby_holder, Room room) {
        String name = room.getName().toUpperCase();
        String count = room.getUsers().size() + "/" + room.getMaxPlayers() + " players";

        Button button = (Button) getActivity().getLayoutInflater().inflate(R.layout.lobby_item, lobby_holder, false);
        Spannable span = new SpannableString(name + "\n" + count);
        span.setSpan(new StyleSpan(Typeface.BOLD), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(0.5f), name.length() + 1, name.length() + 1 + count.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        button.setTypeface(lobby_name_font);
        button.setText(span);

        Bitmap placeholder = getScaledBitmap(R.drawable.placeholder);

        Bitmap combined = Bitmap.createBitmap(scale_size * room.getUsers().size(), scale_size, placeholder.getConfig());
        Canvas combCanvas = new Canvas(combined);
        for(int i=0; i < room.getUsers().size(); i++) {
            combCanvas.drawBitmap(placeholder, i * scale_size, 0, null);
        }

        BitmapDrawable replace = new BitmapDrawable(getResources(), combined);
        replace.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        LayerDrawable unpressed = (LayerDrawable) getResources().getDrawable(R.drawable.lobby_item_unpressed);
        LayerDrawable pressed = (LayerDrawable) getResources().getDrawable(R.drawable.lobby_item_pressed);

        unpressed.setDrawableByLayerId(R.id.lobby_bg_img, replace);
        pressed.setDrawableByLayerId(R.id.lobby_bg_img, replace);

        StateListDrawable lobbyItemBg = new StateListDrawable();
        lobbyItemBg.addState(new int[] {-android.R.attr.state_pressed}, unpressed);
        lobbyItemBg.addState(new int[] {android.R.attr.state_pressed}, pressed);

        button.setBackgroundDrawable(lobbyItemBg);

        lobby_holder.addView(button);
    }


    public Bitmap getScaledBitmap(int drawable) {
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), drawable), scale_size, scale_size, false);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
