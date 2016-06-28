package  com.pfizer.sce.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebExTemplateServlet  extends HttpServlet
{ 
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	ServletContext context=getServletContext();
        File file=new File(context.getRealPath("evaluation/resources/xls/webex.xls"));
        InputStream fis=new FileInputStream(file);//;
        if(fis!=null){
        //this.getClass().getClassLoader().getResourceAsStream("testuploadTemplate.xls");
        response.setContentType("application/save-as-dialog");
        response.setContentType("application/vnd.ms-excel");
        response.setContentLength((int)file.length());
        response.setHeader("Content-Disposition","attachment;filename=webex.xls");
        OutputStream stream =response.getOutputStream();
         byte[] bufferData=new byte[1024];
        int read=0;
        while((read=fis.read(bufferData))!=-1){
        stream.write(bufferData,0,read);
        }
        stream.flush();
        stream.close();
        fis.close();  
        }              
    }
} 


