package kpfu.magistracy.controller.execution.commands;

import kpfu.magistracy.controller.addresses.GlobalQubitAddress;

import javax.annotation.Nonnull;

class MeasureCommand extends PhysicalAddressingCommand {

    MeasureCommand(@Nonnull GlobalQubitAddress qubitForMeasure) {
        setCommandType(CommandTypes.MEASURE);
        setFirstQubit_Part1(qubitForMeasure);
    }
}
