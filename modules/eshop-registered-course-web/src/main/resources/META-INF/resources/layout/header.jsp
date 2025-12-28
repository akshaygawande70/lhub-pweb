<div class="container">
	<div class="row">
		<div class="breadcrumb">
			<ul>
				<li><a href="/home" class="text-dark">Home</a></li>
				<li><a href="/account/course_listing" class="text-dark">Registered
						Courses</a></li>
				<li>${titlePage}</li>
			</ul>
		</div>
	</div>
</div>
<div class="container">
	<div class="row">
		<div class="text-center w100">
			<c:choose>
				<c:when test="${accType == 1}">
					<h1 class="h1-commerce">REGISTERED COURSES</h1>
				</c:when>
				<c:when test="${accType == 2}">
					<h1 class="h1-commerce">REGISTERED COURSES - ADMIN</h1>
				</c:when>
				<c:otherwise>
					<h1 class="h1-commerce">REGISTERED COURSES</h1>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>