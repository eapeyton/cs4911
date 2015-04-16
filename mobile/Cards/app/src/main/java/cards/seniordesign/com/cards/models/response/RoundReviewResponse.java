package cards.seniordesign.com.cards.models.response;

import cards.seniordesign.com.cards.models.Card;
import cards.seniordesign.com.cards.models.GameModel;
import cards.seniordesign.com.cards.models.Player;
import cards.seniordesign.com.cards.models.Round;
import cards.seniordesign.com.cards.models.User;

/**
 * Created by Eric on 4/15/15.
 */
public class RoundReviewResponse {
    public Round round;
    public GameModel game;
    public Card winningCard;
    public User winner;
    public Player leader;
}
