package io.cohito.b2bua.logic;

import io.cohito.b2bua.utils.SipUtilities;
import javax.servlet.sip.Address;


public class ToHeaderWrapper {
      
    //TODO will be moved to properties or external service
    public static final String INCOMMING_PREFIX = "DE07790";
    public static final String OUTGOING_PREFIX = "DE0770048"; 
    
    private Address addr;
    private String user;
    private String incommingPrefix;
    private String incommingDomain;
    private String outgoingDomain;
    
    public ToHeaderWrapper(Address addr, String outgoingDomain) {
        this.addr = addr;
        this.outgoingDomain = outgoingDomain;
        
        String userPart = SipUtilities.getAddrUserPart(addr);
        
        //TODO should be moved to external service
        this.incommingPrefix = INCOMMING_PREFIX;
        this.user = userPart.replaceFirst(this.incommingPrefix, "");
        
        this.incommingDomain = SipUtilities.getAddrDomainPart(addr);
    }

    public Address getAddr() {
        return addr;
    }

    public String getUser() {
        return user;
    }

    public String getIncommingPrefix() {
        return incommingPrefix;
    }

    public String getIncommingDomain() {
        return incommingDomain;
    }
    
    //TODO additional logic should be added
    public String getOutgoingUser() {
        return OUTGOING_PREFIX + user;
    }
    
    public String getOutgoingDomain() {
        return outgoingDomain;
    }
}
