<script language="javascript">
    function openFAQ() {
        window.open('<%=request.getContextPath()%>/evaluation/faq.jsp','faq_window','status=yes,scrollbars=yes,height=600,width=800,resizable=yes');                    
        return false;
    }
    function openContact() {
        window.open('<%=request.getContextPath()%>/evaluation/contact.jsp','contact_window','status=yes,scrollbars=yes,height=600,width=800,resizable=yes');                    
        return false;
    }
    function opentemplatePrint() {
        window.open('<%=request.getContextPath()%>/evaluation/gotoPrintTemplate.do?templateid=4','print_window','status=yes,scrollbars=yes,height=600,width=800,resizable=yes,menubar=no');
        return false;
    }
    function showDocument(name) {
      window.open('<%=request.getContextPath()%>/evaluation/resources/pdf/'+name,'newDocWin','height=720,width=616,resizable,scrollbars');
    }
</script>
<%@ page import="com.pfizer.sce.beans.User"%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<%
     
    User user = (User)session.getAttribute("user");

		// System.out.println("User Details are :: "+user.getFirstName()+""+user.getLastName());
    boolean isAdmin = user != null && "SCE_Administrators".equalsIgnoreCase(user.getUserGroup()) || "SCE_OpsManager".equalsIgnoreCase(user.getUserGroup());
    boolean isTrainingTeam = user != null && "SCE_TrainingTeam".equalsIgnoreCase(user.getUserGroup());
    //Commented by Neha, 28Nov2011
    //boolean isGuestTrainer = user != null && "SCE_GuestTrainers".equalsIgnoreCase(user.getUserGroup());
    //Added by Neha, 28Nov2011
    boolean isGuestTrainer = user != null && "SCE_GuestTrainer_NonMGR".equalsIgnoreCase(user.getUserGroup())||"SCE_GuestTrainer_MGR".equalsIgnoreCase(user.getUserGroup());
    //LoggerHelper.logSystemDebug("User:" + user);    
    String admin="";
%>
<UL id=<%=(isAdmin || isTrainingTeam)?"top_nav":"top_nav1"%>>                                    
     <%--<LI id=nav_evaluate><netui:anchor action="gotoSelectAttendee">evaluate</netui:anchor></LI> --%>  

    <LI id=nav_search><s:url action="searchHome" var="schUrl" ></s:url> <s:a href="%{schUrl}">Search</s:a></LI>
    
    
    
    <%if (isAdmin) {
        admin="admin";
        session.setAttribute("admin",admin);
        %>
        <LI id=nav_admin><s:url action="admin" var="adminUrl" ></s:url><s:a href="%{adminUrl}">Admin</s:a></LI> 
    <%}%>
    <%--<LI id=nav_evaluate><a href="#" onclick="opentemplatePrint()">print blank form</a></LI> --%>
    <!--<LI id=nav_faq><a href="#" onclick="showDocument('SCETemplate.pdf');" title="Download Blank SCE Form">print blank form</a></LI>-->
   <%--CHANGE MADE ON 2 DEC BY RUPINDER----<LI id=nav_faq><netui:anchor action="gotoPrintBlankForm">print blank form</netui:anchor></LI>--%> 
   <LI id=nav_faq><s:url action="printblankform" var="printUrl"></s:url><s:a href="%{printUrl}">Print blank form</s:a></LI>
    <%--<LI id=nav_faq><netui:anchor action="gotoFaq">help/FAQ's</netui:anchor></LI>--%> 
    <%--<LI id=nav_faq><a href="#" onclick="openFAQ()">help/FAQ's</a></LI>--%> 
    <%--<LI class=last id=nav_contact><netui:anchor action="gotoContact">contact</netui:anchor></LI>--%>
    <LI class=last id=nav_contact><a href="#" onclick="openContact()">Contact</a></LI>
</UL>