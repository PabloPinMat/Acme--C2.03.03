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
	<acme:input-textbox code="company.practicum-session.form.label.title" path="title"/>
	<acme:input-textbox code="company.practicum-session.form.label.abstract-session" path="abstractSession"/>
	<acme:input-moment code="company.practicum-session.form.label.start-time-period" path="startTimePeriod"/>
	<acme:input-moment code="company.practicum-session.form.label.end-time-period" path="endTimePeriod"/>
	<acme:input-url code="company.practicum-session.form.label.further-information-link" path="furtherInformationLink"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && publish == false}">
			<acme:submit code="company.practicum-session.form.button.update" action="/company/practicum-session/update"/>
			<acme:submit code="company.practicum-session.form.button.delete" action="/company/practicum-session/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create' && publish == false}">
			<acme:submit code="company.practicum-session.form.button.create" action="/company/practicum-session/create?masterId=${masterId}"/>
		</jstl:when>
		<jstl:when test="${_command == 'create' && publish == true}">
			<acme:input-checkbox code="company.practicum-session.form.label.confirmation" path="confirmation"/>
			<acme:submit code="company.practicum-session.form.button.create-exceptional" action="/company/practicum-session/create?masterId=${masterId}"/>
		</jstl:when>		
	</jstl:choose>		
</acme:form>
