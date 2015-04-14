package cards.seniordesign.com.cards.models;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Eric on 4/13/15.
 */
public class Round {
    private UUID id;
    private UUID gameId;
    private UUID judge;
    private UUID blackCard;
    private GameState state;
    private Date updatedAt;
    private Date createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public UUID getJudge() {
        return judge;
    }

    public void setJudge(UUID judge) {
        this.judge = judge;
    }

    public UUID getBlackCard() {
        return blackCard;
    }

    public void setBlackCard(UUID blackCard) {
        this.blackCard = blackCard;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
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
}
