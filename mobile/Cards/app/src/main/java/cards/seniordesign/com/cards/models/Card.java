package cards.seniordesign.com.cards.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by eric on 3/5/15.
 */
public class Card implements Serializable {
    private UUID id;
    private UUID userId;
    private String text;
    private CardType type;
    private Date createdAt;
    private Date updatedAt;

    public enum CardType {
        @SerializedName("white")
        WHITE,
        @SerializedName("black")
        BLACK
    }

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
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
}
