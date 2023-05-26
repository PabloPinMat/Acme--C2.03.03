<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="assistant.tutorial.session.form.label.title" path="title"/>
	<acme:input-textarea code="assistant.tutorial.session.form.label.abstract$" path="abstract$"/>
	<acme:input-moment code="assistant.tutorial.session.form.label.startSession" path="startSession"/>
	<acme:input-moment code="assistant.tutorial.session.form.label.finishSession" path="finishSession"/>
	
	<acme:input-select code="assistant.tutorial.session.form.label.type" path="sessionType" choices="${sessionType}"/>
	<acme:input-moment code="assistant.tutorial.session.form.label.furtherInformation" path="furtherInformation"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish')}">		
			<acme:submit code="assistant.tutorial.session.form.button.update" action="/assistant/tutorial-session/update"/>
			<acme:submit code="assistant.tutorial.session.form.button.delete" action="/assistant/tutorial-session/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistant.tutorial.session.form.button.create" action="/assistant/tutorial-session/create?tutorialId=${tutorialId}"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>