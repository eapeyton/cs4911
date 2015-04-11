package cards.seniordesign.com.cards.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Eric on 3/26/15.
 */
public class Room implements Parcelable {
    private UUID id;
    private String name;
    private int maxPlayers;
    private Date updatedAt;
    private Date createdAt;
    private List<User> Users;

    public Room() {

    }

    public boolean contains(User user) {
        return Users.contains(user);
    }

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

    public boolean isFull() {
        return getMaxPlayers() == getUsers().size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(getId());
        dest.writeString(getName());
        dest.writeInt(getMaxPlayers());
        dest.writeValue(getUpdatedAt());
        dest.writeValue(getCreatedAt());
        dest.writeTypedList(getUsers());
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel source) {
            return new Room(source);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    public Room(Parcel source) {
        setId((UUID) source.readValue(UUID.class.getClassLoader()));
        setName(source.readString());
        setMaxPlayers(source.readInt());
        setUpdatedAt((Date) source.readValue(Date.class.getClassLoader()));
        setCreatedAt((Date)source.readValue(Date.class.getClassLoader()));
        List<User> users = new ArrayList<User>();
        source.readTypedList(users, User.CREATOR);
        setUsers(users);
    }

}
