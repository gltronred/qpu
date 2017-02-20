package kpfu.magistracy.service_for_controller.commands;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommandsFromClientDTO {

    //qubits count, used to execute all commands
    @SerializedName("qubit_count")
    private Integer mQubitCount;
    //list of commands from user, in which used logical qubits
    @SerializedName("commands")
    private List<LogicalAddressingCommandFromClient> mLogicalAddressingCommandFromClientList;

    public Integer getQubitCount() {
        return mQubitCount;
    }

    public void setQubitCount(Integer qubitCount) {
        mQubitCount = qubitCount;
    }

    public List<LogicalAddressingCommandFromClient> getLogicalAddressingCommandFromClientList() {
        return mLogicalAddressingCommandFromClientList;
    }

    public void setLogicalAddressingCommandFromClientList(List<LogicalAddressingCommandFromClient> logicalAddressingCommandFromClientList) {
        mLogicalAddressingCommandFromClientList = logicalAddressingCommandFromClientList;
    }
}
