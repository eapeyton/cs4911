package cards.seniordesign.com.cards.models.response;

import java.io.Serializable;
import java.util.List;

import cards.seniordesign.com.cards.models.Card;
import cards.seniordesign.com.cards.models.GameModel;
import cards.seniordesign.com.cards.models.Player;
import cards.seniordesign.com.cards.models.Round;

/**
 * Created by Eric on 4/13/15.
 */
public class StartGameResponse implements Serializable {
    public Player judge;
    public Card blackCard;
    public GameModel game;
    public Round round;
    public List<Player.PlayerState> playerStates;
}
