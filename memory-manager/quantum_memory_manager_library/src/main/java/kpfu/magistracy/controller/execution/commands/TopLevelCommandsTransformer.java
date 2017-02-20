package kpfu.magistracy.controller.execution.commands;

import kpfu.magistracy.controller.addresses.MemoryStateKeeper;
import kpfu.magistracy.controller.execution.results.FinalResults;
import kpfu.magistracy.service_for_controller.OwnerData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TopLevelCommandsTransformer {

    private MemoryStateKeeper mMemoryStateKeeper;
    private FinalResults mFinalResults;

    public TopLevelCommandsTransformer(MemoryStateKeeper memoryStateKeeper, FinalResults finalResults) {
        this.mMemoryStateKeeper = memoryStateKeeper;
        this.mFinalResults = finalResults;
    }

    /**
     * @param logicalCommands commands, in which we should transform qubit addresses
     * @return list with commands, prepared for memory performance
     */
    public List<PhysicalAddressingCommand> transformTopLevelCommandsToLowLevel(Map<OwnerData, List<LogicalAddressingCommand>> logicalCommands) {
        List<PhysicalAddressingCommand> physicalAddressingCommands = new ArrayList<>();

        List<PhysicalAddressingCommand> tempList = new ArrayList<>();
        for (Map.Entry<OwnerData, List<LogicalAddressingCommand>> commandEntry : logicalCommands.entrySet()) {
            for (LogicalAddressingCommand logicalAddressingCommand : commandEntry.getValue()) {

                //fill map for retrieving owner data later
                mFinalResults.putOwnerData(logicalAddressingCommand.getFirstQubit_Part1(), commandEntry.getKey());
                mFinalResults.putOwnerData(logicalAddressingCommand.getFirstQubit_Part2(), commandEntry.getKey());
                if (!logicalAddressingCommand.isSecondLogicalQubitNull()) {
                    mFinalResults.putOwnerData(logicalAddressingCommand.getSecondQubit_Part1(), commandEntry.getKey());
                    mFinalResults.putOwnerData(logicalAddressingCommand.getSecondQubit_Part2(), commandEntry.getKey());
                }

                //create physical command with first qubit
                PhysicalAddressingCommand.Builder physicalAddressingCommandBuilder = new PhysicalAddressingCommand.Builder();
                physicalAddressingCommandBuilder
                        .setCommand(logicalAddressingCommand.getCommandType())
                        .setCommandParam(logicalAddressingCommand.getCommandParam())
                        .setFirstQubit_Part1(mMemoryStateKeeper.getGlobalAddressForQubit(logicalAddressingCommand.getFirstQubit_Part1()))
                        .setFirstQubit_Part2(mMemoryStateKeeper.getGlobalAddressForQubit(logicalAddressingCommand.getFirstQubit_Part2()));

                //if second logical qubit is not null - get address for it's parts and add to command
                if (!logicalAddressingCommand.isSecondLogicalQubitNull()) {
                    physicalAddressingCommandBuilder
                            .setSecondQubit_Part1(mMemoryStateKeeper.getGlobalAddressForQubit(logicalAddressingCommand.getSecondQubit_Part1()))
                            .setSecondQubit_Part2(mMemoryStateKeeper.getGlobalAddressForQubit(logicalAddressingCommand.getSecondQubit_Part2()));
                }
                PhysicalAddressingCommand physicalAddressingCommand = physicalAddressingCommandBuilder.build();
                //add Init commands for qubits if it is necessary
                if (mMemoryStateKeeper.needInitializeLogicalQubit(physicalAddressingCommand.getFirstQubit_Part1(), physicalAddressingCommand.getFirstQubit_Part2())) {
                    tempList.add(new InitCommand(physicalAddressingCommand.getFirstQubit_Part1(), physicalAddressingCommand.getFirstQubit_Part2()));
                    mMemoryStateKeeper.onQubitInitialized(physicalAddressingCommand.getFirstQubit_Part1());
                    mMemoryStateKeeper.onQubitInitialized(physicalAddressingCommand.getFirstQubit_Part2());
                }
                if (!logicalAddressingCommand.isSecondLogicalQubitNull()) {
                    if (mMemoryStateKeeper.needInitializeLogicalQubit(physicalAddressingCommand.getSecondQubit_Part1(), physicalAddressingCommand.getSecondQubit_Part2())) {
                        tempList.add(new InitCommand(physicalAddressingCommand.getSecondQubit_Part1(), physicalAddressingCommand.getSecondQubit_Part2()));
                        mMemoryStateKeeper.onQubitInitialized(physicalAddressingCommand.getSecondQubit_Part1());
                        mMemoryStateKeeper.onQubitInitialized(physicalAddressingCommand.getSecondQubit_Part2());
                    }
                }
                tempList.add(physicalAddressingCommand);
            }
            physicalAddressingCommands.addAll(tempList);
            //add measure commands for every init command
            for (PhysicalAddressingCommand physicalAddressingCommand : tempList) {
                if (physicalAddressingCommand.getCommandType() == CommandTypes.INIT) {
                    physicalAddressingCommands.add(new MeasureCommand(physicalAddressingCommand.getFirstQubit_Part1()));
                    physicalAddressingCommands.add(new MeasureCommand(physicalAddressingCommand.getFirstQubit_Part2()));

                    if (!physicalAddressingCommand.isSecondLogicalQubitNull()) {
                        physicalAddressingCommands.add(new MeasureCommand(physicalAddressingCommand.getSecondQubit_Part1()));
                        physicalAddressingCommands.add(new MeasureCommand(physicalAddressingCommand.getSecondQubit_Part2()));
                    }
                }
            }
            tempList.clear();
            //clear memory params 'cause we measure all previous qubits
            mMemoryStateKeeper.clearMemoryParams();
        }
        return physicalAddressingCommands;
    }
}
