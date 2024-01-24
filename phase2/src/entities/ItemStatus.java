package entities;

/**
 * an enum class that represents the statuses of an item in the system
 */
public enum ItemStatus {
    /**
     * an item waiting admin approval
     */
    UNCHECKED,
    /**
     * an approved item available for trade
     */
    AVAILABLE,
    /**
     * an approved item unavailable for trade
     */
    UNAVAILABLE,

    /**
     * an item that has been removed from the system by either an admin or user
     */
    REMOVED
}
