package kpfu.terentyev.quantum.api.KazanModel;

/**
 * Created by aleksandrterentev on 30.03.16.
 */

public class ProcessingAddress {
    private int proccessingUnitNumber;

    public ProcessingUnitCellAddress getProccessingUnitCellAddress() {
        return proccessingUnitCellAddress;
    }

    private ProcessingUnitCellAddress proccessingUnitCellAddress;

    public int getProccessingUnitNumber() {
        return proccessingUnitNumber;
    }

    public ProcessingAddress(int proccessingUnitNumber, ProcessingUnitCellAddress proccessingUnitCellAddress) {
        this.proccessingUnitNumber = proccessingUnitNumber;
        this.proccessingUnitCellAddress = proccessingUnitCellAddress;
    }
}
