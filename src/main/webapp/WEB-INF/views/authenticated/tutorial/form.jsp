<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form readonly = "true">
	<acme:input-textbox code="authenticated.tutorial.form.label.code" path="code"/>
	<acme:input-textbox code="authenticated.tutorial.form.label.title" path="title"/>
	<acme:input-textbox code="authenticated.tutorial.form.label.course.title" path="course.title"/>
	<acme:input-textbox code="authenticated.tutorial.form.label.abstract$" path="abstract$"/>
	<acme:input-textbox code="authenticated.tutorial.form.label.goals" path="goals"/>
	<acme:input-textbox code="authenticated.tutorial.form.label.estimatedTime" path="estimatedTime"/>
	<acme:input-textbox code="authenticated.tutorial.form.label.assitant.supervisor" path="assitant.supervisor"/>
	<acme:input-textbox code="authenticated.tutorial.form.label.assitant.expertiseField" path="assitant.expertiseField"/>
	<acme:input-textbox code="authenticated.tutorial.form.label.assitant.resume" path="assitant.resume"/>
	<acme:input-textbox code="authenticated.tutorial.form.label.assitant.furtherInformation" path="assitant.furtherInformation"/>

</acme:form>