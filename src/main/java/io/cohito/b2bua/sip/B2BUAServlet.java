package io.cohito.b2bua.sip;

import io.cohito.b2bua.logic.ServiceLogic;
import io.cohito.b2bua.utils.SipUtilities;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.sip.B2buaHelper;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletMessage;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.UAMode;
import org.apache.log4j.Logger;


@javax.servlet.sip.annotation.SipServlet(name = "B2BUAServlet", loadOnStartup = 1)
public class B2BUAServlet extends SipServlet {
    
    @Resource 
    private SipFactory sipFactory;
    
    private static Logger log= Logger.getLogger(B2BUAServlet.class.getName());

    @Override
    protected void doRequest(SipServletRequest req) throws ServletException, IOException {
        log.info("appsessId: " + req.getApplicationSession().getId() + "|"
                + "callId: " + req.getCallId() + "|" + req.getMethod() + "|"
                + "from: " + req.getFrom().getURI() + "|"
                + "to: " + req.getTo().getURI());
        
        try {
            super.doRequest(req);
        } catch(Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    protected void doResponse(SipServletResponse resp) throws ServletException, IOException {
        log.info("appsessId: " + resp.getApplicationSession().getId() + "|"
                + "callId: " + resp.getCallId() + "|"
                + resp.getStatus() + " " + resp.getReasonPhrase() + "|"+ resp.getMethod() + "|"
                + "from: " + resp.getFrom().getURI() + "|"
                + "to: " + resp.getTo().getURI());
        try {
            super.doResponse(resp);
        } catch(Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
        }
    }
  
    @Override
    protected void doInvite(SipServletRequest req) throws ServletException, IOException {
        B2buaHelper b2bua = req.getB2buaHelper();
        if(req.isInitial()) {
            Map<String, List<String>> headerMap = ServiceLogic.createSecondLegsInviteHeaders(sipFactory, req);            
            SipServletRequest invite = b2bua.createRequest(req, true, headerMap);
            SipUtilities.copyContent(req, invite);
            ServiceLogic.modifySecondLegsInviteRequest(req);
            
            logAction(invite, "sending Invite to second leg");
            invite.send();
        } else {
            SipSession linkedSession = b2bua.getLinkedSession(req.getSession());
            SipServletRequest reInvite = b2bua.createRequest(linkedSession, req, null);
            SipUtilities.copyContent(req, reInvite);
            
            logAction(reInvite, "sending ReInvite to second leg");
            reInvite.send();
        }
    }
    
    @Override
    protected void doProvisionalResponse(SipServletResponse resp) throws ServletException, IOException {
        SipServletResponse firstLegResp = SipUtilities.prepareResponseForFirstLeg(resp);
        SipUtilities.copyContent(resp, firstLegResp);
        
        logAction(firstLegResp, "Passing provisional response message");
        firstLegResp.send();
    }

    @Override
    protected void doErrorResponse(SipServletResponse resp) throws ServletException, IOException {
        if(resp.getStatus() == SipServletResponse.SC_REQUEST_TERMINATED) {
            logAction(resp, "Got 487 for canceled request, finishing");
            return;
        }
        
        SipServletResponse firstLegResp = SipUtilities.prepareResponseForFirstLeg(resp);        
        
        logAction(firstLegResp, "Passing error response message");
        firstLegResp.send();
    }
       
    @Override
    protected void doSuccessResponse(SipServletResponse resp) throws ServletException, IOException {
        if(resp.getMethod().equals("BYE")) return;
        
        SipServletResponse firstLegResp = SipUtilities.prepareResponseForFirstLeg(resp);
        SipUtilities.copyContent(resp, firstLegResp);
        
        logAction(firstLegResp, "Passing success message.");
        firstLegResp.send();
    }
    
    @Override
    protected void doAck(SipServletRequest req) throws ServletException, IOException {
        B2buaHelper b2bua = req.getB2buaHelper();
        SipSession linked = b2bua.getLinkedSession(req.getSession());
        
        List<SipServletMessage> pendingMsgs = b2bua.getPendingMessages(linked, UAMode.UAC);
        for(SipServletMessage msg : pendingMsgs) {
            if(msg instanceof SipServletResponse) {
                SipServletResponse resp = (SipServletResponse)msg;
                if(resp.getStatus() == SipServletResponse.SC_OK) {
                    SipServletRequest ack = resp.createAck();
                    SipUtilities.copyContent(req, ack);
                    
                    logAction(ack, "Call established, sending ack");
                    ack.send();
                }
            }
        }     
    }

    @Override
    protected void doCancel(SipServletRequest req) throws ServletException, IOException {
        B2buaHelper b2bua = req.getB2buaHelper();
        SipSession linkedSession = b2bua.getLinkedSession(req.getSession());
        SipServletRequest cancel =  b2bua.createCancel(linkedSession);
        
        logAction(cancel, "Canceling request");
        cancel.send();
        
    }
    
    @Override
    protected void doBye(SipServletRequest req) throws ServletException, IOException {

    }

    @Override
    protected void doOptions(SipServletRequest req) throws ServletException, IOException {
        req.createResponse(SipServletResponse.SC_OK).send();
    }   
    
    protected void logAction(SipServletMessage sipMsg, String logMsg) {
        log.info("appsessId: " + sipMsg.getApplicationSession().getId() + "|"
                + "callId: " + sipMsg.getCallId() + "|"
                + logMsg);
    }
}
