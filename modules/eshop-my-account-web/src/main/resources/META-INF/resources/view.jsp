<%@page import="web.ntuc.eshop.myaccount.constants.MVCCommandNames"%>
<%@ include file="init.jsp"%>

<portlet:actionURL name="<%=MVCCommandNames.UPDATE_INFO%>"
	var="updateInfoURL">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:actionURL>

<section class="general-info myaccount-cxrus">
	<div class="container">
		<div class="row">
			<div class="breadcrumb-profile">
				<ul>
					<li><a href="/">Home</a></li>
					<li><a href="/account/profile">My Account</a></li>
					<li>Personal Info</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="row">
			<div class="text-center w100">
				<h1 class="h1-commerce">MY ACCOUNT</h1>
			</div>
		</div>
	</div>
	<c:forEach items="${accountList}" var="account">
		<div class="container">
			<div class="row">
				<div class="box-name">
					<div class="name" id="capital-name">HI, ${fullNameCap}</div>
					<div class="email">${account.email}</div>
				</div>
				<hr class="hr-bold" />
			</div>
		</div>
	</c:forEach>
	<div class="container">
		<div class="row">
			<div class="col-md-3">
				<div class="heading-title" data-id="#myacc">MY ACCOUNT</div>
				<div class="isi-account active" id="myacc">
					<ul>
						<li class="list-item active" data-id="#pi"><a
							href="/account/profile">Personal Info</a></li>
						<li class="list-item" data-id="#cp"><a
							href="/account/change_password" class="text-dark">Change
								Password</li>
					</ul>
				</div>
				<div class="heading-title" data-id="#register">
					<a href="/account/course_listing" class="text-dark">REGISTERED
						COURSE </a>
				</div>
				<div class="heading-title" data-id="#invoices">INVOICES</div>
				<div class="isi-account" id="invoices">
					<ul>
						<li class="list-item" data-id="#course-order-history"><a
							href="/account/courseinvoices" class="text-dark">Course Order
								History</a></li>

						<li class="list-item" data-id="#exam-order-history"><a
							href="/account/examinvoices" class="text-dark">Exam Order History</a></li>
						<!-- <li class="list-item" data-id="#merchandize-order-history"><a
							href="/account/merchandizeinvoices" class="text-dark">Merchandize Order
								History</a></li>
						<li class="list-item" data-id="#exam-merchandize-order-history"><a
							href="/account/exammerchandizeinvoices" class="text-dark">Exam & Merchandize Order History</a></li> -->
					</ul>
				</div>
			</div>
			<div class="col-md-9">
				<!--Start content-->
				<div class="content active" id="pi">
					<c:forEach items="${accountList}" var="account">
						<aui:form name="fm" method="post" action="${updateInfoURL}"
							enctype="multipart/form-data">
							<div class="fly-box-right">
								<div class="box-sc-image">
									<img alt="" class="rounded" src="${account.imgProfile}"
										id="imgPrev" /> <label class="upload-image"> SELECT
										IMAGE <aui:input cssClass="btn-upload" type="file"
											name="profilePicture" id="profilePicture" label=""
											accept=".jpg,.jpeg,.png" onchange="uploadPreview()">
										</aui:input>
									</label>
									<div class="normal">
										File size: Maximum 1 MB<br />File Extension: JPEG, PNG
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-12">
									<div class="heading-content">EDIT PERSONAL INFO</div>
									<div class="box-info">
										<c:choose>
											<c:when test="${type == '1'}">
												<div class="small">Full Name (as NRIC/FIN/Passport)</div>
												<div class="medium">${account.fullName}</div>
												<div class="small">NRIC/FIN/Passport Number</div>
												<div class="medium">${account.nric}</div>
												<div class="small">Date of Birth</div>
												<div class="medium">${account.birthDate}</div>
											</c:when>
											<c:otherwise>
												<div class="small">Company Name</div>
												<div class="medium">${account.companyName}</div>
												<div class="small">ACRA/UEN Number</div>
												<div class="medium">${account.uenNumber}</div>
												<div class="small">Corporate Administrator Name</div>
												<div class="medium">${account.fullName}</div>
												<div class="small">Corporate Administrator Email</div>
												<div class="medium">${account.email}</div>
											</c:otherwise>
										</c:choose>
									</div>

									<div class="heading-content">EDIT ADDRESS</div>
									<div class="box-input">
										<label for="fname">Contact Number</label>
										<aui:input cssClass="cs-form" type="text" id="contactNumber"
											placeholder="contact-number" name="contactNumber" label=""
											value="${account.contactNumber}">
											<aui:validator name="digits" />
										</aui:input>
										<aui:input cssClass="cs-form" type="hidden" id="phoneId"
											name="phoneId" label="" value="${account.phoneId}" />
									</div>
									<div class="row">
										<div class="col-md-6">
											<div class="box-input">
												<label for="fname">Address #1</label>
												<aui:input cssClass="cs-form" type="text" id="address1"
													placeholder="address1" name="address1" label=""
													value="${account.address1}" />
												<aui:input cssClass="cs-form" type="hidden" id="addressId"
													name="addressId" label="" value="${account.addressId}" />
											</div>
										</div>
										<div class="col-md-6">
											<div class="box-input">
												<label for="fname">Address #2</label>
												<aui:input cssClass="cs-form" type="text" id="address2"
													placeholder="address2" name="address2" label=""
													value="${account.address2}" />
											</div>
										</div>
									</div>
									<div class="box-input">
										<label for="fname">Postal Code</label>
										<aui:input cssClass="cs-form" type="text" id="postalCode"
											placeholder="postal-code" name="postalCode" label=""
											value="${account.postalCode}">
											<aui:validator name="digits" />
										</aui:input>
									</div>
									<div class="box-input">
										<label for="fname">Country/Region</label>
										<aui:select cssClass="cs-form" name="country" label="">
											<c:forEach items="${countryList}" var="country">
												<option value="${country.countryId}"
													<c:if test="${country.countryId == account.countryId}"> selected="selected"</c:if>>
													${country.countryName}</option>
											</c:forEach>
										</aui:select>
									</div>
									<div class="btn-box-right">
										<a class="btn-white" href="#">Cancel</a>
										<aui:button type="submit" id="submit" name="submit"
											cssClass="btn-blue" value="Save Changes" />
									</div>
								</div>
							</div>
						</aui:form>
					</c:forEach>
				</div>
				<!--End content-->
			</div>
		</div>
	</div>
</section>

<script type="text/javascript">
	function uploadPreview() {
    	imgPrev.src=URL.createObjectURL(event.target.files[0]);
	}
</script>