<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.audit.list.label.code" path="code"  width="20%"/>
	<acme:list-column code="authenticated.audit.list.label.conclusion" path="conclusion"  width="40%"/>
	<acme:list-column code="authenticated.audit.list.label.strongPoints" path="strongPoints"  width="20%"/>
	<acme:list-column code="authenticated.audit.list.label.weakPoints" path="weakPoints"  width="20%"/>
</acme:list>
