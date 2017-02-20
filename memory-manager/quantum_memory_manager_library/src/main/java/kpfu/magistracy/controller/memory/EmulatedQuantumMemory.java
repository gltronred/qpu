package kpfu.magistracy.controller.memory;

import kpfu.magistracy.controller.addresses.GlobalQubitAddress;
import kpfu.magistracy.controller.execution.commands.PhysicalAddressingCommand;
import kpfu.magistracy.controller.execution.results.LowLevelResult;
import kpfu.terentyev.quantum.api.KazanModel.Emulator;
import kpfu.terentyev.quantum.api.KazanModel.ProcessingAddress;
import kpfu.terentyev.quantum.api.KazanModel.ProcessingUnitCellAddress;
import kpfu.terentyev.quantum.emulator.Complex;

import java.util.ArrayList;
import java.util.List;

public class EmulatedQuantumMemory implements QuantumMemory {

    private ProcessingAddress tranzistor0_0;
    private ProcessingAddress tranzistor0_1;
    private ProcessingAddress tranzistor0_c;
    private Emulator mEmulator;

    public EmulatedQuantumMemory() {
        mEmulator = new Emulator(getMaxMemoryFrequency(), getMinMemoryFrequency(), getMaxMemoryTimeCycle(), getProcessingUnitsCount());
        tranzistor0_0 = new ProcessingAddress(0, ProcessingUnitCellAddress.Cell0);
        tranzistor0_1 = new ProcessingAddress(0, ProcessingUnitCellAddress.Cell1);
        tranzistor0_c = new ProcessingAddress(0, ProcessingUnitCellAddress.ControlPoint);
    }

    @Override
    public long getMaxMemoryFrequency() {
        return 200;
    }

    @Override
    public long getMinMemoryFrequency() {
        return 50;
    }

    @Override
    public long getFrequencyStep() {
        return 1;
    }

    @Override
    public int getProcessingUnitsCount() {
        return 1;
    }

    @Override
    public long getMaxMemoryTimeCycle() {
        return 50;
    }

    @Override
    public long getMemoryTimeStep() {
        return 1;
    }

    @Override
    public void initMemory() {
        if (mEmulator == null) {
            mEmulator = new Emulator(getMaxMemoryFrequency(), getMinMemoryFrequency(), getMaxMemoryTimeCycle(), getProcessingUnitsCount());
        } else {
            throw new IllegalStateException("Memory already initialized");
        }
    }

    @Override
    public boolean isMemoryAvailable() {
        return mEmulator != null;
    }

    @Override
    public List<LowLevelResult> perform(List<PhysicalAddressingCommand> physicalAddressingCommands) {


        List<LowLevelResult> lowLevelResults = new ArrayList<>();
        for (PhysicalAddressingCommand physicalAddressingCommand : physicalAddressingCommands) {
            switch (physicalAddressingCommand.getCommandType()) {
                case INIT:
                    mEmulator.initLogicalQubit(
                            physicalAddressingCommand.getFirstQubit_Part1().getQuantumMemoryAddress(), new Complex(1, 0), new Complex(0, 0),
                            physicalAddressingCommand.getFirstQubit_Part2().getQuantumMemoryAddress(), new Complex(0, 0), new Complex(1, 0)
                    );
                    System.out.println("INIT command, globalId_1 = " + physicalAddressingCommand.getFirstQubit_Part1().getGlobalId() +
                            ", frequency_1 = " + physicalAddressingCommand.getFirstQubit_Part1().getQuantumMemoryAddress().getFrequency() +
                            ", time_1 = " + physicalAddressingCommand.getFirstQubit_Part1().getQuantumMemoryAddress().getTimeDelay() +
                            ", globalId_2 = " + physicalAddressingCommand.getFirstQubit_Part2().getGlobalId() +
                            ", frequency_2 = " + physicalAddressingCommand.getFirstQubit_Part2().getQuantumMemoryAddress().getFrequency() +
                            ", time_2 = " + physicalAddressingCommand.getFirstQubit_Part2().getQuantumMemoryAddress().getTimeDelay());
                    break;
                case MEASURE:
                    GlobalQubitAddress qubitAddressForMeasure = physicalAddressingCommand.getFirstQubit_Part1();
                    lowLevelResults.add(new LowLevelResult(
                            qubitAddressForMeasure,
                            mEmulator.measure(qubitAddressForMeasure.getQuantumMemoryAddress()) != 0)
                    );
                    System.out.println("Measure command, globalId = " + qubitAddressForMeasure.getGlobalId() +
                            ", frequency = " + qubitAddressForMeasure.getQuantumMemoryAddress().getFrequency() +
                            ", time = " + qubitAddressForMeasure.getQuantumMemoryAddress().getTimeDelay() +
                            ", RESULT = " + lowLevelResults.get(lowLevelResults.size() - 1).isOne());

                    break;
                case QET:
                    mEmulator.load(physicalAddressingCommand.getFirstQubit_Part1().getQuantumMemoryAddress(), tranzistor0_0);
                    mEmulator.load(physicalAddressingCommand.getFirstQubit_Part2().getQuantumMemoryAddress(), tranzistor0_1);
                    mEmulator.QET(0, physicalAddressingCommand.getCommandParam());
                    mEmulator.save(tranzistor0_0, physicalAddressingCommand.getFirstQubit_Part1().getQuantumMemoryAddress());
                    mEmulator.save(tranzistor0_1, physicalAddressingCommand.getFirstQubit_Part2().getQuantumMemoryAddress());

                    System.out.println("QET command, globalId_1 = " + physicalAddressingCommand.getFirstQubit_Part1().getGlobalId() +
                            ", frequency_1 = " + physicalAddressingCommand.getFirstQubit_Part1().getQuantumMemoryAddress().getFrequency() +
                            ", time_1 = " + physicalAddressingCommand.getFirstQubit_Part1().getQuantumMemoryAddress().getTimeDelay() +
                            ", globalId_2 = " + physicalAddressingCommand.getFirstQubit_Part2().getGlobalId() +
                            ", frequency_2 = " + physicalAddressingCommand.getFirstQubit_Part2().getQuantumMemoryAddress().getFrequency() +
                            ", time_2 = " + physicalAddressingCommand.getFirstQubit_Part2().getQuantumMemoryAddress().getTimeDelay() +
                            ", command_param = " + physicalAddressingCommand.getCommandParam());
                    break;
                case PHASE:
                    mEmulator.load(physicalAddressingCommand.getFirstQubit_Part1().getQuantumMemoryAddress(), tranzistor0_0);
                    mEmulator.load(physicalAddressingCommand.getFirstQubit_Part2().getQuantumMemoryAddress(), tranzistor0_1);
                    mEmulator.PHASE(0, physicalAddressingCommand.getCommandParam());
                    mEmulator.save(tranzistor0_0, physicalAddressingCommand.getFirstQubit_Part1().getQuantumMemoryAddress());
                    mEmulator.save(tranzistor0_1, physicalAddressingCommand.getFirstQubit_Part2().getQuantumMemoryAddress());

                    System.out.println("PHASE command, globalId_1 = " + physicalAddressingCommand.getFirstQubit_Part1().getGlobalId() +
                            ", frequency_1 = " + physicalAddressingCommand.getFirstQubit_Part1().getQuantumMemoryAddress().getFrequency() +
                            ", time_1 = " + physicalAddressingCommand.getFirstQubit_Part1().getQuantumMemoryAddress().getTimeDelay() +
                            ", globalId_2 = " + physicalAddressingCommand.getFirstQubit_Part2().getGlobalId() +
                            ", frequency_2 = " + physicalAddressingCommand.getFirstQubit_Part2().getQuantumMemoryAddress().getFrequency() +
                            ", time_2 = " + physicalAddressingCommand.getFirstQubit_Part2().getQuantumMemoryAddress().getTimeDelay() +
                            ", command_param = " + physicalAddressingCommand.getCommandParam());
                    break;
                case CQET:
                    mEmulator.load(physicalAddressingCommand.getFirstQubit_Part1().getQuantumMemoryAddress(), tranzistor0_0);
                    mEmulator.load(physicalAddressingCommand.getFirstQubit_Part2().getQuantumMemoryAddress(), tranzistor0_1);
                    mEmulator.load(physicalAddressingCommand.getSecondQubit_Part1().getQuantumMemoryAddress(), tranzistor0_c);
                    mEmulator.PHASE(0, physicalAddressingCommand.getCommandParam());
                    mEmulator.save(tranzistor0_0, physicalAddressingCommand.getFirstQubit_Part1().getQuantumMemoryAddress());
                    mEmulator.save(tranzistor0_1, physicalAddressingCommand.getFirstQubit_Part2().getQuantumMemoryAddress());
                    mEmulator.save(tranzistor0_c, physicalAddressingCommand.getSecondQubit_Part1().getQuantumMemoryAddress());

                    System.out.println("PHASE command, globalId_1 = " + physicalAddressingCommand.getFirstQubit_Part1().getGlobalId() +
                            ", frequency_1 = " + physicalAddressingCommand.getFirstQubit_Part1().getQuantumMemoryAddress().getFrequency() +
                            ", time_1 = " + physicalAddressingCommand.getFirstQubit_Part1().getQuantumMemoryAddress().getTimeDelay() +
                            ", globalId_2 = " + physicalAddressingCommand.getFirstQubit_Part2().getGlobalId() +
                            ", frequency_2 = " + physicalAddressingCommand.getFirstQubit_Part2().getQuantumMemoryAddress().getFrequency() +
                            ", time_2 = " + physicalAddressingCommand.getFirstQubit_Part2().getQuantumMemoryAddress().getTimeDelay() +
                            ", globalId_c = " + physicalAddressingCommand.getSecondQubit_Part1().getGlobalId() +
                            ", frequency_c = " + physicalAddressingCommand.getSecondQubit_Part1().getQuantumMemoryAddress().getFrequency() +
                            ", time_c = " + physicalAddressingCommand.getSecondQubit_Part1().getQuantumMemoryAddress().getTimeDelay() +
                            ", command_param = " + physicalAddressingCommand.getCommandParam());
                    break;
            }
        }
        return lowLevelResults;
    }

}
