<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="student.activity.list.label.title" path="title" width="40%"/>
	<acme:list-column code="student.activity.list.label.type" path="type" width="10%"/>
	<acme:list-column code="student.activity.list.label.startDate" path="startDate" width="25%"/>
	<acme:list-column code="student.activity.list.label.endDate" path="endDate" width="25%"/>
</acme:list>

<acme:button test="${showCreate}" code="student.activity.list.button.create" action="/student/activity/create?enrolmentId=${enrolmentId}"/>