package org.jvoicexml.jsapi2.sapi.recognition;

public class JNITest {
	
	public native int number( );

	
//  public static void main(String[] args) 
//  { 
//      JNITest jniTest = new JNITest();
//	  int test = jniTest.number();     
//      System.out.println("C-code number: " + test ); 
//  }
  
  
  JNITest(){	  
	  String dir = System.getProperty("user.dir");
	  System.out.println(dir);	
	  System.load(dir+"\\bin\\JSapi2Sapi.dll");	      
  }
  
}
