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
	<acme:list-column code="lecturer.lecture.list.label.title" path="title"  width="40%"/>
	<acme:list-column code="lecturer.lecture.list.label.abstractt" path="abstractt" width="40%" />
	<acme:list-column code="lecturer.lecture.list.label.estimatedLearningTime" path="estimatedLearningTime" width="20%" />
	<acme:list-column code="lecturer.lecture.list.label.lectureType" path="lectureType" width="20%"/>
	<acme:list-column code="lecturer.lecture.list.label.draftMode" path="draftMode" width="20%"/>
</acme:list>

<jstl:if test="${_command == 'list-all'}">
	<acme:button code="lecturer.lecture.list.button.create" action="/lecturer/lecture/create"/>
</jstl:if>


