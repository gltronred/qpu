package kpfu.magistracy.service_for_controller.results;

/**
 * measure result for two physical qubits = one logical qubit
 */
public class TopLevelResult {

    private boolean mQubit_1MeasureResult;
    private boolean mQubit_2MeasureResult;

    public boolean getQubit_1MeasureResult() {
        return mQubit_1MeasureResult;
    }

    public void setQubit_1MeasureResult(boolean qubit_1MeasureResult) {
        this.mQubit_1MeasureResult = qubit_1MeasureResult;
    }

    public boolean getQubit_2MeasureResult() {
        return mQubit_2MeasureResult;
    }

    public void setQubit_2MeasureResult(boolean qubit_2MeasureResult) {
        this.mQubit_2MeasureResult = qubit_2MeasureResult;
    }
}
