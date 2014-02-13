package io.cohito.b2bua.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.URI;

public class ServiceLogic {
    
    public static  Map<String, List<String>> createSecondLegsInviteHeaders(SipFactory sipFactory, SipServletRequest req) {
        Map<String, List<String>> headerMap = new HashMap<String, List<String>>();
        List<String> toList = new ArrayList<String>();
        ToHeaderWrapper to = new ToHeaderWrapper(req.getTo());
        
        URI toURI = sipFactory.createSipURI(to.getOutgoingUser(), to.getOutgoingDomain());
        toList.add(toURI.toString());
        headerMap.put("To", toList);
        
        return headerMap;
    }
    
    public static void modifySecondLegsInviteRequest(SipServletRequest req) {
        //remove headers, change values, etc
        return;
    }
}
