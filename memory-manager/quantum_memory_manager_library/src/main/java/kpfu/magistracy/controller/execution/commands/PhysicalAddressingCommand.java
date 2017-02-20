package kpfu.magistracy.controller.execution.commands;

import kpfu.magistracy.controller.addresses.GlobalQubitAddress;

public class PhysicalAddressingCommand extends GeneralCommand<GlobalQubitAddress> {

    static class Builder {

        private PhysicalAddressingCommand physicalAddressingCommand;

        Builder() {
            physicalAddressingCommand = new PhysicalAddressingCommand();
        }

        PhysicalAddressingCommand.Builder setCommand(CommandTypes commandType) {
            physicalAddressingCommand.setCommandType(commandType);
            return this;
        }

        PhysicalAddressingCommand.Builder setCommandParam(Double commandParam) {
            physicalAddressingCommand.setCommandParam(commandParam);
            return this;
        }

        PhysicalAddressingCommand.Builder setFirstQubit_Part1(GlobalQubitAddress qubitAddress) {
            physicalAddressingCommand.setFirstQubit_Part1(qubitAddress);
            return this;
        }

        PhysicalAddressingCommand.Builder setFirstQubit_Part2(GlobalQubitAddress qubitAddress) {
            physicalAddressingCommand.setFirstQubit_Part2(qubitAddress);
            return this;
        }

        PhysicalAddressingCommand.Builder setSecondQubit_Part1(GlobalQubitAddress qubitAddress) {
            physicalAddressingCommand.setSecondQubit_Part1(qubitAddress);
            return this;
        }

        PhysicalAddressingCommand.Builder setSecondQubit_Part2(GlobalQubitAddress qubitAddress) {
            physicalAddressingCommand.setSecondQubit_Part2(qubitAddress);
            return this;
        }

        PhysicalAddressingCommand build() {
            return physicalAddressingCommand;
        }
    }
}
