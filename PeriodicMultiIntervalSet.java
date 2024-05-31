package inherit;

import multi.MultiIntervalSet;

import java.io.IOException;

/**
 * 周期性的多时间段时间轴，默认时间从第一个周期开始，到最大周期数量结束
 * <p>时间轴的周期长度和周期时间在创建时被确定，无法修改
 * @param <L> 时间段代表的事件的标签类型
 */
public interface PeriodicMultiIntervalSet<L>
    extends MultiIntervalSet<L> {
    /**
     * 插入事件和时间
     * @param start 标签在时间轴上的起始时间
     * @param end 标签在时间轴上的终止之间,应小于等于周期长度和个数决定的最长时间
     * @param label 标签元素
     * @throws IOException start<0或end<=start或end大于周期长度和个数决定的最长时间
     */
    void insert(long start,long end,L label) throws IOException;
    /**
     * 周期性地插入一个事件，事件的时间范围应在一个周期以内
     * @param start 事件在一个周期内的起始时间，应在大于等于0，一个周期的范围内，并且小于end
     * @param end 事件在一个周期内的结束时间，应在一个周期的范围内，小于周期长度，并且大于start
     * @param label 事件标签
     * @param cycleNum 事件持续的周期数，不超过时间轴的最大周期数
     * @param cycleStart 事件起始的周期，不小于1
     * @throws IOException 输入时间或周期规定不合法
     */
    void insert(long start,long end,L label,int cycleNum,int cycleStart) throws IOException;

    /**
     * 检查某个事件的出现时间是否具有周期性以及周期性出现的时间范围
     * @param label 待查询事件的标签
     * @return 数组大小为4，如果事件的时间具有周期性，分别表示<p>起始周期、持续周期数、起始时间、结束时间</p>如果不存在周期性，数组值为-1、-1、-1、-1
     * @throws IOException 时间轴上不存在该事件
     */
    long[] checkCycle(L label) throws IOException;
}
