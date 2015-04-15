package cards.seniordesign.com.cards.models.response;

import java.io.Serializable;
import java.util.List;

import cards.seniordesign.com.cards.models.Card;
import cards.seniordesign.com.cards.models.GameModel;
import cards.seniordesign.com.cards.models.Round;

/**
 * Created by Eric on 4/14/15.
 */
public class JudgeWaitingResponse implements Serializable {
    public Round round;
    public GameModel game;
    public Card.PlayedCard playedCard;
    public List<Card.PlayedCard> playedCards;
}
