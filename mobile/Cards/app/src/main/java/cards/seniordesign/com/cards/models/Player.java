package cards.seniordesign.com.cards.models;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Eric on 4/1/15.
 */
public class Player implements Serializable {
    private UUID id;
    private UUID userId;
    private UUID roomId;
    private int place;
    private int points;
    private Date updatedAt;
    private Date createdAt;
    private User User;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
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

    public String getName() {
        return User.getName();
    }

    public static class PlayerState implements Serializable {
        public UUID id;
        public UUID userId;
        public UUID gameId;
        public GameState state;
        public Date createdAt;
        public Date updatedAt;
    }
}
