package kpfu.terentyev.quantum.api.KazanModel;

/**
 * Created by aleksandrterentev on 29.03.16.
 */
public class ProcessingUnit {

    private ProcessingUnitCell cell0 = new ProcessingUnitCell();
    private ProcessingUnitCell controlPoint = new ProcessingUnitCell();
    private ProcessingUnitCell cell1 = new ProcessingUnitCell();

    QuantumProccessorHelper processorHelper;

    ProcessingUnit(QuantumProccessorHelper processorHelper){
        this.processorHelper = processorHelper;
    }

    ProcessingUnitCell cellForUnitAddress(ProcessingUnitCellAddress address){
        switch (address){
            case Cell0:
                return cell0;
            case ControlPoint:
                return controlPoint;
            case Cell1:
                return cell1;
        }
        return null;
    }

    private boolean areCellsReadyToPerformTransformation (boolean checkControlledPoint){
        boolean result = cell0.getQubit() != null && cell1.getQubit() != null;
        if (checkControlledPoint){
            result = result && controlPoint.getQubit() != null;
        }
        return result;
    }

    private void checkCells(boolean checkControlledPoint) throws Exception {
        if (!areCellsReadyToPerformTransformation(checkControlledPoint)){
            throw new Exception("Cells are not ready! Qubits not loaded");
        }
    }

    /*
    * Следующие методы кидают исключение только если количество загруженных в регистры кубитов не совпадает с необходимым
    *
    * Для дебага алгоритмов хорошо бы ещё проверять правильные ли кубиты загружены в регистры процессора.
    * Но на реальной установке невозможно проверить даже наличие кубитов в регистрах. Так что на уровне эмулятора
    * идёт просто вызов операций без какой-либо информации о результате выполнения.
    *
    * При данной реализации эмулятора преобразование может быть выполнено, если в процессор загружены даже "не те кубиты",
    * но их количества достаточно для выполнения операции.
    *
    * В идеале нужно посмотреть и промоделировать случаи, когда в блоке 1 - 2 кубита,
    * которые по ошибке прораммиста остались в процессоре и как на них влияют физические операции.
    * */

    void QET(double phase) throws Exception {
        checkCells(false);
        processorHelper.physicalQET(cell0.getQubit(), cell1.getQubit(), phase);
    }

    void cQET (double phase) throws Exception {
        checkCells(true);
        processorHelper.physicalCQET(cell0.getQubit(), controlPoint.getQubit(), cell1.getQubit(), phase);
    }

    void PHASE (double phase) throws Exception {
        checkCells(false);
        processorHelper.physicalPHASE(cell0.getQubit(), cell1.getQubit(), phase);
    }
}
