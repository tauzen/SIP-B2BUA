package io.cohito.b2bua.sip;

import java.io.IOException;
import javax.servlet.sip.SipServlet;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import org.apache.log4j.Logger;


@javax.servlet.sip.annotation.SipServlet(name = "B2BUAServlet", loadOnStartup = 1)
public class B2BUAServlet extends SipServlet {
    
    @Resource 
    private SipFactory sipFactory;
    
    private static Logger log= Logger.getLogger(B2BUAServlet.class.getName());

    @Override
    protected void doInvite(SipServletRequest req) throws ServletException, IOException {
        super.doInvite(req); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected void doProvisionalResponse(SipServletResponse resp) throws ServletException, IOException {
        super.doProvisionalResponse(resp); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected void doSuccessResponse(SipServletResponse resp) throws ServletException, IOException {
        super.doSuccessResponse(resp); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected void doErrorResponse(SipServletResponse resp) throws ServletException, IOException {
        super.doErrorResponse(resp); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected void doAck(SipServletRequest req) throws ServletException, IOException {
        super.doAck(req); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doCancel(SipServletRequest req) throws ServletException, IOException {
        super.doCancel(req); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected void doBye(SipServletRequest req) throws ServletException, IOException {
        super.doBye(req); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doOptions(SipServletRequest req) throws ServletException, IOException {
        req.createResponse(SipServletResponse.SC_OK).send();
    }   
}
