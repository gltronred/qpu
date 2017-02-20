package kpfu.magistracy.controller.addresses;

import kpfu.terentyev.quantum.api.KazanModel.QuantumMemoryAddress;

public class GlobalQubitAddress {

    private long globalId;

    private QuantumMemoryAddress quantumMemoryAddress;

    GlobalQubitAddress(long globalId, QuantumMemoryAddress quantumMemoryAddress) {
        this.globalId = globalId;
        this.quantumMemoryAddress = quantumMemoryAddress;
    }

    public long getGlobalId() {
        return globalId;
    }

    public QuantumMemoryAddress getQuantumMemoryAddress() {
        return quantumMemoryAddress;
    }
}
