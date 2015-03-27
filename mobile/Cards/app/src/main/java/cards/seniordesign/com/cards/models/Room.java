package cards.seniordesign.com.cards.models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Eric on 3/26/15.
 */
public class Room {
    private UUID id;
    private String name;
    private int maxPlayers;
    private Date createdAt;
    private Date updatedAt;
    private List<User> Users;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<User> getUsers() {
        return Users;
    }

    public void setUsers(List<User> users) {
        Users = users;
    }

    public boolean isEmpty() {
        return getUsers().isEmpty();
    }
}
