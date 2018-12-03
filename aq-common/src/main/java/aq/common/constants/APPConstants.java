package aq.common.constants;

/**
 * Created by 杨大山 on 2017-02-27.
 */
public final class APPConstants {

    public static final String HTTP = "http://";

    //服务器地址
    public static final String SPBILL_CREATE_IP = "47.106.177.213";

    //文件服务器地址
    public static final String FILE_SERVICE_URL = HTTP + SPBILL_CREATE_IP;
//    public static final String FILE_SERVICE_URL = "D:\\Work\\resource\\contract\\";

    //附件资源位置
    public static final String FILE_SERVICE_PLACE = "../../resouce/contract/xh";
//    public static final String FILE_SERVICE_PLACE = "D:\\Work\\resource\\contract";


    //支付回调
    public static final String WX_NOTIFY = FILE_SERVICE_URL + ":8083/aq/api/wx/notify";

    //数据库备份
    public static final String SQL_PATH = "../../sql/";


}
