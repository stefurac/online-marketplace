package usecases;

import entities.Meeting;

import java.time.*;
import java.util.*;

public class MeetingManager {

    /**
     * MeetingManager is a use case for Meeting.
     *
     * editsThreshold is the threshold for editing a Meeting.
     * meetingIdToMeeting store all of Meetings that exist and all corresponding MeetingsIds.
     */
    private final int editsThreshold;
    private final HashMap<String, Meeting> meetingIdToMeeting = new HashMap<>();

    /**
     * Constructor for MeetingManager. Creates a MeetingManager with editsThreshold and meetingIdToMeeting, which is
     * based on the meetingManagerList and stores the HashMap of meetingId to Meeting from the database.
     *
     * @param editsThreshold editsThreshold
     * @param listMeeting meetingList
     */
    public MeetingManager(int editsThreshold, List<Meeting> listMeeting) {
        this.editsThreshold = editsThreshold;
        for (Meeting meeting: listMeeting) {
            this.meetingIdToMeeting.put(meeting.getMeetingId(), meeting);
        }
    }

    /**
     * Gets the limit for the number of times a User can edit a Meeting.
     *
     * @return an int of the threshold for editing a Meeting.
     */
    public int getEditsThreshold() { return this.editsThreshold; }
    

    /**
     * Gets meetingIdToMeeting.
     *
     * @return a HashMap of Meetings and corresponding Meeting ids.
     */
    public Map<String, Meeting> getMeetingIdToMeeting() {
        return this.meetingIdToMeeting;
    }

    /**
     * Gets a Meeting from a MeetingId.
     *
     * @return a Meeting
     */
    public Meeting findMeeting(String meetingId) {
        return getMeetingIdToMeeting().get(meetingId);
    }

    /**
     * Adds a Meeting to the MeetingManager.
     *
     * @param meetingId a Meeting id.
     * @param meeting a corresponding Meeting to the Meeting id.
     */
    public void addMeeting(String meetingId, Meeting meeting) {
        getMeetingIdToMeeting().put(meetingId, meeting);
    }

    /**
     * Deletes a Meeting from MeetingManager.
     *
     * @param meetingId a Meeting id.
     */
    public void deleteMeeting(String meetingId) {
        getMeetingIdToMeeting().remove(meetingId);
    }

    /**
     * Creates a new Meeting adds it to the MeetingManager.
     *
     * @param time a suggested time for the Meeting.
     * @param place a suggested place for the Meeting.
     * @return a String of the Meeting Id of the Meeting.
     */
    public String createNewMeeting(LocalDate time, String place, String userId) {
        Meeting meeting = new Meeting(time, place, userId);
        addMeeting(meeting.getMeetingId(), meeting);
        return (meeting.getMeetingId());
    }

    /**
     * Gets whether or not a User is able to edit a Meeting.
     *
     * @param meetingId Meeting id of the Meeting.
     * @param userNum the id of the User.
     * @return a boolean of whether or not the User can edit the Meeting.
     */
    public boolean userCanEdit(String meetingId, int userNum) {
        Meeting meeting = findMeeting(meetingId);
        return meeting.getUsersEditCount()[userNum] < getEditsThreshold();
    }

    /**
     * Edits the Meeting if it is possible for the User to do so.
     *
     * @param meetingId Meeting id of the Meeting.
     * @param time a suggested time for the Meeting.
     * @param place a suggested place for the Meeting.
     * @param userNum the id of the User.
     * @return a boolean of whether or not the User can edit the Meeting.
     */
    public boolean editTimePlace(String meetingId, LocalDate time, String place, int userNum) {
        Meeting meeting = findMeeting(meetingId);
        if (userCanEdit(meetingId, userNum)) {
            meeting.setTime(time);
            meeting.setPlace(place);
            changeTurns(meetingId, userNum);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets whether or not it is a User's turn to edit a Meeting.
     *
     * @param meetingId Meeting id of the Meeting.
     * @param userNum the id of the User.
     * @return a boolean of whether or not it is the User's turn to edit the Meeting.
     */
    public boolean usersTurn(String meetingId, int userNum) {
        return findMeeting(meetingId).getUsersTurn()[userNum];
    }

    /**
     * Confirms the Meeting if it is possible for the User to do so.
     *
     * @param meetingId Meeting id of the Meeting.
     * @param userNum the id of the User.
     */
    public void confirmTimePlace(String meetingId, int userNum) {
        Meeting meeting = findMeeting(meetingId);
        meeting.getUsersTurn()[userNum] = false;
    }

    /**
     * Gets whether or not it a Meeting is confirmed.
     *
     * @param meetingId Meeting id of the Meeting.
     * @return a boolean of whether or not the Meeting is confirmed.
     */
    public boolean isMeetingIdConfirmed(String meetingId) {
        Meeting meeting = findMeeting(meetingId);
        return !meeting.getUsersTurn()[0] && !meeting.getUsersTurn()[1];
    }

    /**
     * Gets the place of a Meeting from a Meeting id.
     *
     * @param meetingId Meeting id of the Meeting.
     * @return a String of the place of the Meeting.
     */
    public String getPlaceByMeetingId(String meetingId) {
        return findMeeting(meetingId).getPlace();
    }

    /**
     * Gets the time of a Meeting from a Meeting id.
     *
     * @param meetingId Meeting id of the Meeting.
     * @return a LocalDate of the time of the Meeting.
     */
    public LocalDate getTimeByMeetingId(String meetingId) {
        return findMeeting(meetingId).getTime();
    }

    private void changeTurns(String meetingId, int userNum) {
        Meeting meeting = findMeeting(meetingId);
        if (meeting.getUsersTurn()[0] && !meeting.getUsersTurn()[1]) {
            meeting.getUsersEditCount()[userNum]++;
            meeting.getUsersTurn()[0] = false;
            meeting.getUsersTurn()[1] = true;
        } else if (!meeting.getUsersTurn()[0] && meeting.getUsersTurn()[1]) {
            meeting.getUsersEditCount()[userNum]++;
            meeting.getUsersTurn()[0] = true;
            meeting.getUsersTurn()[1] = false;
        } else if (meeting.getUsersTurn()[0] && meeting.getUsersTurn()[1]) {
            meeting.getUsersTurn()[userNum] = false;
            meeting.getUsersEditCount()[userNum]++;
        }
    }
}
