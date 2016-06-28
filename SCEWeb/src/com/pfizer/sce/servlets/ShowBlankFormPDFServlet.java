package com.pfizer.sce.servlets; 


import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * This servlet reads image data (in the form of a byte[]) from the request object.
 * It outputs the byte[] as a visible image.
 */

public class ShowBlankFormPDFServlet extends HttpServlet
{ 
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        byte[] rgb = (byte[]) request.getAttribute("byArr");
        if (rgb != null)
        {
            response.setContentType("application/save-as-dialog");
            response.setHeader("Content-Disposition", "attachment;filename=EvaluationForm.pdf");

            OutputStream stream = response.getOutputStream();
            stream.write(rgb);
        }
        else
        {
            response.setContentType("text");
            response.getWriter().write("attribute byArr not found");
        }
    }
}