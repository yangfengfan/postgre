import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class SpiltDateUtil {
    public static void main(String[] args) {
        List<KeyValueForDate> list = SpiltDateUtil.getKeyValueForDate("2017-08-12 ","2017-12-21 ");
        System.out.println("开始日期--------------结束日期");
        for (int t=0; t<=list.size()-1;t++) {
            System.out.println(list.get(t).getStartDate()+"--------"+list.get(t+1).getStartDate());
        }
    }
    public static List<KeyValueForDate> getKeyValueForDate(String startDate,String endDate) {
        List<KeyValueForDate> list = null;
        try {
            list = new ArrayList<KeyValueForDate>();

            String firstDay = "";
            String lastDay = "";
            Date dstart = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);// 定义起始日期
            Date dend = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);// 定义结束日期

            Calendar dfirst = Calendar.getInstance();// 定义日期实例
            dfirst.setTime(dstart);// 设置日期起始时间
            Calendar cale = Calendar.getInstance();
            Calendar dlast = Calendar.getInstance();
            dlast.setTime(dend);

            //int startDay =  dstart.getDate();
            //int endDay = dend.getDate();
            int startDay = dfirst.get(Calendar.DAY_OF_MONTH);
            int endDay = dlast.get(Calendar.DAY_OF_MONTH);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            KeyValueForDate keyValueForDate = null;

            while (dfirst.getTime().before(dend)) {// 判断是否到结束日期
                keyValueForDate = new KeyValueForDate();
                cale.setTime(dfirst.getTime());

                if(dfirst.getTime().equals(dstart)){
                    cale.set(Calendar.DAY_OF_MONTH, dfirst
                            .getActualMaximum(Calendar.DAY_OF_MONTH));
                    lastDay = sdf.format(cale.getTime());
                    keyValueForDate.setStartDate(sdf.format(dstart));
                    keyValueForDate.setEndDate(lastDay);

                }else if(dfirst.get(Calendar.MONTH) == dend.getMonth() && dfirst.get(Calendar.YEAR) == dlast.get(Calendar.YEAR)){
                    cale.set(Calendar.DAY_OF_MONTH,1);//取第一天
                    firstDay = sdf.format(cale.getTime());

                    keyValueForDate.setStartDate(firstDay);
                    keyValueForDate.setEndDate(sdf.format(dend));

                }else {
                    cale.set(Calendar.DAY_OF_MONTH,1);//取第一天
                    firstDay = sdf.format(cale.getTime());

                    cale.set(Calendar.DAY_OF_MONTH, dfirst
                            .getActualMaximum(Calendar.DAY_OF_MONTH));
                    lastDay = sdf.format(cale.getTime());

                    keyValueForDate.setStartDate(firstDay);
                    keyValueForDate.setEndDate(lastDay);

                }
                list.add(keyValueForDate);
                dfirst.add(Calendar.MONTH, 1);// 进行当前日期月份加1

            }

            if(endDay<startDay){
                keyValueForDate = new KeyValueForDate();

                cale.setTime(dend);
                cale.set(Calendar.DAY_OF_MONTH,1);//取第一天
                firstDay = sdf.format(cale.getTime());

                keyValueForDate.setStartDate(firstDay);
                keyValueForDate.setEndDate(sdf.format(dend));
                list.add(keyValueForDate);
            }
        } catch (ParseException e) {
            return null;
        }

        return list;
    }

}

