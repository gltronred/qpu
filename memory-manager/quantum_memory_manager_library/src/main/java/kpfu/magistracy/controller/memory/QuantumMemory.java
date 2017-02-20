package kpfu.magistracy.controller.memory;

import kpfu.magistracy.controller.execution.commands.PhysicalAddressingCommand;
import kpfu.magistracy.controller.execution.results.LowLevelResult;

import java.util.List;

/**
 * Interface for classes, which should operate like quantum memory
 */
public interface QuantumMemory {

    long getMaxMemoryFrequency();

    long getMinMemoryFrequency();

    long getFrequencyStep();

    int getProcessingUnitsCount();

    long getMaxMemoryTimeCycle();

    long getMemoryTimeStep();

    void initMemory();

    boolean isMemoryAvailable();

    List<LowLevelResult> perform(List<PhysicalAddressingCommand> physicalAddressingCommands);
}
