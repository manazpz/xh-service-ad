package aq.common.access;


import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

/**
 * 上下文对象 建议放到各自项目的Web层（如果每个Web层处理逻辑有差异的话）
 * Created by 杨大山 on 2017-02-17.
 */
public class Context extends ContextBase{

    //Security Key
    private static final String SECURITY_KEY ="D88F4F270F438B3D2F1D0D2^2B908_11";

    private static Context instance;

    public static synchronized  IContext current() {
       if (instance == null){
           instance = new Context();
       }
       return instance;
    }

    @Override
    public Ticket ticket() {
            boolean hasToken = false;
            String token = null;
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            Ticket ticket = new Ticket(request.getAttribute("token").toString(), request.getAttribute("userId").toString(), request.getAttribute("sessionId").toString());
            return ticket;
    }

}
