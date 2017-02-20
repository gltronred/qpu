package kpfu.magistracy.service_for_controller.commands;

import com.google.gson.annotations.SerializedName;
import kpfu.magistracy.controller.execution.commands.CommandTypes;
import kpfu.magistracy.service_for_controller.addresses.LogicalQubitAddressFromClient;


public class LogicalAddressingCommandFromClient {
    //command name from universal basis
    @SerializedName("command_name")
    private CommandTypes mCommandType;
    @SerializedName("command_param")
    private Double mCommandParam;
    @SerializedName("qubit_1")
    private LogicalQubitAddressFromClient mQubit_1;
    @SerializedName("qubit_2")
    private LogicalQubitAddressFromClient mQubit_2;

    public LogicalAddressingCommandFromClient(CommandTypes commandType, Double commandParam, LogicalQubitAddressFromClient qubit_1, LogicalQubitAddressFromClient qubit_2) {
        mCommandType = commandType;
        mCommandParam = commandParam;
        mQubit_1 = qubit_1;
        mQubit_2 = qubit_2;
    }

    public LogicalAddressingCommandFromClient(CommandTypes commandType, Double commandParam, LogicalQubitAddressFromClient qubit_1) {
        mCommandType = commandType;
        mCommandParam = commandParam;
        mQubit_1 = qubit_1;
    }

    public CommandTypes getCommandType() {
        return mCommandType;
    }

    public Double getCommandParam() {
        return mCommandParam;
    }

    public LogicalQubitAddressFromClient getQubit_1() {
        return mQubit_1;
    }

    public LogicalQubitAddressFromClient getQubit_2() {
        return mQubit_2;
    }
}
