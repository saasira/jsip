/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Unpublished - rights reserved under the Copyright Laws of the United States.
 * Copyright � 2003 Sun Microsystems, Inc. All rights reserved.
 *
 * U.S. Government Rights - Commercial software. Government users are subject 
 * to the Sun Microsystems, Inc. standard license agreement and applicable 
 * provisions of the FAR and its supplements.
 *
 * Use is subject to license terms.
 *
 * This distribution may include materials developed by third parties. Sun, 
 * Sun Microsystems, the Sun logo, Java, Jini and JAIN are trademarks or 
 * registered trademarks of Sun Microsystems, Inc. in the U.S. and other 
 * countries.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *
 * Module Name   : JAIN SIP Specification
 * File Name     : Message.java
 * Author        : Phelim O'Doherty
 *
 *  HISTORY
 *  Version   Date      Author              Comments
 *  1.1     08/10/2002  Phelim O'Doherty    
 *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package javax.sip.message;

import javax.sip.header.*;
import java.util.*;
import java.io.Serializable;
import java.text.ParseException;

/**
* A SIP message is either a request from a client to a server, or a
 * response from a server to a client. Both Request and Response messages
 * use the basic format of <a href ="http://www.ietf.org/rfc/rfc2822.txt">
 * RFC 2822</a>, even though the syntax differs in
 * character set and syntax specifics.  (SIP allows header fields that
 * would not be valid RFC 2822 header fields, for example.)  Both types
 * of messages consist of a method name, address and protocol version,
 * one or more header fields which describe the routing of the message, and 
 * an optional message-body. The message-body contains a session description 
 * in a format such as Session Description Protocol see 
 * <a href ="http://jcp.org/jsr/detail/141.jsp">JSR 141</a>.
 * <p>
 * This interface contains common elements of both Request and Response such as:
 * <ul>
 * <li> Generic accessor functions to headers.
 * <li> Convenience accessor to the expiration value of the message.
 * <li> Convenience header accessor methods for the body content type, language, 
 * disposition and length. 
 * <li> Accessor methods to the body content itself.
 * </ul>
 *
 * @see Request
 * @see Response
 * @see Header
 *
 * @version 1.1
 * @author Sun Microsystems
 *
 */

public interface Message extends Cloneable, Serializable {


// Generic header methods of the Message

    /**
     * Adds the new Header to the existing list of Headers contained in this 
     * Message. The Header is added to the end of the List and will appear in 
     * that order in the SIP Message. 
     * <p> 
     * Required Headers that are singletons should not be added to the message 
     * as they already exist in the message and therefore should be changed using 
     * the {@link Message#setHeader(Header)} method. 
     * <p>
     * This method should be used to support the special case of adding 
     * required ViaHeaders to a message. When adding a ViaHeader using this 
     * method the implementation will add the ViaHeader to the top of the 
     * ViaHeader list, and not the end like all other Headers. 
     *
     * @param header the new Header to be added to the existing Headers List.
     */
    public void addHeader(Header header);
    
    /**
     * Removes the Header of the supplied name from the list of headers in 
     * this Message. If multiple headers exist then they are all removed from 
     * the header list. If no headers exist then this method returns silently.
     * This method should not be used to remove required Headers, required 
     * Headers should be replaced using the {@link Message#setHeader(Header)}. 
     *
     * @param headername the new string value name of the Header to be 
     * removed.
     */
    public void removeHeader(String headerName);         

    /**
     * Gets a ListIterator over all the header names in this Message. Note 
     * that the order of the Header Names in the ListIterator is same as the 
     * order in which they appear in the SIP Message. 
     * 
     * @return the ListIterator over all the Header Names in the Message. 
     */    
    public ListIterator getHeaderNames();     
    
    /**
     * Gets a ListIterator over all the Headers of the newly specified name  
     * in this Message. Note that order of the Headers in ListIterator is the 
     * same as the order in which they appear in the SIP Message. 
     * 
     * @param headerName the new string name of Header types requested.
     * @return the ListIterator over all the Headers of the specified name in 
     * the Message, this method returns an empty ListIterator if no Headers 
     * exist of this header type.
     */    
    public ListIterator getHeaders(String headerName);  
    
    /**
     * Gets the Header of the specified name in this Message. If multiple 
     * Headers of this header name exist in the message, the first header
     * in the message is returned.
     * 
     * @param headerName the new string name of Header type requested.
     * @return the Header of the specified name in the Message, this method 
     * returns null if the Header does not exist.
     */      
    public Header getHeader(String headerName);

    /**
     * Gets a ListIterator over all the UnrecognizedHeaders in this 
     * Message. Note the order of the UnrecognizedHeaders in the ListIterator is 
     * the same as order in which they appeared in the SIP Message.  
     * UnrecognizedHeaders are headers that the underlying implementation does 
     * not recognize, if a header is recognized but is badly formatted it will 
     * be dropped by the underlying implementation and will not be included in 
     * this list. A Proxy should not delete UnrecognizedHeaders and should 
     * add these Headers to the end of the header list of the Message that is
     * being forwarded. A User Agent may display these unrecognized headers to
     * the user.
     * 
     * @return the ListIterator over all the UnrecognizedHeaders in the Message 
     * represented as Strings, this method returns an empty ListIterator if no 
     * UnrecognizedHeaders exist.
     */    
    public ListIterator getUnrecognizedHeaders();      
    
    /**
     * Sets the new Header to replace existings Header of that type in
     * the message. If the SIP message contains more than one Header of 
     * the new Header type it should replace the first occurance of this 
     * Header and removes all other Headers of this type. If no Header of this 
     * type exists this header is added to the end of the SIP Message.
     * This method should be used to change required Headers and overwrite 
     * optional Headers.
     *
     * @param header the new Header to replace any existing Headers of that 
     * type.
     */        
    public void setHeader(Header header);    

// Content manipulation methods of the Message
    
    /**
     * Set the ContentLengthHeader of this Message. 
     * The actual content length for the outgoing message will be computed from 
     * the content assigned. If the content is speficied as an object it will 
     * be converted to a String before the message is sent out and the content 
     * length computed from the length of the string. If the message content is 
     * specified in bytes, the length of the byte array specified will be used 
     * to determine the content length header, that is in both cases, the length 
     * of the content overrides any value specified in the content-length 
     * header.
     *
     * @param contentLength the new ContentLengthHeader object containing the 
     * content length value of this Message.
     * 
     */
    public void setContentLength(ContentLengthHeader contentLength);

    /**
     * Gets the ContentLengthHeader of the body content of this Message. This is 
     * the same as <code>this.getHeader(Content-Length);</code>
     *
     * @return the ContentLengthHeader of the message body.
     */
    public ContentLengthHeader getContentLength();

    /**
     * Sets the ContentLanguageHeader of this Message. This overrides the 
     * ContentLanguageHeader set using the setHeaders method. If no 
     * ContentLanguageHeader exists in this message this ContentLanguageHeader 
     * is added to the end of the Header List.
     *
     * @param contentLanguage the new ContentLanguageHeader object containing the 
     * content language value of this Message.
     */
    public void setContentLanguage(ContentLanguageHeader contentLanguage);

    /**
     * Gets the ContentLanguageHeader of this Message. This is the same as
     * <code>this.getHeader(Content-Langauge);</code>
     *
     * @return the ContentLanguageHeader of the message body.
     */
    public ContentLanguageHeader getContentLanguage();
    
    /**
     * Sets the ContentEncodingHeader of this Message. This overrides the 
     * ContentEncodingHeader set using the setHeaders method. If no 
     * ContentEncodingHeader exists in this message this ContentEncodingHeader 
     * is added to the end of the Header List.
     *
     * @param contentEncoding the new ContentEncodingHeader object containing the 
     * content encoding values of this Message.
     */
    public void setContentEncoding(ContentEncodingHeader contentEncoding);
    
    /**
     * Gets the ContentEncodingHeader of this Message. This is the same as
     * <code>this.getHeader(Content-Encoding);</code>
     *
     * @return the ContentEncodingHeader of the message body.
     */
    public ContentEncodingHeader getContentEncoding();

    /**
     * Sets the ContentDispositionHeader of this Message. This overrides the 
     * ContentDispositionHeader set using the setHeaders method. If no 
     * ContentDispositionHeader exists in this message this ContentDispositionHeader 
     * is added to the end of the Header List.
     *
     * @param contentDisposition the new ContentDispositionHeader object 
     * containing the content disposition value of this Message.
     */
    public void setContentDisposition(ContentDispositionHeader contentDisposition);

    /**
     * Gets the ContentDispositionHeader of this Message. This is the same as
     * <code>this.getHeader(Content-Disposition);</code>
     *
     * @return the ContentDispositionHeader of the message body.
     */
    public ContentDispositionHeader getContentDisposition();    
    

    /**
     * Sets the body of this Message, with the ContentType defined by the new
     * ContentTypeHeader object and the string value of the content.
     *
    * @param content the new Object value of the content of the Message.
     * @param contentTypeHeader the new ContentTypeHeader object that defines
     * the content type value.
     * @throws ParseException which signals that an error has been reached
     * unexpectedly while parsing the body.
     */
    public void setContent(Object content, ContentTypeHeader contentTypeHeader)
                                             throws ParseException;

    /**
     * Gets the body content of the Message as a byte array.
     * 
     * @return the body content of the Message as a byte array, this method 
     * returns null if a body does not exist.
     */
    public byte[] getRawContent();

    /**
     * Gets the body content of the Message as an Object.
     * 
     * @return the body content of the Message as an Object, this method 
     * returns null if a body does not exist.
     */
    public Object getContent();
       
    /**
     * Removes the body content from this Message and all associated entity 
     * headers, if a body exists, this method returns sliently if no body exists.
     */
    public void removeContent();    

    
// Additional Utility methods
    
    /**
     * Sets the ExpiresHeader of this Message. This overrides the ExpiresHeader 
     * set using the setHeaders method. If no ExpiresHeader exists in this 
     * message this ExpiresHeader is added to the end of the Header List.
     *
     * @param expires the new ExpiresHeader object containing the expires 
     * values of this Message.
     */
    public void setExpires(ExpiresHeader expires);
    
    /**
     * Gets the ExpiresHeader of this Message. This is the same as
     * <code>this.getHeader(Expires);</code>
     *
     * @return the ExpiresHeader of the message body.
     */
    public ExpiresHeader getExpires();

    /**
     * Sets the protocol version of SIP being used by this Message.
     *
     * @param version the new String object containing the version of the SIP 
     * Protocol of this Message.
     * @throws ParseException which signals that an error has been reached
     * unexpectedly while parsing the version argument.
     */
    public void setSIPVersion(String version) throws ParseException;
    
    /**
     * Gets the protocol version of SIP being used by this Message. 
     *
     * @return the protocol version of the SIP protocol of this message.
     */
    public String getSIPVersion();    
    
 
// Java Utility methods for Message
    
    /**
     * Creates and returns a deep copy of the Message. This methods must ensure a
     * deep copy of the message, so that it can be modified without effecting
     * the original message. This provides useful functionality for proxying
     * Requests and Responses, for example:
     * <ul>
     * <li>Recieve a message.
     * <li>Create a deep clone of the message.
     * <li>Modify necessary headers.
     * <li>Proxy the message using the send methods on the SipProvider.
     * </ul>
     *
     * @return a deep copy of Message
     */
    public Object clone();
    
    /**
     * Gets string representation of Message
     * @return string representation of Message
     */
    public String toString();
    /**
     * Compare this SIP Message for equality with another.
     *
     * @param obj the object to compare this Message with.
     * @return <code>true</code> if <code>obj</code> is an instance of this class
     * representing the same SIP Message as this, <code>false</code> otherwise.
     */
    public boolean equals(Object object);
     
}
