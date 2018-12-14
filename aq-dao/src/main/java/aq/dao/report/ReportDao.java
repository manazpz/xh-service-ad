package aq.dao.report;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("report")
public interface ReportDao {

    //客户报表
    List<Map<String,Object>> selectOrderReport(Map<String,Object> map);
}
