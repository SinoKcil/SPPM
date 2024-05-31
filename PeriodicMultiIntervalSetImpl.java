package inherit;

import interval.IntervalSet;
import multi.CommonMultiIntervalSet;

import java.io.IOException;
import java.util.List;

public class PeriodicMultiIntervalSetImpl<L>{
    private final int periodTime;
    private final int cycleNum;
    /*
    * Abstract function:
    *   AF(P):一个由周期和周期长度确定的周期性时间轴。periodTime为周期长度，cycleNum为周期数量。
    *         起始时间为0，结束时间为periodTime*cycleNum-1
    * Rep invariant：
    *   1.periodTime>0,cycleNum>0
    *   2.周期性的时间不允许跨周期，即周期性时间的end-start不超过periodTime-1
    * Safety from rep exposure:
    *   1.字段均为private final，且为不可变类型
    *   2.observer不涉及内部字段，或使用防御性拷贝
    * */
    /**
     *
     * @param cycle 周期数量，应大于0
     * @param period 周期长度，应大于0
     * @throws IOException 周期长度或周期数量小于等于0
     */
    public PeriodicMultiIntervalSetImpl(int cycle,int period) throws IOException{
        if(period<=0||cycle<=0) throw new IOException("周期设置错误");
        this.periodTime=period;
        this.cycleNum=cycle;
    }
    @Override
    public void insert(long start,long end,L label) throws IOException{
        if(end>=periodTime*cycleNum) throw new IOException("结束时间不应大于周期时间轴最长时间");
        super.insert(start,end,label);
    }

    @Override
    public void insert(long start, long end, L label, int cycleNum, int cycleStart) throws IOException {
        if(end>=periodTime) throw new IOException("起止时间不在周期范围内");
        if(cycleStart<=0||cycleNum<=0) throw new IOException("周期输入不合法");
        if(cycleStart>this.cycleNum||cycleStart+cycleNum-1>this.cycleNum) throw new IOException("周期长度超过能设置的最长周期");
        long cStart=(cycleStart-1)*periodTime;
        for(int i=0;i<cycleNum;i++){
            insert(cStart+start,cStart+end,label);//start和end的基本规范交给原生insert实现
            cStart+=periodTime;
        }
    }
}
