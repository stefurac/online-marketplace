package entities;

/**
 * an enum class that represents the four possible statuses of a user
 */
public enum UserStatus {
    /**
     * good-standing; allowed to request and perform trades
     */
    GOOD,

    /**
     * flagged automatically by the system for violating thresholds, and pending for review by an admin; prevented from initiating/receiving trades
     */
    PROBATION,


    /**
     * a flagged User that was decided to be frozen by an admin; prevented from initiating/receiving trades
     */
    FROZEN,

    /**
     * a frozen User that has requested to be unfrozen, and pending for review by an admin; prevented from initiating/receiving trades
     */
    REQUESTED,

    /**
     * a frozen User that has enough points to be considered a Premium User; additional abilities
     */
    PREMIUM,

}