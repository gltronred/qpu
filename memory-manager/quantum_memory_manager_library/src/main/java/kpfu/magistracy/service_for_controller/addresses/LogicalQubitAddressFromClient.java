package kpfu.magistracy.service_for_controller.addresses;


import com.google.gson.annotations.SerializedName;
import kpfu.magistracy.service_for_controller.OwnerData;

public class LogicalQubitAddressFromClient {

    //Local ordinal qubit number in the client program
    @SerializedName("local_id")
    private
    int mLogicalQubitAddress;
    //заполняю самостоятельно, нужен для однозначной идентификации локального кубита одного из пользователей
    //fill by myself, need to uniquely identify every user's local qubit
    private OwnerData mOwnerData;

    public LogicalQubitAddressFromClient(int logicalQubitAddress) {
        mLogicalQubitAddress = logicalQubitAddress;
    }

    public int getLogicalQubitAddress() {
        return mLogicalQubitAddress;
    }

    private OwnerData getOwnerData() {
        return mOwnerData;
    }

    public void setOwnerData(OwnerData ownerData) {
        mOwnerData = ownerData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogicalQubitAddressFromClient)) return false;

        LogicalQubitAddressFromClient that = (LogicalQubitAddressFromClient) o;
        return getLogicalQubitAddress() == that.getLogicalQubitAddress() && getOwnerData().equals(that.getOwnerData());

    }
}
