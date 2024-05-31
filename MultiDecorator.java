package decorator.multi;

import interval.IntervalSet;
import multi.MultiIntervalSet;

import java.io.IOException;
import java.util.Set;

public class MultiDecorator<L> implements MultiIntervalSet<L> {
    protected final MultiIntervalSet<L> multiIntervalSet;
    public MultiDecorator(MultiIntervalSet<L> multiIntervalSet){
        this.multiIntervalSet=multiIntervalSet;
    }

    @Override
    public void insert(long start, long end, L label) throws IOException {
        this.multiIntervalSet.insert(start,end,label);
    }

    @Override
    public boolean remove(L label) {
        return this.multiIntervalSet.remove(label);
    }

    @Override
    public Set<L> labels() {
        return this.multiIntervalSet.labels();
    }

    @Override
    public IntervalSet<Integer> intervals(L label) {
        return this.multiIntervalSet.intervals(label);
    }

    @Override
    public long totalInterval(L label) {
        return this.multiIntervalSet.totalInterval(label);
    }
}
