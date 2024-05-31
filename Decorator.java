package decorator.interval;

import interval.IntervalSet;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 装饰器模式，为IntervalSet增加新功能，需要传入基础的IntervalSet的实现
 * @param <L> 元素类型，应与传入的IntervalSet的类型相同，为不可变类型
 */
public abstract class Decorator<L> implements IntervalSet<L> {
    protected final IntervalSet<L> intervalSet;//委派给别人
    public Decorator(IntervalSet<L> intervalSet){
        this.intervalSet = intervalSet;
    }

    @Override
    public void insert(long start, long end, L label) throws IOException {
        intervalSet.insert(start,end,label);
    }

    @Override
    public boolean remove(L label) {
        return intervalSet.remove(label);
    }

    @Override
    public long start(L label) {
        return intervalSet.start(label);
    }

    @Override
    public long end(L label) {
        return intervalSet.end(label);
    }

    @Override
    public Set<L> labels() {
        return intervalSet.labels();
    }

    @Override
    public List<L> sortedLabels(){
        return intervalSet.sortedLabels();
    }

}
