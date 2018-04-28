package com.fy.util;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author:飞羽
 * @date:2018/3/13 15:44
 * @Description:
 */
public class FyUtil {

    /**
     * 验证非零的正整数
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串,返回 true ,否则为 false
     */
    public static boolean IsIntNumber(String str) {
        String regex = "^\\+?[1-9][0-9]*$";
        return match(regex, str);
    }

    /**
     * 验证数字输入
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串,返回 true ,否则为 false
     */
    public static boolean IsNumber(String str) {
        String regex = "^[0-9]*$";
        String regex1 = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
        if (match(regex1, str) || match(regex, str)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param regex
     *            正则表达式字符串
     * @param str
     *            要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 将查询条件加入到sql语句中
     *
     * @param ac
     *            ActionContext
     * @param sql
     *            SQL语句
     * @param conditionName
     *            条件的名称（需要和页面上以及数据库名称一致）
     * @param modelType
     *            判断模式,目前仅支持"="及"like"模式，"like"模式即为like %condition%
     * @param list
     *            保存查询参数的list
     */
    public static void appendQueryConditionToSql(Controller ac, StringBuilder sql, String conditionName, String modelType, List<Object> list) {
        if (null == ac || null == sql || StringIsNullOrEmpty(conditionName) || StringIsNullOrEmpty(modelType)) {
            return;
        }
        String model1 = null;
        String model2 = null;
        if ("=".equals(modelType)) {
            model1 = "=";
            model2 = "";
        } else if ("like".equals(modelType)) {
            model1 = " like ";
            model2 = "%";
        }

        if(conditionName.indexOf(".")!=-1){//处理a.xxx类型的字段
            String[] arr=conditionName.split("\\.");
            String condition =ac.getRequest().getParameter(arr[1]);
            ac.setAttr(arr[1], condition);
            if (!StringIsNullOrEmpty(condition)) {
                sql.append(" and ").append(conditionName).append(model1).append("?");
                list.add(model2 + condition + model2);
            }
        }else{
            String condition =ac.getRequest().getParameter(conditionName);
            ac.setAttr(conditionName, condition);
            if (!StringIsNullOrEmpty(condition)) {
                sql.append(" and ").append(conditionName).append(model1).append("?");
                list.add(model2 + condition + model2);
            }
        }

    }

    /**
     * 将查询条件加入到sql语句中
     *
     * @param ac
     *            ActionContext
     * @param sql
     *            SQL语句
     * @param conditionName
     *            条件的名称（需要和页面上以及数据库名称一致）
     * @param modelType
     *            判断模式,目前仅支持"="及"like"模式，"like"模式即为like %condition%
     * @param list
     *            保存查询参数的list
     */
    public static void appendQueryConditionToSql(Controller ac, StringBuffer sql, String conditionName, String modelType, List<Object> list) {
        if (null == ac || null == sql || StringIsNullOrEmpty(conditionName) || StringIsNullOrEmpty(modelType)) {
            return;
        }
        String model1 = null;
        String model2 = null;
        if ("=".equals(modelType)) {
            model1 = "=";
            model2 = "";
        } else if ("like".equals(modelType)) {
            model1 = " like ";
            model2 = "%";
        }

        if(conditionName.indexOf(".")!=-1){
            String[] arr=conditionName.split("\\.");
            String condition =ac.getRequest().getParameter(arr[1]);
            ac.setAttr(arr[1], condition);
            if (!StringIsNullOrEmpty(condition)) {
                sql.append(" and ").append(conditionName).append(model1).append("?");
                list.add(model2 + condition + model2);
            }
        }else{
            String condition =ac.getRequest().getParameter(conditionName);
            ac.setAttr(conditionName, condition);
            if (!StringIsNullOrEmpty(condition)) {
                sql.append(" and ").append(conditionName).append(model1).append("?");
                list.add(model2 + condition + model2);
            }
        }
    }

    /**
     * 用于将null字符串转换为“”，避免空指针异常
     *
     * @param Str 要处理的字符串
     * @return 经过处理的字符串
     */
    public static String emptyCase(String Str) {
        return Str+"";
    }
    /**
     * 用于将null转换为“”，避免空指针异常(Object版本)
     *
     * @param Str 要处理的字符串
     * @return 经过处理的字符串
     */
    public static String emptyCase(Object Str) {
        if(null==Str){
            return "";
        }else{
            return Str+"";
        }
    }

    /**
     * 字符串是否为空的判断
     *
     * @param str 要判断的字符串
     * @return
     */
    public static boolean StringIsNullOrEmpty(Object str) {
        String s=emptyCase(str);
        if (s == null || s.equals("") || s.equalsIgnoreCase("null")) {
            return true;
        }
        return false;
    }

    /**
     * 计算endDate-beginDate等于多少天
     *
     * @param beginDate
     * @param endDate
     * @return endDate-beginDate等于多少天
     */
    public static int DaysCalculation(Date beginDate, Date endDate) {
        if (null == beginDate || null == endDate) {
            return 0;
        }
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(beginDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(endDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1;

    }

    /**
     * 判断str是否包含中文
     *
     * @param str
     * @return 包含返回true，不包含返回false
     */
    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    /**
     *
     * @param dateFormat
     *            日期格式
     * @param str
     *            要验证的日期
     * @return 验证通过返回true,否则返回false
     */
    public static boolean isValidDate(String dateFormat, String str) {
        boolean convertSuccess = true;
        // 指定日期格式
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007-02-29会被接受，并转换成2007-03-01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }


    /**
     * 将字符串中的中文括号转换为英文括号
     * @param s 字符串
     * @return 进过转换后的字符串
     */
    public static String englishBracketsCase(String s) {
        if (StringIsNullOrEmpty(s)) {
            return "";
        }
        s = s.replace("（", "(");
        s = s.replace("）", ")");
        s = s.replace(" ", "");
        return s;
    }

    /**
     * 将时间对象按照指定格式转化为字符串
     * @param date 时间对象
     * @param format 时间格式
     * @return 格式化之后的时间字符串，如果对象为空则返回空字符串
     */
    public static String simpleDateFormat(Object date,String format){
        if(null==date){
            return "";
        }
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 将时间字符串按照格式装换
     * @param date 时间字符串
     * @param a 需要被替换的字符
     * @param b 需要替换的字符
     * @return 替换之后的字符串，如果对象为空则返回空字符串
     */
    public static String simpleDateFormat(String date,String a,String b){
        if(date==null||a==null||b==null){
            return "";
        }
        return date.replaceAll(a, b);
    }


    public static void main(String[] args) {
        String s="eww.wew";
        String[] s2=s.split(".");
        System.out.println(s2.length);
    }

    /**
     * 获取excel单元格的文本内容（支持获取数字格式\文本格式\日期格式YYYY-MM-DD）
     * @param cell 要获取内容的单元格
     * @return 返回单元格的内容，如果单元格无内容或不支持的格式，返回空字符串
     */
    public static String getCellValue(Cell cell){
        if(null==cell){
            return "";
        }
        int cellType=cell.getCellType();
        String cellValue=null;
        switch(cellType){
            case HSSFCell.CELL_TYPE_STRING:cellValue=cell.getStringCellValue();break;
            case HSSFCell.CELL_TYPE_NUMERIC:
            {
                if(HSSFDateUtil.isCellDateFormatted(cell)){
                    Date date=cell.getDateCellValue();
                    cellValue=new SimpleDateFormat("yyyy-MM-dd").format(date);
                }else{
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cellValue=cell.getStringCellValue();
                }
                break;
            }
            default:cellValue="";break;
        }
        return cellValue;
    }

    /**
     * 自适应查询，适合单表查询使用，要求页面上的标签name与数据库字段名一致,必须大写（有自定义函数版）
     * @param tablename 表名
     * @param pageNum 分页参数
     * @param pageSize 分页参数
     * @param controller 调用这个方法的controller
     * @param methodName 自定义查询接管函数，用于添加自定义查询，格式：public void custom(Stringbuilder sql,List param)
     * @return
     */
    public static Page<Record> selfAdaptionQuery(String tablename,Integer pageNum,Integer pageSize,Controller controller,String methodName,String orderby){
        if(null==tablename){
            return null;
        }
        if(null==pageNum||pageNum==0){
            pageNum=1;
        }
        if(null==pageSize||pageSize==0){
            pageSize=50;
        }
        List<Record> colList = new ArrayList<Record>();
        colList=Db.find("select table_name,column_name,data_type,data_length from user_tab_columns where table_name=? ", tablename.toUpperCase());
        Iterator<Record> i=colList.iterator();
        Record record=null;
        List<Object> para = new ArrayList<Object>();
        String sql1="select *  ";
        StringBuilder sql=new StringBuilder(" from "+tablename+" where 1=1 ");
        while(i.hasNext()){
            record=i.next();
            String s=controller.getPara(record.getStr("column_name"));
            if(!StringIsNullOrEmpty(s)){
                sql.append(" and ").append(record.get("column_name"));
                boolean flag=(Integer.parseInt(record.get("data_length").toString())<50);
                sql.append(flag?" = ? ":" like ? ");
                para.add(flag?s:"%"+s+"%");
                controller.setAttr(record.getStr("column_name"), s);
            }
        }
        if(!StringIsNullOrEmpty(methodName)){
            try {
                Method method=controller.getClass().getMethod(methodName,sql.getClass(),List.class);
                method.invoke(controller, sql,para);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sql.append(" "+orderby);
        Page<Record> page=null;
        if(null==para||para.size()<1){
            page=Db.paginate(pageNum, pageSize, sql1, sql.toString());
        }else{
            page=Db.paginate(pageNum, pageSize, sql1, sql.toString(),para.toArray());
        }
        return page;
    }

    /**
     * 自适应查询，适合单表查询使用，要求页面上的标签name与数据库字段名一致（无自定义函数版）
     * @param tablename 表名
     * @param pageNum 分页参数
     * @param pageSize 分页参数
     * @param controller 调用这个方法的controller
     * @return
     */
    public static Page<Record> selfAdaptionQuery(String tablename,Integer pageNum,Integer pageSize,Controller controller ){
        if(null==tablename){
            return null;
        }
        if(null==pageNum||pageNum==0){
            pageNum=1;
        }
        if(null==pageSize||pageSize==0){
            pageSize=50;
        }
        List<Record> colList = new ArrayList<Record>();
        colList=Db.find("select table_name,column_name,data_type,data_length from user_tab_columns where table_name=? ", tablename.toUpperCase());
        Iterator<Record> i=colList.iterator();
        Record record=null;
        List<Object> para = new ArrayList<Object>();
        String sql1="select *  ";
        StringBuilder sql=new StringBuilder(" from "+tablename+" where 1=1 ");
        while(i.hasNext()){
            record=i.next();
            String s=controller.getPara(record.getStr("column_name"));
            if(!StringIsNullOrEmpty(s)){
                sql.append(" and ").append(record.get("column_name"));
                boolean flag=(Integer.parseInt(record.get("data_length").toString())<50);
                sql.append(flag?" = ? ":" like ? ");
                para.add(flag?s:"%"+s+"%");
            }
        }
        Page<Record> page=null;
        if(null==para||para.size()<1){
            page=Db.paginate(pageNum, pageSize, sql1, sql.toString());
        }else{
            page=Db.paginate(pageNum, pageSize, sql1, sql.toString(),para.toArray());
        }

        return page;
    }

    /**
     * 对比两个record，不同的标记为red
     * @param record1
     * @param record2
     * @return 比较结果，也是一个record，字段同比较对象，值不同的返回red
     */
    public static Record compareRecord(Record record1, Record record2) {
        String[] columnArr=record1.getColumnNames();
        Record result=new Record();
        for(String s:columnArr){
            if(emptyCase(record1.getStr(s)).equals(emptyCase(record2.getStr(s)))){
                result.set(s, "black");
            }else{
                result.set(s,"red");
            }
        }
        return result;
    }

    /**
     * @param sql 样例：select count(1) from table where id=? and rownum=1
     * @return 是否存在
     */
    public static boolean isExists(String sql,Object... parm){
        BigDecimal result=Db.queryBigDecimal(sql,parm);
        if("0".equals(result.toString())){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 生成多位随机数字
     * @param bit 要生成几位
     * @return 生成的数字
     */
    public static String randomNum(int bit){
        if(bit<1){
            return "";
        }
        StringBuilder s=new StringBuilder();
        Random r=new Random();
        for(int i=0;i<bit;i++){
            s.append(r.nextInt(10));
        }
        return s.toString();
    }

    /**
     * list<record> to excel
     * @param tableHead 表头(可自定义表头上面的格式，方法会跳过自定义部分的表头)
     * @param columnData 和表头对应的字段名
     * @param workbook excel文档
     * @param list 数据list（注意所有字段必须是String型）
     */
    public static void listToExcel(String[] tableHead,String[] columnData,Workbook workbook,List<Record> list){
        Sheet sheet=null;
        try {
            sheet=workbook.getSheetAt(0);
        } catch (Exception e) {
            sheet=workbook.createSheet();
        }

        int tableHeadNum=0;
        if(null==sheet){
            sheet=workbook.createSheet();
            tableHeadNum=0;
        }else{
            tableHeadNum=sheet.getLastRowNum()+1;
        }

        Row tableHeadRow=sheet.createRow(tableHeadNum);
        for(int i=0;i<tableHead.length;i++){//填写表头
            tableHeadRow.createCell(i).setCellValue(tableHead[i]);
        }
        for(int i=0;i<list.size();i++){//填写数据
            Row dataRow=sheet.createRow(i+1+tableHeadNum);
            Record record=list.get(i);
            for(int j=0;j<columnData.length;j++){
                dataRow.createCell(j).setCellValue(emptyCase(record.get(columnData[j])));
            }
        }
    }

    /**
     * 将工作表转换为文件夹
     * @param workbook 要转换为文件的工作表
     * @param fileName 转换成文件的文件名（记得加后缀）
     * @return 转换好的文件
     */
    public static File workBookToFile(Workbook workbook, String fileName){
        if(null==workbook){
            return null;
        }
        File file=new File(fileName);
        FileOutputStream fout;
        try {
            fout = new FileOutputStream(file);
            workbook.write(fout);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file;
    }

    /**
     * jfinal无法直接使用java.util.Date，采用此方法将Date转换为jfinal支持的类型
     * @param date
     * @return 转换后的date
     */
    public static Timestamp jfinalDate(Date date){
        return new Timestamp(date.getTime());
    }

    /**
     * 带分页的record查询
     * @param pageCurrent 当前页是哪一页
     * @param pageSize 每一页显示几条
     * @param sql 查询sql
     * @param list 参数list
     * @return 分页对象
     */
    public static Page<Record> queryPage(int pageCurrent, int pageSize, String sql, List<Object> list) {
        List<Record> recordList = Db.find(sql,list.toArray());
        Page<Record> page=new Page<Record>(recordList, pageCurrent, pageSize, recordList.size()/pageSize, recordList.size());
        return page;
    }

    /**
     * 文件上传的表单提交，使用request.getParameter()会获取不到表单元素，可以使用这个方法来获取
     * 表单元素的值
     * @param request httpServletRequest
     * @param name 表单元素的名称
     * @return 表单元素的值
     */
    @Deprecated
    public static String fileRequestGetParameter(HttpServletRequest request, String name) {
        if(StringIsNullOrEmpty(name)){
            return "";
        }
        ServletFileUpload servletFileUpload=new ServletFileUpload();
        Map<String, String> map = new HashMap<String, String>();
        try {
            FileItemIterator i=servletFileUpload.getItemIterator(request);
            while(i.hasNext()){
                FileItemStream fis=i.next();
                InputStream is=fis.openStream();
                if(fis.isFormField()&&fis.getFieldName().equals(name)){
                    Scanner scan=new Scanner(is);
                    StringBuilder s=new StringBuilder();
                    while(scan.hasNextLine()){
                        s.append(scan.nextLine());
                    }
                    is.close();
                    return s.toString();
                }
                is.close();
            }
        } catch (FileUploadException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 循环遍历输出map
     * 如果是自定义对象，需要实现toString()方法，否则只能输出对象地址
     * @param map
     */
    public static <T,T1> void print(Map<T, T1> map)
    {
        Set<T> set=map.keySet();
        for(T s:set){
            T1 t2=map.get(s);
            System.out.println(s+" "+t2);
        }
    }

    /**
     * 循环遍历输出set
     * 如果是自定义对象，需要实现toString()方法，否则只能输出对象地址
     * @param set
     */
    public static <T> void print(Set<T> set) {
        for(T s:set){
            System.out.println(s);
        }
    }

    /**
     * 循环遍历输出list
     * 如果是自定义对象，需要实现toString()方法，否则只能输出对象地址
     * @param list
     */
    public static <T> void print(List<T> list) {
        for(int i=0;i<list.size();i++){
            System.out.println(list.get(i));
        }
    }


    /**
     * 获取这个excel工作表一共有多少行(仅适用于真实存在的文档，内存中新建出来的文档不适用)
     * @param sheet 工作表
     * @return 这个工作表的行数
     */
    public static int getRowNum(Sheet sheet) {
        int num = sheet.getLastRowNum();
        Row row = null;
        String cerno = null;
        for (int i = num; i > 0; i--) {
            row = sheet.getRow(i);
            if (row != null) {
                cerno = getCellValue(row.getCell(0));// 使用第一格
                if (!FyUtil.StringIsNullOrEmpty(cerno)) {// 如果这个行不为空且第一格也不为空，那就认为这个行是有东西的
                    num = i;
                    break;
                }
            }
        }
        return num;
    }

    /**
     * list<record>横向合计
     * @param list 数据列表
     * @param columnData 要合计的字段
     */
    public static void xcount(List<Record> list,String ...columnData){
        try {
            int total=0;
            for(Record r:list){
                for(int i=0;i<columnData.length;i++){
                    total+=Integer.parseInt(emptyCase(r.get(columnData[i])));
                }
                r.set("合计", total);
                total=0;
            }
        } catch (Exception e) {
            double total=0.0;
            for(Record r:list){
                for(int i=0;i<columnData.length;i++){
                    total+=Double.parseDouble(emptyCase(r.get(columnData[i])));
                }
                r.set("合计", total);
                total=0;
            }
        }
    }

    /**
     * list<record>纵向合计
     * @param list 数据列表
     * @param headColumn 合计两个字要写在哪里
     * @param columnData 要合计的字段
     */
    public static void ycount(List<Record> list,String headColumn,String ...columnData){

        try {
            int total=0;
            Record r =new Record();
            for(int i=0;i<columnData.length;i++){
                for(int j=0;j<list.size();j++){
                    total+=Integer.parseInt(emptyCase(list.get(j).get(columnData[i])));
                }
                r.set(columnData[i], total);
                total=0;
            }
            r.set(headColumn, "合计");
            list.add(r);
        } catch (Exception e) {
            double total=0;
            Record r =new Record();
            for(int i=0;i<columnData.length;i++){
                for(int j=0;j<list.size();j++){
                    total+=Double.parseDouble(emptyCase(list.get(j).get(columnData[i])));
                }
                r.set(columnData[i], total);
                total=0;
            }
            list.add(r);
        }
    }

    /**
     * list<record>横纵向合计
     * @param list 数据列表
     * @param headColumn 纵向的合计两个字要写在哪里
     * @param columnData 要合计的字段
     */
    public static void xycount(List<Record> list,String headColumn,String ...columnData){
        ycount(list,headColumn, columnData);
        xcount(list, columnData);
    }


    /**
     * 替换jfinal的record.getInt()
     * @param r
     * @param columnName 字段名
     * @return 一个int
     */
    public static int jfinalGetInt(Record r,String columnName){
        return Integer.parseInt(emptyCase(r.get(columnName)));
    }

    /**
     * 替换jfinal的record.getDouble()
     * @param r 记录
     * @param columnName 字段名
     * @return 一个Double
     */
    public static double jfinalGetDouble(Record r, String columnName){
        return Double.parseDouble(emptyCase(r.get(columnName)));
    }

    /**
     * 计算a/b的百分比，保留两位小数
     * @param a
     * @param b
     * @return
     */
    public static Double calPercent(Double a,Double b){
        DecimalFormat df=new DecimalFormat("#.00");
        Double result=a/b*100;
        return Double.parseDouble(df.format(result));
    }

    /**
     * 获取年份
     * @return 年份列表
     */
    public static List<String> getYears(){
        Calendar a = Calendar.getInstance();
        int y = a.get(Calendar.YEAR);
        List<String> years = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            years.add(String.valueOf(y));
            y--;
        }
        return years;
    }

    

}
