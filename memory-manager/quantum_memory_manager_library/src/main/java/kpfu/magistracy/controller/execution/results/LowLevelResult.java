package kpfu.magistracy.controller.execution.results;

import kpfu.magistracy.controller.addresses.GlobalQubitAddress;

public class LowLevelResult {


    private GlobalQubitAddress mGlobalQubitAddress;

    private boolean mMeasureResult; // true = 1, false = 0

    public LowLevelResult(GlobalQubitAddress globalQubitAddress, boolean measureResult) {
        this.mGlobalQubitAddress = globalQubitAddress;
        this.mMeasureResult = measureResult;
    }

    GlobalQubitAddress getGlobalQubitAddress() {
        return mGlobalQubitAddress;
    }

    public boolean isOne() {
        return mMeasureResult;
    }
}
