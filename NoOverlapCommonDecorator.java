package decorator.interval;

import interval.IntervalSet;

import java.io.IOException;
import java.util.List;

/**
 * 为Set增加不可重叠的特性
 * <p>若一个事件标签结束前，另一个事件标签开始了，则存在重叠
 * @param <L> Set的类型，应为不可变类型
 */
public class NoOverlapCommonDecorator<L>
        extends Decorator<L>
        implements Set<L> {
    /*
    * Abstract function:
    *   AF=将传入的MultiIntervalSet装饰为各事件不能重叠的时间轴
    * Rep invariant:
    *   1.传入的基本对象不能为空
    * Safety from rep exposure:
    *   1.Observer中拷贝的类型L为不可变类型
    * */

    /**
     *
     * @param intervalSet 基础功能的实现对象
     * @throws IOException 传入的基本对象不合法
     */
    public NoOverlapCommonDecorator(IntervalSet<L> intervalSet) throws IOException{
        super(intervalSet);
        if(intervalSet==null) throw new IOException("传入的基本对象不能为空");
        long lastEnd=0;
        for(L label:intervalSet.sortedLabels()){
            long start=intervalSet.start(label);
            long end= intervalSet.end(label);
            if(start<lastEnd) throw new IOException("传入的基本对象存在重叠！");
            lastEnd=end;
        }
    }

    /**
     *
     * @param start 标签在时间轴上的起始时间，应早于终止时间
     * @param end 标签在时间轴上的终止时间，应晚于起始时间
     * @param label 标签元素
     * @throws IOException start<0或end<=start，或新增时间与已有时间有重叠(下一段时间的起始时间必须严格大于上一段时间的结束)
     */
    @Override
    public void insert(long start,long end,L label) throws IOException{
        long originStart=-1;
        long originEnd=-1;
        if(intervalSet.labels().contains(label)){
            originStart=intervalSet.start(label);
            originEnd=intervalSet.end(label);
        }
        super.insert(start,end,label);
        //由于装饰器在外部，不能访问内部rep，所以只能检查而不能重新插入
        boolean overlap=false;
        List<L> sorted=sortedLabels();
        for(int i=0;i<sorted.size();i++){
            if(sorted.get(i).equals(label)){
                if(i>0){
                    long lastEnd=intervalSet.end(sorted.get(i-1));
                    if(start<lastEnd) overlap=true;
                }
                if(i<sorted.size()-1){
                    long nextStart=intervalSet.start(sorted.get(i+1));
                    if(end>nextStart) overlap=true;
                }
                break;
            }
        }
        if(overlap){
            intervalSet.remove(label);
            //如果原本有这个标签，还原
            if(originStart!=-1&&originEnd!=-1) super.insert(start,end,label);
            throw new IOException("输入的时间有重叠！");
        }
    }
}
