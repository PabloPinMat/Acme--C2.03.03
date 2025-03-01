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
	<acme:list-column code="auditor.auditingRecord.list.label.subject" path="subject"/>
	<acme:list-column code="auditor.auditingRecord.list.label.correction" path="correction"/>
    <acme:list-column code="auditor.auditingRecord.list.label.mark" path="mark"/> 
    <acme:list-column code="auditor.auditingRecord.list.label.published" path="published"/> 
</acme:list>
