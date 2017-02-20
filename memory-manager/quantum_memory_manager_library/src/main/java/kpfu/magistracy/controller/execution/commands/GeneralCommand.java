package kpfu.magistracy.controller.execution.commands;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GeneralCommand<T> {
    @Nonnull
    private CommandTypes mCommandType;

    @Nonnull
    private Double mCommandParam;

    @Nonnull
    private T mFirstQubit_Part1;
    @Nonnull
    private T mFirstQubit_Part2;
    @Nullable
    private T mSecondQubit_Part1;
    @Nullable
    private T mSecondQubit_Part2;

    public CommandTypes getCommandType() {
        return mCommandType;
    }

    void setCommandType(CommandTypes mCommandType) {
        this.mCommandType = mCommandType;
    }

    public Double getCommandParam() {
        return mCommandParam;
    }

    void setCommandParam(Double mCommandParam) {
        this.mCommandParam = mCommandParam;
    }

    public T getFirstQubit_Part1() {
        return mFirstQubit_Part1;
    }

    void setFirstQubit_Part1(T mFirstQubit_Part1) {
        this.mFirstQubit_Part1 = mFirstQubit_Part1;
    }

    public T getFirstQubit_Part2() {
        return mFirstQubit_Part2;
    }

    void setFirstQubit_Part2(T mFirstQubit_Part2) {
        this.mFirstQubit_Part2 = mFirstQubit_Part2;
    }

    public T getSecondQubit_Part1() {
        return mSecondQubit_Part1;
    }

    void setSecondQubit_Part1(T mSecondQubit_Part1) {
        this.mSecondQubit_Part1 = mSecondQubit_Part1;
    }

    T getSecondQubit_Part2() {
        return mSecondQubit_Part2;
    }

    void setSecondQubit_Part2(T mSecondQubit_Part2) {
        this.mSecondQubit_Part2 = mSecondQubit_Part2;
    }

    boolean isSecondLogicalQubitNull() {
        if (mSecondQubit_Part1 == null && mSecondQubit_Part2 != null || mSecondQubit_Part1 != null && mSecondQubit_Part2 == null)
            throw new IllegalStateException("Logical qubit cannot be written partially, one part is null");
        return mSecondQubit_Part1 == null && mSecondQubit_Part2 == null;
    }

}
