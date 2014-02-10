package io.cohito.b2bua.utils;

import java.io.IOException;
import javax.servlet.sip.Address;
import javax.servlet.sip.SipServletMessage;

/**
 *
 * @author krzysiek
 */
public class SipUtilities {
    
    public static void copyContent(SipServletMessage source, SipServletMessage target) throws IOException {
        if(source.getContentLength() > 0) {
            target.setContent(source.getContent(), source.getContentType());
            String encoding = source.getCharacterEncoding();
            if(encoding != null && encoding.length() > 0) {
                target.setCharacterEncoding(encoding);
            }
        }
    }
    
    public static String getAddrUserPart(Address addr) {
        return (addr == null) ? "" : addr.getURI().toString()
                .replaceAll("tel\\:", "")
                .replaceAll("sip\\:", "")
                .replaceAll("@.*", "")
                .replaceAll("\\+", "");                
    }
    
    public static String getAddrDomainPart(Address addr) {
        return (addr == null) ? "" : addr.getURI().toString().replaceAll(".*@", "");
    }
}
