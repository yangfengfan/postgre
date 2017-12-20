import java.sql.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
public class TestSpringBlob {
    static String url = "jdbc:postgresql://10.10.100.32:5432/electricdb";
    static String usr = "electric";
    static String psd = "electric";
    static String CollectionBoer = "collections_boer";
    static String ConsumptionBoer = "consumption_boer";
    static String ElectricprojectBoer = "electricproject_boer";
    static String LoopBoer = "loop_boer";
    static String BianYaQiBoer = "bianyaqi_boer";
    static String CapacitancepamBoer = "capacitancepam_boer";
    static String HighLowVoltage = "highlowvoltage_boer";
    static String HighVoltage = "highvoltagesystem_boer";
    static String LowVoltageSystemBoer = "lowvoltagesystem_boer";
    static String PowerRoomBoer = "power_room_boer";
    static String PowerFactor = "powerfactor_boer";
    static String PowerValue = "powervalue_boer";
    static String EpValueOfCollect = "epvalueofcollection_boer";
    public static void main(String args[]) {
        findProectName("盛锦三配",EpValueOfCollect,"createtime < '2017-12-01 00:00:00' ");
        //郑港
        //锦绣二配
        //盛锦二配
        //盛锦三配
    }
    public static void findProectName(String projectName ,String tableName, String time ){
        Connection conn = null;
        List<List<Object>> dataList = new ArrayList<List<Object>>();
        List<Object> rowList = null;
        List<List<Object>> dataListhead = new ArrayList<List<Object>>();
        List<Object> rowListhead = null;

        List<List<Object>> dataList1 = new ArrayList<List<Object>>();
        List<Object> rowList1 = null;
        List<List<Object>> dataListhead1 = new ArrayList<List<Object>>();
        List<Object> rowListhead1 = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, usr, psd);
//            Statement statement = conn.createStatement();
            //Statement st = conn.createStatement();
            //查看某张表中，字段name为？数据
            String sql = "SELECT * FROM "+tableName+" WHERE "+time+" AND deleteflag = '0' AND datapoint_id in\n" +
                    "(SELECT name FROM collections_boer WHERE deleteflag = '0' AND loop_id in\n" +
                    "(SELECT id FROM loop_boer WHERE deleteflag = '0' AND electricproject_id in\n" +
                    "(SELECT id FROM electricproject_boer WHERE deleteflag = '0' AND name LIKE ?)\n" +
                    ")\n" +
                    ")";
            String sql1 = "SELECT name FROM collections_boer WHERE deleteflag = '0' AND loop_id in\n" +
                    "(SELECT id FROM loop_boer WHERE deleteflag = '0' AND electricproject_id in\n" +
                    "(SELECT id FROM electricproject_boer WHERE deleteflag = '0' AND name LIKE ? )\n" +
                    ")";
//            String sql = "select * from "+tableName+" where name like ?";
            //name中包含?,查看该?的loop_id在某张表中的数据
            //String sql = "SELECT * FROM "+tableName+" WHERE loop_id in (SELECT id FROM loop_boer WHERE electricproject_id = (SELECT id FROM electricproject_boer WHERE name LIKE  ?)) order by createtime desc";
            //查看?项目在某张表的数据
            //String sql ="select * from "+tableName+" l where l.electricproject_id = (select id from electricproject_boer e where e.name like ?) order by createtime desc ";
            //name为?的electricproject_id,在某张表中的数据
            //String sql = "select * from "+tableName+" c where c.electricproject_id = (select id from electricproject_boer e where e.name like ?)  order by createtime desc,type asc";
            //name -> electricproject_id ->project_id
            //String sql = "select * from "+tableName+" p where p.project_id = (select id from electricproject_boer e where e.name like ?) order by createtime desc";
            //name -> electricproject_id -> loop_id
            //String sql = "select * from "+tableName+" p where p.loop_id in (select id from loop_boer l where l.electricproject_id = (select id from electricproject_boer e where e.name like ?)) order by createtime desc,loop_id desc";
            PreparedStatement pstmt = conn.prepareStatement(sql1);
            //ResultSet卷动
            //PreparedStatement pstmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //rs.last();// 移动到最后
            //System.out.println(rs.getRow());// 获得结果集长度
            pstmt.setString(1,"%"+projectName+"%");
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData data = rs.getMetaData();
            int rowL = data.getColumnCount();
            rowListhead = new ArrayList<Object>();
            for ( int i=1;i <= rowL; i++) {
                rowListhead.add(data.getColumnLabel(i));
                dataListhead.add(rowListhead);
            }
            System.out.println();

            while (rs.next()) {
                rowList = new ArrayList<Object>();
                for ( int i=1;i <= rowL; i++) {
                    System.out.print(rs.getString(data.getColumnLabel(i)));
                    System.out.print("  ");
                    rowList.add(rs.getString(data.getColumnLabel(i)));
                }
                System.out.println("");
                dataList.add(rowList);
            }
            System.out.println(dataList);
            System.out.println(dataList.get(1).get(0));

            rowList1 = new ArrayList<Object>();
            for (int i=0;i <= dataList.size()-1; i++){
                String sql2 = "SELECT * FROM epvalueofcollection_boer WHERE createtime >=  '2017-08-01 00:00:00' AND  createtime<'2017-09-01 00:00:00' AND deleteflag = '0' AND datapoint_id =  "+"'"+dataList.get(i).get(0)+"'"+"ORDER  BY createtime";
                Statement statement = conn.createStatement();
                ResultSet rs1 = statement.executeQuery(sql2);
                System.out.println(rs1);
                ResultSetMetaData data1 = rs1.getMetaData();
                int rowL1 = data1.getColumnCount();

                while (rs1.next()) {
                    rowList1 = new ArrayList();
                    for ( int b=1;b <= rowL1; b++) {
                       // System.out.print(rs1.getString(data1.getColumnLabel(b)));
                       // System.out.print("  ");
                        rowList1.add(rs1.getString(data1.getColumnLabel(b)));
                    }
                    System.out.println("");
                   dataList1.add(rowList1);
                }
            }

//创建文件
            String fileName = tableName+"08"+".csv";//文件名称
            String filePath = "c:/test/"+projectName+"/"; //文件路径
            File csvFile = null;
            BufferedWriter csvWtriter = null;
            try {
                csvFile = new File(filePath + fileName);
                File parent = csvFile.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                csvFile.createNewFile();

                // GB2312使正确读取分隔符","
                csvWtriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 1024);
                int num = dataListhead.size() / 2;
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < num; i++) {
                    buffer.append(" ,");
                }
               // csvWtriter.write(buffer.toString() + fileName + buffer.toString());
                //csvWtriter.newLine();

                // 写入文件头部
                writeRow(rowListhead, csvWtriter);

                // 写入文件内容
                for (List<Object> row : dataList1) {
                    writeRow(row, csvWtriter);
                }
                csvWtriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    csvWtriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            rs.close();
            //st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeRow(List<Object> row, BufferedWriter csvWriter) throws IOException {
        for (Object data : row) {
            StringBuffer sb = new StringBuffer();
            String rowStr = sb.append("\"").append(data).append("\",").toString();
            csvWriter.write(rowStr);
        }
        csvWriter.newLine();
    }
}
