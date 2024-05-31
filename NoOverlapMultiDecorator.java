package decorator.multi;

import decorator.interval.NoOverlapCommonDecorator;
import interval.CommonIntervalSet;
import interval.IntervalSet;
import multi.MultiIntervalSet;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 为MultiIntervalSet增加不可重叠的特性
 * <p>若一个事件标签结束前，另一个事件标签开始了，则存在重叠
 * @param <L> MultiIntervalSet的类型，应为不可变类型
 */
public class NoOverlapMultiDecorator<L>
        extends MultiDecorator<L>
        implements MultiIntervalSet<L> {
    private IntervalSet<Integer> intervalSet;
    private int num=0;
    /*
     * Abstract function:
     *   AF=将传入的MultiIntervalSet装饰为各事件不能重叠的时间轴,
     *      intervalSet集合了MultiIntervalSet的时间段，且是不可重叠的,num表示时间轴上时间的总段数
     * Rep invariant:
     *   1.传入的基本对象不能为空
     *   2.intervalSet的标签数量与num一致
     * Safety from rep exposure:
     *   1.Observer中拷贝的类型L为不可变类型
     *   2.所有变量设置为private
     * */
    private void checkRep(){
        if(multiIntervalSet==null) throw new AssertionError("传入对象不能为空");
        if(intervalSet.labels().size()!=num) throw new AssertionError("实际数量不一致");
    }
    /**
     * 构造不可重叠的时间轴
     * @param multiIntervalSet 实现的基本对象
     * @throws IOException 传入对象存起止时间不合法，或传入对象存在重叠
     */
    public NoOverlapMultiDecorator(MultiIntervalSet<L> multiIntervalSet) throws IOException {
        super(multiIntervalSet);
        Set<L> labels=multiIntervalSet.labels();
        intervalSet=new NoOverlapCommonDecorator<>(new CommonIntervalSet<>());
        for(L label:labels){
            IntervalSet<Integer> labelSet=multiIntervalSet.intervals(label);
            List<Integer> list=labelSet.sortedLabels();
            for (Integer integer : list) {
                long start = labelSet.start(integer);
                long end = labelSet.end(integer);
                intervalSet.insert(start,end,num++);
            }
        }
        checkRep();
    }

    @Override
    public void insert(long start,long end,L label) throws IOException{
        intervalSet.insert(start,end,num++);//先检查是否重叠，如果重叠，直接退出
        super.insert(start,end,label);
    }
    @Override
    public boolean remove(L remove){
        try{
            if(!super.remove(remove)) return false;//本来就没有这个标签
            Set<L> labels=multiIntervalSet.labels();
            intervalSet=new NoOverlapCommonDecorator<>(new CommonIntervalSet<>());
            num=0;
            for(L label:labels){//重新构建时间轴
                IntervalSet<Integer> labelSet=multiIntervalSet.intervals(label);
                List<Integer> list=labelSet.sortedLabels();
                for (Integer integer : list) {
                    long start = labelSet.start(integer);
                    long end = labelSet.end(integer);
                    intervalSet.insert(start,end,num++);
                }
            }
            checkRep();
        }catch (IOException e){
            throw new AssertionError("移除错误");
        }
        return true;
    }
}
