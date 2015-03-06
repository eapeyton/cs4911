package cards.seniordesign.com.cards;

import java.util.Date;
import java.util.UUID;

/**
 * Created by eric on 3/5/15.
 */
public class Card {
    UUID id;
    UUID userId;
    String text;
    CardType type;
    Date createdAt;
    Date updatedAt;


    public enum CardType {
        WHITE,
        BLACK
    }
}
