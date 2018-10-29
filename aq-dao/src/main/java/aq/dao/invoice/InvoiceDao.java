package aq.dao.invoice;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoInvoice")
public interface InvoiceDao {

    //查询发票
    List<Map<String,Object>> selectInvoice(Map<String,Object> map);

    //更新发票
    int updateInvoice(Map<String,Object> map);

    //新增发票
    int insertInvoice(Map<String,Object> map);


}
