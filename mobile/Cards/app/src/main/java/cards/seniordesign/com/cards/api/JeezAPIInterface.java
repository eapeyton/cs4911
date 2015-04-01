package cards.seniordesign.com.cards.api;

import java.util.List;
import java.util.UUID;

import cards.seniordesign.com.cards.models.Card;
import cards.seniordesign.com.cards.models.Room;
import cards.seniordesign.com.cards.models.User;
import cards.seniordesign.com.cards.models.response.AddRoomResponse;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by eric on 3/5/15.
 */
public interface JeezAPIInterface {

    @POST("/users/login")
    public void userLogin(@Body User user, Callback<User> cb);

    @DELETE("/users/{id}")
    public void deleteUser(@Path("id") UUID id, Callback<Boolean> cb);

    @GET("/users")
    public void getUsers(Callback<List<User>> cb);

    @GET("/cards")
    public void getCards(Callback<List<Card>> card);

    @GET("/rooms")
    public void getRooms(Callback<List<Room>> cb);

    @POST("/rooms")
    public void addRoom(@Body Room newRoom, Callback<AddRoomResponse> cb);
}
