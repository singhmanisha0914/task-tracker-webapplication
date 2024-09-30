<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>

<div class="container">
	<h2>Enter Task Details</h2>
	<form:form method="post" modelAttribute="taskTracker">
		
		<!-- Define label, input, and errors fields for the Description field. Also add margin using mb-1/mb-2/mb-3/mb-4-->
		<fieldset class="mb-3">
		<form:label path="description">Description</form:label>
		<form:input type="text" path="description" required="required"/>
		<!--display the description validation error message in case of error-->
		<form:errors path="description" cssClass="text-warning"/>
		</fieldset>
		
		<fieldset class="mb-3">
		<form:label path="targetDate">Target Date</form:label>
		<form:input type="text" path="targetDate" required="required"/>
		<!--display the description validation error message in case of error-->
		<form:errors path="targetDate" cssClass="text-warning"/>
		</fieldset>
		
		<!--create hidden variable-->
		<form:input type="hidden" path="id"/>
		<form:input type="hidden" path="done"/>
		<input type="submit" class="btn btn-success"/>
		
	</form:form>
	<%@ include file="common/footer.jspf" %>
	<!-- Script for poping the date while picking the target date (#targetDate where # indicates that we are using it as id). Also in datepicker framework mm indicates month-->
	<script type="text/javascript">
	$('#targetDate').datepicker({
		format: 'yyyy-mm-dd'
	});
	</script>