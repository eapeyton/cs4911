package cards.seniordesign.com.cards.models.response;

import cards.seniordesign.com.cards.game.Game;
import cards.seniordesign.com.cards.models.Card;
import cards.seniordesign.com.cards.models.Round;
import cards.seniordesign.com.cards.models.User;

/**
 * Created by Eric on 4/23/15.
 */
public class GameReviewResponse {
    public Round round;
    public Game game;
    public Card winningCard;
    public User winner;
}
