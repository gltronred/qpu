package kpfu.magistracy.service_for_controller.addresses;


public class LogicalQubitAddressForController {

    private long globalId;
    //0 or 1
    private int memoryPart;

    public LogicalQubitAddressForController(long globalId, int memoryPart) {
        this.globalId = globalId;
        this.memoryPart = memoryPart;
    }

    private long getGlobalId() {
        return globalId;
    }

    public int getMemoryPart() {
        return memoryPart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogicalQubitAddressForController)) return false;

        LogicalQubitAddressForController that = (LogicalQubitAddressForController) o;

        return getGlobalId() == that.getGlobalId() && getMemoryPart() == that.getMemoryPart();

    }

    @Override
    public String toString() {
        return "globalId = " + globalId + ", memoryPart = " + memoryPart;
    }
}
