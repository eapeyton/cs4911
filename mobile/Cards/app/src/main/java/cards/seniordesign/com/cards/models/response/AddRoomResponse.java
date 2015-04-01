package cards.seniordesign.com.cards.models.response;

import java.util.List;

import cards.seniordesign.com.cards.models.Player;
import cards.seniordesign.com.cards.models.Room;
import cards.seniordesign.com.cards.models.User;

/**
 * Created by Eric on 4/1/15.
 */
public class AddRoomResponse {
    public Room room;
    public User user;
    public Player host;
    public List<Player> judges;
    public List<User> Users;
}
