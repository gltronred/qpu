package kpfu.magistracy.controller.execution.commands;

import kpfu.magistracy.controller.addresses.GlobalQubitAddress;

import javax.annotation.Nonnull;

class InitCommand extends PhysicalAddressingCommand {

    InitCommand(@Nonnull GlobalQubitAddress qubit_Part1, @Nonnull GlobalQubitAddress qubit_Part2) {
        setCommandType(CommandTypes.INIT);
        setFirstQubit_Part1(qubit_Part1);
        setFirstQubit_Part2(qubit_Part2);
    }
}
