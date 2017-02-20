package kpfu.magistracy.controller.addresses;

import kpfu.magistracy.controller.memory.QuantumMemory;
import kpfu.magistracy.service_for_controller.addresses.LogicalQubitAddressForController;
import kpfu.terentyev.quantum.api.KazanModel.MemoryHalf;
import kpfu.terentyev.quantum.api.KazanModel.QuantumMemoryAddress;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class for creating memory addresses, keeping memory state, mapping logical & global addresses
 */
public class MemoryStateKeeper {

    private Map<GlobalQubitAddress, LogicalQubitAddressForController> mMemoryAddresses;

    private Map<LogicalQubitAddressForController, GlobalQubitAddress> mMemoryAddressesReverse;

    private Set<GlobalQubitAddress> initializedQubits;

    private int mMaxQubitCount;

    private Long mFrequencyToUse;

    private Long mTimeToUse;

    private int globalId;

    private QuantumMemory mQuantumMemory;

    public MemoryStateKeeper(QuantumMemory quantumMemory) {
        globalId = 0;
        mQuantumMemory = quantumMemory;

        mFrequencyToUse = quantumMemory.getMinMemoryFrequency();
        mTimeToUse = 0L;

        mMaxQubitCount = (int) Math.min(
                mQuantumMemory.getMaxMemoryTimeCycle() / mQuantumMemory.getMemoryTimeStep(),
                (mQuantumMemory.getMaxMemoryFrequency() - mQuantumMemory.getMinMemoryFrequency()) / mQuantumMemory.getFrequencyStep());

        mMemoryAddresses = new HashMap<>();
        mMemoryAddressesReverse = new HashMap<>();

        initializedQubits = new HashSet<>();

    }

    public GlobalQubitAddress getGlobalAddressForQubit(LogicalQubitAddressForController qubitLogicalAddress) {
        if (mMemoryAddressesReverse.containsKey(qubitLogicalAddress))
            return mMemoryAddressesReverse.get(qubitLogicalAddress);
        if (mFrequencyToUse > mQuantumMemory.getMaxMemoryFrequency() || mTimeToUse > mQuantumMemory.getMaxMemoryTimeCycle())
            throw new IllegalStateException("Wrong attempt to add another one qubit while memory is completely full");

        QuantumMemoryAddress quantumMemoryAddress;
        switch (qubitLogicalAddress.getMemoryPart()) {
            case 0:
                quantumMemoryAddress = new QuantumMemoryAddress(mFrequencyToUse, mTimeToUse, MemoryHalf.HALF_0);
                break;
            case 1:
                quantumMemoryAddress = new QuantumMemoryAddress(mFrequencyToUse, mTimeToUse, MemoryHalf.HALF_1);
                break;
            default:
                throw new IllegalArgumentException("Memory part in logical qubit address isn't equals to 0 or 1");
        }
        GlobalQubitAddress globalQubitAddress = new GlobalQubitAddress(globalId++, quantumMemoryAddress);
        mMemoryAddresses.put(globalQubitAddress, qubitLogicalAddress);
        mMemoryAddressesReverse.put(qubitLogicalAddress, globalQubitAddress);

        mFrequencyToUse += mQuantumMemory.getFrequencyStep();
        mTimeToUse += mQuantumMemory.getMemoryTimeStep();

        return globalQubitAddress;
    }

    public void onQubitInitialized(GlobalQubitAddress qubitAddress) {
        initializedQubits.add(qubitAddress);
    }

    public boolean needInitializeLogicalQubit(GlobalQubitAddress qubitPart_1, GlobalQubitAddress qubitPart_2) {
        if (initializedQubits.contains(qubitPart_1) && initializedQubits.contains(qubitPart_2)) return false;
        if (initializedQubits.contains(qubitPart_1) && !initializedQubits.contains(qubitPart_2) || !initializedQubits.contains(qubitPart_1) && initializedQubits.contains(qubitPart_2))
            throw new IllegalStateException("Qubits cannot be initialize partially");
        return true;
    }

    public int getMaxQubitCount() {
        return mMaxQubitCount;
    }

    public void clearMemoryParams() {
        mFrequencyToUse = mQuantumMemory.getMinMemoryFrequency();
        mTimeToUse = 0L;
    }

    public void clearMemoryData() {
        mMemoryAddresses.clear();
        mMemoryAddressesReverse.clear();
        initializedQubits.clear();
        globalId = 0;
    }

    public LogicalQubitAddressForController getLogicalQubitAddressByPhysical(GlobalQubitAddress globalQubitAddress) {
        return mMemoryAddresses.get(globalQubitAddress);
    }


}
