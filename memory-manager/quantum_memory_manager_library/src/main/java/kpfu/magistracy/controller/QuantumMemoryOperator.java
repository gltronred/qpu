package kpfu.magistracy.controller;

import kpfu.magistracy.controller.addresses.MemoryStateKeeper;
import kpfu.magistracy.controller.execution.commands.LogicalAddressingCommand;
import kpfu.magistracy.controller.execution.commands.PhysicalAddressingCommand;
import kpfu.magistracy.controller.execution.commands.TopLevelCommandsTransformer;
import kpfu.magistracy.controller.execution.results.FinalResults;
import kpfu.magistracy.controller.memory.EmulatedQuantumMemory;
import kpfu.magistracy.controller.memory.QuantumMemory;
import kpfu.magistracy.service_for_controller.OwnerData;
import kpfu.magistracy.service_for_controller.addresses.LogicalQubitAddressForController;

import java.util.List;
import java.util.Map;

public class QuantumMemoryOperator {

    private static QuantumMemoryOperator mQuantumMemoryOperator;

    private QuantumMemory mQuantumMemory;

    private MemoryStateKeeper mMemoryStateKeeper;

    private TopLevelCommandsTransformer mTopLevelCommandsTransformer;

    private FinalResults finalResults;

    private QuantumMemoryOperator() {
        //todo initialize real memory or emulator
        mQuantumMemory = new EmulatedQuantumMemory();
        mMemoryStateKeeper = new MemoryStateKeeper(mQuantumMemory);
        finalResults = new FinalResults(mMemoryStateKeeper);
        mTopLevelCommandsTransformer = new TopLevelCommandsTransformer(mMemoryStateKeeper, finalResults);
    }

    public static QuantumMemoryOperator getOperator() {
        if (mQuantumMemoryOperator == null) {
            mQuantumMemoryOperator = new QuantumMemoryOperator();
        }
        return mQuantumMemoryOperator;
    }

    /**
     * @return command number count, which controller can process
     */
    public int getCommandsMaxCount() {
        //todo
        return 100;
    }

    /**
     * @return max qubit count, which client can use in a single command pack
     */
    public int getQubitsMaxCount() {
        return mMemoryStateKeeper.getMaxQubitCount();
    }


    public synchronized Map<OwnerData, Map<LogicalQubitAddressForController, Boolean>> executeCommands(Map<OwnerData, List<LogicalAddressingCommand>> logicalCommands) {
        List<PhysicalAddressingCommand> physicalAddressingCommands = mTopLevelCommandsTransformer.transformTopLevelCommandsToLowLevel(logicalCommands);
        return finalResults.collectFinalResults(mQuantumMemory.perform(physicalAddressingCommands));
    }

}
