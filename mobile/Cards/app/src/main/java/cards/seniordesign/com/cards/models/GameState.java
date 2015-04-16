package cards.seniordesign.com.cards.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Eric on 4/13/15.
 */
public enum GameState {
    @SerializedName("waiting for players")
    WAITING_PLAYERS,
    @SerializedName("waiting for judge")
    WAITING_JUDGE
}
