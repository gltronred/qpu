package kpfu.magistracy.controller.execution.commands;

import kpfu.magistracy.service_for_controller.addresses.LogicalQubitAddressForController;

import javax.annotation.Nonnull;

public class LogicalAddressingCommand extends GeneralCommand<LogicalQubitAddressForController> {

    public static class Builder {

        private LogicalAddressingCommand logicalAddressingCommand;

        public Builder() {
            logicalAddressingCommand = new LogicalAddressingCommand();
        }

        public Builder setCommand(@Nonnull CommandTypes commandType) {
            logicalAddressingCommand.setCommandType(commandType);
            return this;
        }

        public Builder setCommandParam(@Nonnull Double commandParam) {
            logicalAddressingCommand.setCommandParam(commandParam);
            return this;
        }

        public Builder setFirstQubit_Part1(@Nonnull LogicalQubitAddressForController qubitAddress) {
            logicalAddressingCommand.setFirstQubit_Part1(qubitAddress);
            return this;
        }

        public Builder setFirstQubit_Part2(@Nonnull LogicalQubitAddressForController qubitAddress) {
            logicalAddressingCommand.setFirstQubit_Part2(qubitAddress);
            return this;
        }

        public Builder setSecondQubit_Part1(@Nonnull LogicalQubitAddressForController qubitAddress) {
            logicalAddressingCommand.setSecondQubit_Part1(qubitAddress);
            return this;
        }

        public Builder setSecondQubit_Part2(@Nonnull LogicalQubitAddressForController qubitAddress) {
            logicalAddressingCommand.setSecondQubit_Part2(qubitAddress);
            return this;
        }

        public LogicalAddressingCommand build() {
            return logicalAddressingCommand;
        }
    }
}


