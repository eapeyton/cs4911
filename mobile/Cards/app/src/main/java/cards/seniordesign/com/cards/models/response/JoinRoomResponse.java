package cards.seniordesign.com.cards.models.response;

import java.util.List;

import cards.seniordesign.com.cards.models.Player;
import cards.seniordesign.com.cards.models.Room;
import cards.seniordesign.com.cards.models.User;

/**
 * Created by Eric on 4/9/15.
 */
public class JoinRoomResponse {
    public Room room;
    public Player host;
    public List<Player> judges;
    public List<User> Users;
}
