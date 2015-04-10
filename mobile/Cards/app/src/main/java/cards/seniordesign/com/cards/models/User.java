package cards.seniordesign.com.cards.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.model.GraphUser;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Eric on 3/24/15.
 */
public class User implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(getId());
        dest.writeString(getFbToken());
        dest.writeString(getFbId());
        dest.writeString(getPic());
        dest.writeString(getName());
        dest.writeLong(getUpdatedAt().getTime());
        dest.writeLong(getCreatedAt().getTime());
        dest.writeValue(getRoomId());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private User(Parcel in) {
        setId((UUID)in.readValue(UUID.class.getClassLoader()));
        setFbToken(in.readString());
        setFbId(in.readString());
        setPic(in.readString());
        setName(in.readString());
        setUpdatedAt(new Date(in.readLong()));
        setCreatedAt(new Date(in.readLong()));
        setRoomId((UUID)in.readValue(UUID.class.getClassLoader()));
    }
}
