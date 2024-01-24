package entities;
import java.time.*;
import java.util.UUID;

public class Meeting {

    /**
     * Meeting is an entity
     * <p>
     * meetingId: The unique identifier of the Meeting.
     * time: The time of the Meeting.
     * place: The place of the Meeting.
     * usersTurn: an indicator of whether or not it is a User's turn to edit a Meeting.
     * usersEditCount: the number of edits a user has made to a Meeting.
     */
    private String meetingId;
    private LocalDate time;
    private String place;
    private boolean[] usersTurn;
    private String userLastEdited;
    private int[] usersEditCount;

    /**
     * Constructor for "Meeting." Newly constructed Meetings automatically have whether or not it is a User's turn  set
     * to false, and the number of edits a User has made as 0. "
     *
     * @param time  The assigned Meeting time.
     * @param place The assigned Meeting place.
     */
    public Meeting(LocalDate time, String place, String user) {
        UUID id = UUID.randomUUID();
        this.meetingId = String.valueOf(id);
        this.time = time;
        this.place = place;
        this.usersTurn = new boolean[]{true, true};
        this.userLastEdited = user;
        this.usersEditCount = new int[]{0, 0};
    }

    /**
     * toString method for Meeting
     *
     * @return information about Meeting in String
     */
    public String toString() {
        return "MeetingId: " + getMeetingId() +
                "\n Time: " + getTime() +
                "\n Place: " + getPlace() +
                "\n Requester can edit: " + getUsersTurn()[0] +
                "\n Receiver can edit: " + getUsersTurn()[1] +
                "\n Requester's number of edits: " + getUsersEditCount()[0] +
                "\n Receiver's number of edits: " + getUsersEditCount()[1];
    }

    /**
     * Gets the Meeting id.
     *
     * @return meetingId
     */
    public String getMeetingId() {
        return this.meetingId;
    }

    /**
     * Sets the Meeting id.
     *
     * @param suggestedId the suggested Id.
     */
    public void setId(String suggestedId) {
        this.meetingId = suggestedId;
    }

    /**
     * Gets the Meeting time.
     *
     * @return time
     */
    public LocalDate getTime() {
        return this.time;
    }

    /**
     * Sets the Meeting time.
     *
     * @param suggestedTime the suggested time.
     */
    public void setTime(LocalDate suggestedTime) {
        this.time = suggestedTime;
    }

    /**
     * Gets the Meeting place.
     *
     * @return place
     */
    public String getPlace() {
        return this.place;
    }

    /**
     * Sets the Meeting place.
     *
     * @param suggestedPlace the suggested place.
     */
    public void setPlace(String suggestedPlace) {
        this.place = suggestedPlace;
    }

    /**
     * Gets whether or not it is a User's turn.
     *
     * @return usersTurn
     */
    public boolean[] getUsersTurn() {
        return this.usersTurn;
    }

    /**
     * Sets the User's turns; must be used in conjunction with setUserLastEdited()
     *
     * @param usersTurns the suggested turns.
     */
    public void setUsersTurns(boolean[] usersTurns) {
        this.usersTurn = usersTurns;
    }

    /**
     * Gets the user ID of the user who last made an edit
     *
     * @return the user ID of the user who last made an edit
     */
    public String getUserLastEdited() {
        return this.userLastEdited;
    }

    /**
     * Sets the user ID of the user who last made an edit
     *
     * @param userLastEdited the user ID of the user who last made an edit
     */
    public void setUserLastEdited(String userLastEdited) {
        this.userLastEdited = userLastEdited;
    }

    /**
     * Gets number of edits a User has made to a Meeting.
     *
     * @return usersEditCount
     */
    public int[] getUsersEditCount() {
        return this.usersEditCount;
    }

    /**
     * Sets the User's edit count for this Meeting.
     *
     * @param usersEditCount the suggested count.
     */
    public void setUsersEditCount(int[] usersEditCount) {
        this.usersEditCount = usersEditCount;
    }
}




