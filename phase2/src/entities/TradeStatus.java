package entities;

/**
 * an enum class that represents the statuses of a trade
 */
public enum TradeStatus {
    /**
     * the current status of the Trade is requested by the requester
     */
    REQUESTED,
    /**
     * the Trade is accepted by the receiver
     */
    ACCEPTED,
    /**
     * the Trade is rejected by the receiver
     */
    REJECTED,
    /**
     * both users involved in the Trade have confirmed a meeting time/place
     */
    CONFIRMED,
    /**
     * the Trade has been confirmed to have happened in real life by the requester
     */
    REQUESTER_CONFIRMED,
    /**
     * the Trade has been confirmed to have happened in real life by the receiver
     */
    RECEIVER_CONFIRMED,
    /**
     * both users have confirmed that the Trade has happened in real life
     */
    COMPLETED,
    /**
     * the Trade is cancelled after the Trade is accepted
     */
    CANCELLED,
    /**
     * the Trade has been deleted by an admin
     */
    ADMIN_DELETED,
}
