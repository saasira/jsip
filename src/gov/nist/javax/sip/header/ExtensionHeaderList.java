/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
*******************************************************************************/
package gov.nist.javax.sip.header;
import  gov.nist.javax.sip.header.*;
import java.util.ListIterator;
import javax.sip.header.*;

/**
* A generic extension header list.
*/

public class ExtensionHeaderList extends SIPHeaderList { 

	public ExtensionHeaderList(String hName) {
		super( ExtensionHeaderImpl.class, hName);
	}
	public ExtensionHeaderList() {
		this(null);
	}
	
	public String encode() {
		StringBuffer retval = new StringBuffer();
		ListIterator it = this.listIterator();
		while(it.hasNext()) {
		   ExtensionHeaderImpl eh = (ExtensionHeaderImpl) it.next();
		   retval.append(eh.encode());
		}
		return retval.toString();
	}
		
        
}
