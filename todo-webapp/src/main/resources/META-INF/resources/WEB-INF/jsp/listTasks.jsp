<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>	

<div class="container">
	<h2>Your have following pending tasks:</h2>
	
	<table class="table">
		<thead>
			<tr>
				<th>Description</th>
				<th>Target Date</th>
				<th>Is Done?</th>
				<th></th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${tasks}" var="task">
				<tr>
					<td>${task.description}</td>
					<td>${task.targetDate}</td>
					<td>${task.done}</td>
					<!-- here id is not a sensitive information. Therefore, we are passing it in the url to tell the controller which Task to delete -->
					<td><a href="delete-task?id=${task.id}" class="btn btn-warning">Delete</a></td>
					<td><a href="update-task?id=${task.id}" class="btn btn-success">Update</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<a href="add-task" class="btn btn-success">Add Task</a>
	<%@ include file="common/footer.jspf" %>