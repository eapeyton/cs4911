package cards.seniordesign.com.cards.models.response;

import cards.seniordesign.com.cards.game.Game;
import cards.seniordesign.com.cards.models.Card;
import cards.seniordesign.com.cards.models.GameModel;
import cards.seniordesign.com.cards.models.Round;
import cards.seniordesign.com.cards.models.User;

/**
 * Created by Eric on 4/23/15.
 */
public class PlayCardResponse {
    public Round round;
    public GameModel game;
    public Card playedCard;
    public User User;
}
