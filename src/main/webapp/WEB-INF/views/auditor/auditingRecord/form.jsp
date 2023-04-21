<%--
- form.jsp
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

<acme:form>
	<acme:input-textbox code="auditor.auditingRecord.form.label.subject" path="subject"/>	
	<acme:input-textbox code="auditor.auditingRecord.form.label.assessment" path="assessment"/>	
	<acme:input-moment code="auditor.auditingRecord.form.label.startPeriod" path="startPeriod" />
	<acme:input-moment code="auditor.auditingRecord.form.label.endPeriod" path="endPeriod" />
	<acme:input-select code="auditor.auditingRecord.form.label.mark" path="mark" choices="${marks}"/>
	<acme:input-url code="auditor.auditingRecord.form.label.furtherInformationLink" path="furtherInformationLink"/>
		
	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && published == true}">
			<acme:submit code="auditor.auditingRecord.form.button.update" action="/auditor/auditingRecord/update"/>
			<acme:submit code="auditor.auditingRecord.form.button.delete" action="/auditor/auditingRecord/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create' && published == true}">
			<acme:submit code="auditor.auditingRecord.form.button.create" action="/auditor/auditingRecord/create?masterId=${masterId}"/>
		</jstl:when>	
		<jstl:when test="${_command == 'create'&& published == false}">
			<acme:input-checkbox code="auditor.auditingRecord.form.button.confirmation" path="confirmation"/>
			<acme:submit code="auditor.auditingRecord.form.button.create-exceptional" action="/auditor/auditingRecord/create?masterId=${masterId}"/>
		</jstl:when>	
	</jstl:choose>
	
</acme:form>
