package cards.seniordesign.com.cards.models;

import com.facebook.model.GraphUser;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Eric on 3/24/15.
 */
public class User {
    private UUID id;
    private String fbToken;
    private String fbId;
    private String pic;
    private String name;
    private Date updatedAt;
    private Date createdAt;
    private UUID roomId;

    public User() {

    }

    public User(GraphUser graphUser, String accessToken) {
        setFbToken(accessToken);
        setFbId(graphUser.getId());
        setName(graphUser.getName());
        setPic("https://graph.facebook.com/" + getFbId() + "/pic?type=large");
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFbToken() {
        return fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    public String getAuthHeader() {
        return "Token " + getFbToken();
    }
}