<%--
- list.jsp
-
- Copyright (C) 2012-2023 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>

	<acme:list-column code="company.practicum-session.list.label.title" path="title" width="60"/>	
	<acme:list-column code="company.practicum-session.list.label.start-time-period" path="startTimePeriod" width="20%"/>
	<acme:list-column code="company.practicum-session.list.label.end-time-period" path="endTimePeriod" width="10%"/>
	<acme:list-column code="company.practicum-session.list.label.confirmation" path="exceptional" width="10%"/>
        
           
         
       
        
	
            
        
	
</acme:list>

<acme:button test="${showCreate && !exceptionalCreate}" code="company.practicum-session.list.button.create" action="/company/practicum-session/create?masterId=${masterId}"/>
<acme:button test="${showCreate && exceptionalCreate}" code="company.practicum-session.list.button.create-exceptional" action="/company/practicum-session/create?masterId=${masterId}"/>