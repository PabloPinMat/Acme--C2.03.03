<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form readonly = "true">
	<acme:input-textbox code="authenticated.audit.form.label.code" path="code"/>	
	<acme:input-textbox code="authenticated.audit.form.label.conclusion" path="conclusion"/>
	<acme:input-textbox code="authenticated.audit.form.label.strongPoints" path="strongPoints"/>
	<acme:input-textbox code="authenticated.audit.form.label.weakPoints" path="weakPoints"/>
		<acme:input-textbox code="authenticated.audit.form.label.auditor" path="auditor"/>	
</acme:form>
