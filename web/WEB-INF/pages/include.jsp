<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<a href="${pageContext.request.contextPath}/">Go home</a>
<br/>Your logged as <sec:authentication property="principal.username"/>
<br/>You are <sec:authentication property="principal.authorities"/>

<br/>
