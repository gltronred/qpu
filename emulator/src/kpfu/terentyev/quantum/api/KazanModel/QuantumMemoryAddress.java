package kpfu.terentyev.quantum.api.KazanModel;

/**
 * Created by aleksandrterentev on 01.04.16.
 */
public class QuantumMemoryAddress {
    private double frequency;
    private double timeDelay;
    private MemoryHalf memoryHalf;

    public double getFrequency() {
        return frequency;
    }

    public double getTimeDelay() {
        return timeDelay;
    }

    public MemoryHalf getMemoryHalf(){
        return memoryHalf;
    }

    public QuantumMemoryAddress(double frequency, double timeDelay, MemoryHalf memoryHalf) {
        this.memoryHalf = memoryHalf;
        this.frequency = frequency;
        this.timeDelay = timeDelay;
    }
}
