package com.denger.client.another.hooks;

import net.minecraft.profiler.*;

import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

public class TimeTrackerHook extends TimeTracker {

    private final LongSupplier realTime;
    private final IntSupplier tickCount;
    private IResultableProfiler profiler = EmptyProfiler.INSTANCE;

    public TimeTrackerHook(LongSupplier p_i231483_1_, IntSupplier p_i231483_2_) {
        super(p_i231483_1_, p_i231483_2_);
        this.realTime = p_i231483_1_;
        this.tickCount = p_i231483_2_;
    }

    public boolean isEnabled() {
        return this.profiler != ProfilerHook.INSTANCE;
    }

    public void disable() {
        this.profiler = ProfilerHook.INSTANCE;
    }

    public void enable() {
        this.profiler = new ProfilerHook();
    }

    public IProfiler getFiller() {
        return this.profiler;
    }

    public IProfileResult getResults() {
        return this.profiler.getResults();
    }

}
