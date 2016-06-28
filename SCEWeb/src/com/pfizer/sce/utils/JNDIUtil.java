package com.pfizer.sce.utils;

import javax.naming.*;

/**
 * <pre>
 * There are two ways to use this class. If you just want to lookup one item,
 * the quickest way is to use the static method lookupS:
 *  
 * Object val = JNDIUtil.lookupS(name);
 * 
 * This method creates a context, lookups the item and closes the context. 
 * 
 * If on the other hand, you want to lookup several items, it's better to 
 * create an instance of JNDIUtil, perform all the lookups you want and then 
 * call the close method on the object:
 * 
 * JNDIUtil j = new JNDIUtil();
 * Object o = j.lookup(name1);
 * String s = (String)j.lookup(name2);
 * Integer i = (Integer)j.lookup(name3);
 * ..
 * j.close();
 */

public class JNDIUtil {
  
  private Context context = null;

  public static Object lookupS(String name) throws RuntimeException {
    JNDIUtil jndi = new JNDIUtil();
    try {
      return jndi.lookup(name);
    }
    catch (Exception e) {
	    throw new RuntimeException(e.getMessage());
    }
    finally {
	    try {
        jndi.close();
      }
	    catch (Exception e2) {
	    }
    }
  }

  public JNDIUtil() throws RuntimeException {
    try {
	    this.context = new InitialContext();
    }
    catch (Exception e) {
	    throw new RuntimeException(e.getMessage());
    }
  }
	
  public Object lookup(String name)  throws RuntimeException {
    try {

      //Object o = context.lookup("java:comp/env/" + name);
      Object o = context.lookup(name);
      return o;
    }
    catch (Exception e) {
	    throw new RuntimeException(e.getMessage());
    }
  }

  public void close() {
    try {
	    if (context != null) {
        context.close();
	    }
    }
    catch (Exception e) {
    }
  }
}
