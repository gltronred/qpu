package kpfu.terentyev.quantum.api.KazanModel;

/**
 * Created by aleksandrterentev on 01.04.16.
 */
public class QuantumMemoryInfo {
    private double maximumAvailableFrequency;
    private double minimumAvailableFrequency;

    public QuantumMemoryInfo(double maximumAvailableFrequency, double minimumAvailableFrequency, double timeInterval) {
        this.maximumAvailableFrequency = maximumAvailableFrequency;
        this.minimumAvailableFrequency = minimumAvailableFrequency;
        this.timeInterval = timeInterval;
    }



    public double getMaximumAvailableFrequency() {

        return maximumAvailableFrequency;
    }

    public void setMaximumAvailableFrequency(double maximumAvailableFrequency) {
        this.maximumAvailableFrequency = maximumAvailableFrequency;
    }

    public double getMinimumAvailableFrequency() {
        return minimumAvailableFrequency;
    }

    public void setMinimumAvailableFrequency(double minimumAvailableFrequency) {
        this.minimumAvailableFrequency = minimumAvailableFrequency;
    }

    public double getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(double timeInterval) {
        this.timeInterval = timeInterval;
    }

    private double timeInterval;
}
