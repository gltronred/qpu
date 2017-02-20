package kpfu.magistracy.service_for_controller;


public class OwnerData {

    //if user connected to quantum machine through internet userId is socket address
    private String userId;
    //time, when user's commands list was transformed
    private long timeStamp;

    public OwnerData(String userId, long timeStamp) {
        this.userId = userId;
        this.timeStamp = timeStamp;
    }

    String getUserId() {
        return userId;
    }

    private long getTimeStamp() {
        return timeStamp;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OwnerData)) return false;

        OwnerData that = (OwnerData) o;

        return getUserId().equals(that.getUserId()) && getTimeStamp() == that.getTimeStamp();

    }

    @Override
    public String toString() {
        return "UserId = " + userId + ", timeStamp = " + timeStamp;
    }
}
