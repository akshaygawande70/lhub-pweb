<%@page import="java.util.Locale"%>
<%@page import="java.util.stream.Collectors"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayPortletMode"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.liferay.portal.kernel.portlet.PortletURLFactoryUtil"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="com.liferay.portal.kernel.service.PortletPreferencesLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.model.PortletPreferences"%>
<%@page import="com.liferay.portal.kernel.service.LayoutLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.model.Layout"%>
<%@page import="com.liferay.portal.kernel.security.auth.AuthTokenUtil"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.commerce.tax.engine.fixed.service.CommerceTaxFixedRateLocalServiceUtil"%>
<%@page import="api.ntuc.common.util.CurrencyUtil"%>
<%@page import="api.ntuc.common.util.RoleUtil"%>
<%@page import="com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.model.User"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="com.liferay.asset.kernel.model.AssetCategory"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.asset.kernel.model.AssetEntry"%>
<%@page
	import="com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil"
%>
<%@page
	import="web.ntuc.eshop.checkout.custom.candidate.info.constants.CandidateInfoStepPortletKeys"
%>
<%@page
	import="web.ntuc.eshop.checkout.custom.candidate.info.display.context.CandidateInfoStepDisplayContext"
%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.Serializable"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.liferay.commerce.model.CommerceOrder"%>
<%@page import="com.liferay.commerce.model.CommerceOrderItem"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="com.liferay.commerce.product.model.CPDefinition"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@ include file="/init.jsp"%>
<%
	CandidateInfoStepDisplayContext candidateInfoStepDisplayContext = (CandidateInfoStepDisplayContext) request
			.getAttribute(CandidateInfoStepPortletKeys.COMMERCE_CHECKOUT_CUSTOM_CANDIDATE_INFO_DISPLAY_CONTEXT);
	CommerceOrder commerceOrder = candidateInfoStepDisplayContext.getCommerceOrder();
	
	
	ArrayList<String> times = (ArrayList<String>) request.getAttribute("times");
	String time1Str = (String) request.getAttribute("time1Str");
	String time2Str = (String) request.getAttribute("time2Str");
	String time3Str = (String) request.getAttribute("time3Str");
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.UK);
	
	double commerceTaxFixedRate = CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).get(0).getRate();
	
%>
<section class="candidate">
	<div class="container">
		<div class="row">
			<%
				for (CommerceOrderItem item : commerceOrder.getCommerceOrderItems()) {
					CPDefinition cpDefinition = item.getCPDefinition();
					boolean productCitrep = (Boolean) cpDefinition.getExpandoBridge().getAttribute("CITREP");
					AssetEntry assetEntry = AssetEntryLocalServiceUtil.fetchEntry(CPDefinition.class.getName(),
							cpDefinition.getCPDefinitionId());
					List<AssetCategory> categories = assetEntry.getCategories();
					Map<String, AssetCategory> categoryMap = candidateInfoStepDisplayContext.getSingleCategory(categories);
					Set<String> listField = candidateInfoStepDisplayContext.getListField(commerceOrder, categoryMap);
					//	listField.contains(CandidateInfoStepPortletKeys.FULL_NAME)
			%>
			<div class="col-md-6">
				<!-- <div class="title">CANDIDATE INFORMATION</div>
				<div class="abstrak">Please complete the information fields
					below. All field are mandatory.</div> -->
				<div>
					<c:if
						test="<%=listField.contains(CandidateInfoStepPortletKeys.FULL_NAME)%>"
					>
						<div class="box-input">
							<label class="control-label" for="<portlet:namespace />full_name">Full Name (as
								NRIC/FIN/Passport) <span class="red">*</span>
							</label>
							<aui:input cssClass="clearable cs-form"
								name="full_name" label="" type="text" value="${user.fullName}"
							/>
						</div>
					</c:if>
					<c:if
						test="<%=listField.contains(CandidateInfoStepPortletKeys.DOB)%>"
					>
						<div class="box-input">
							<label for="<portlet:namespace />dob" class="control-label">Date of Birth <span
								class="red"
							>*</span></label>
							<aui:input cssClass="clearable cs-form date"
								name="dob" label="" type="date" value="${dob}"
							/>
						</div>
					</c:if>
					<c:if
						test="<%=listField.contains(CandidateInfoStepPortletKeys.GENDER)%>"
					>
						<div class="box-input">
							<label for="gender" class="control-label">Gender <span
								style="color: red;"
							>*</span></label>
							<aui:select cssClass="clearable cs-form " name="gender" label=""
								showEmptyOption="<%=true%>" required="true"
							>
								<aui:option value="<%=true %>"
									selected="${gender==true?true:false}"
								>Male</aui:option>
								<aui:option value="<%=false %>"
									selected="${gender==false?true:false}"
								>Female</aui:option>
							</aui:select>
						</div>
					</c:if>
					<c:if
						test="<%=listField.contains(CandidateInfoStepPortletKeys.ACCA_REG_NO)%>"
					>
						<div class="box-input">
							<label for="accaRegistrationNo" class="control-label">ACCA
								Registration No <span style="color: red;">*</span>
							</label>
							<aui:input cssClass="clearable cs-form "
								name="accaRegistrationNo" label="" type="text"
								value="${accaRegistrationNo}"
							>
								<aui:validator name="required" />
							</aui:input>
						</div>
					</c:if>
					<c:if
						test="<%=listField.contains(CandidateInfoStepPortletKeys.MS_ID)%>"
					>
						<div class="box-input">
							<label for="msId" class="control-label">Microsoft ID <span
								class="red"
							>*</span></label>
							<aui:input cssClass="clearable cs-form " name="msId" label=""
								type="text" value="${msId}"
							>
								<aui:validator name="required" />
							</aui:input>
						</div>
					</c:if>
					<c:if
						test="<%=listField.contains(CandidateInfoStepPortletKeys.PMI_ID)%>"
					>
						<div class="box-input">
							<label for="pmiId" class="control-label">PMI ID <span
								class="red"
							>*</span></label>
							<aui:input cssClass="clearable cs-form " name="pmiId" label=""
								type="text" value="${pmiId}"
							>
								<aui:validator name="required" />
							</aui:input>
						</div>
					</c:if>
					<c:if
						test="<%=listField.contains(CandidateInfoStepPortletKeys.PMI_MEMBERSHIP)%>"
					>
						<div class="box-input">
							<label for="pmiMembership" class="control-label">PMI
								Membership <span class="red">*</span>
							</label>
							<aui:select showEmptyOption="<%=true%>"
								cssClass="clearable cs-form " name="pmiMembership" label=""
								required="true"
							>
								<aui:option value="<%=true %>"
									selected="${pmiMembership==true?true:false}"
								>Member</aui:option>
								<aui:option value="<%=false %>"
									selected="${pmiMembership==false?true:false}"
								>Non-Member</aui:option>
							</aui:select>
						</div>
					</c:if>
					<!-- <div class="row">
						<div class="col-md-6">
							<div class="box-input">
								<label for="address">Address <span class="red">*</span></label>
								<input class="form-control cs-form" id="address" name="address"
									placeholder="Block 424 Ang Mio Kio Ave 3" required=""
									type="text"
								/>
							</div>
						</div>
						<div class="col-md-6">
							<div class="box-input">
								<label for="address">Address #2<span class="red">*</span></label>
								<input class="form-control cs-form" id="address" name="address"
									placeholder="#08-368" required="" type="text"
								/>
							</div>
						</div>
					</div>
					<div class="box-input">
						<label for="address">Postal Code<span class="red">*</span></label>
						<input class="form-control cs-form" id="address" name="address"
							placeholder="560424" required="" type="text"
						/>
					</div>
					<div class="box-input">
						<label for="address">Contact Number<span class="red">*</span></label>
						<input class="form-control cs-form" id="address" name="address"
							placeholder="82212783" required="" type="text"
						/>
					</div> -->
					<c:if
						test="<%=listField.contains(CandidateInfoStepPortletKeys.EMAIL_ADDRESS)%>"
					>
						<div class="box-input">
							<label for="<portlet:namespace />email_address" class="control-label">Email Address<span
								class="red"
							>*</span></label>
							<!-- <input class="form-control cs-form" id="email" name="email"
							placeholder="mikochan@cxrus.com" required="" type="text"
						/> -->
							<aui:input cssClass="clearable cs-form"
								name="email_address" label="" type="email" value="${user.emailAddress}">
								<aui:validator name="email"
									errorMessage="Allows only an email address"
								/>
							</aui:input>
						</div>
					</c:if>
					<div class="box-input form-check">
						<!-- <label class="form-check-label"> <input
							class="form-check-input" name="agree" required="true"
							type="checkbox"
						/> I Agree to Terms of condition By clicking submit, I consent ,
							and agree to submit information relating to another individual, I
							repseent and warrant that such individual also consents, to NTUC
							Learning Hub companies Terms of condition.
						</label> -->
						<aui:input id="agree" name="agree" type="checkbox" value="agree"
							label="I agree to the terms and conditions. By checking this box, I consent and agree to submit information relating to another individual, I represent and warrant that the individuals also consents to NTUC LearningHub's terms and conditions."
							cssClass="form-check-input" wrapperCssClass="inline-remover"
							inlineLabel="right"
						>
							<aui:validator name="required" errorMessage="Please check this box to proceed."/>
						</aui:input>
					</div>
					<!-- <button class="btn-blue" type="submit">Submit</button> -->
				</div>
			</div>
			<!--CONTENT RIGHT-->
			<div class="col-md-6">
				<c:if
					test="<%=listField.contains(CandidateInfoStepPortletKeys.NOTES_1)%>"
				>
					<div class="alert alert-danger cs-alert" role="alert">
						<liferay-ui:message
							key="<%=candidateInfoStepDisplayContext.getOrderNotes1()%>"
						/>
					</div>
				</c:if>
				<c:if
					test="<%=listField.contains(CandidateInfoStepPortletKeys.NOTES_2)%>"
				>
					<div class="desc-exam">
						<liferay-ui:message
							key="<%=candidateInfoStepDisplayContext.getOrderNotes2()%>"
						/>
					</div>
				</c:if>
				<div class="box-selection-date">
					<c:if
						test="<%=listField.contains(CandidateInfoStepPortletKeys.EXAM_CODE_AND_TITLE)%>"
					>
						<div class="image-course">
							<img alt="" class="img-fluid"
								src="<%=candidateInfoStepDisplayContext.getCommerceOrderItemThumbnailSrc(item)%>"
							/>
						</div>
					</c:if>
					<div class="box-date-exams box-input">
						<c:set var="productName" value="<%=HtmlUtil.escape(cpDefinition.getName(themeDisplay.getLanguageId())) %>" />
						<c:if
							test="<%=listField.contains(CandidateInfoStepPortletKeys.EXAM_CODE_AND_TITLE)%>"
						>
							<div class="name-exams"><%=HtmlUtil.escape(cpDefinition.getName(themeDisplay.getLanguageId()))%></div>
							<div class="desc-exams"><%=HtmlUtil.escape(cpDefinition.getShortDescription(themeDisplay.getLanguageId()))%></div>
						</c:if>
						<c:if
							test="<%=listField.contains(CandidateInfoStepPortletKeys.EXAM_DATE)%>"
						>
							<!-- Date 1 -->
							<div class="section-exams">
								<div class="section-title">Selection 1</div>
								<div class="toggle-select" data-id="#one">Select Date
									&amp; Time</div>
								<div class="box-date-select" id="one">
									<div class="box-input date-input">
										<label for="date1" class="control-label">Date<span
											class="red"
										>*</span></label>
										<aui:input name="date1" label="" showRequiredLabel=""
											cssClass="clearable cs-form date" type="date"
											value="${date1Str}" placeholder="DD/MM/YYYY" required=""
										>
											<aui:validator name="required" />
											<aui:validator name="custom"
												errorMessage="Please choose a future time and date."
											>
											 function (val, fieldNode, ruleValue) {
											 		var form = Liferay.Form.get('<portlet:namespace />fm');
													var flag = false;
													var date = new Date(val);
													var today = new Date();
													flag = date >= today;
													/*var isDateValid = true;
													if(!flag){
														isDateValid = dayValidation(form, fieldNode, val);
													}*/
													errorFormHandling();
													return flag;
											  }
										</aui:validator>
										</aui:input>
										<%-- <input name="<portlet:namespace />date1" type="text"
										id="<portlet:namespace />date1" class="clearable cs-form date"
										data-date-format="dd-MM-yyyy" required autocomplete="off"
										value="<%=date1Str %>"
									></input> --%>
									</div>
									<div class="box-input">
										<aui:select id="time1" name="time1" cssClass="cs-form option"
											label=""
										>
											<%
												for (String time : times) {
																boolean selected = false;
																if (time.equals(time1Str)) {
																	selected = true;
																}
											%>
											<aui:option value="<%=time%>" selected="<%=selected%>"><%=time%></aui:option>
											<%
												}
											%>
										</aui:select>
									</div>
								</div>
							</div>
							<!-- Date 2 -->
							<div class="section-exams">
								<div class="section-title">Selection 2</div>
								<div class="toggle-select" data-id="#two">Select Date
									&amp; Time</div>
								<div class="box-date-select" id="two">
									<div class="box-input date-input">
										<label for="date2" class="control-label">Date<span
											class="red"
										>*</span></label>
										<aui:input name="date2" label="" showRequiredLabel=""
											cssClass="clearable cs-form date" type="date"
											value="${date2Str}" placeholder="DD/MM/YYYY" required=""
										>
											<aui:validator name="required" />
											<aui:validator name="custom"
												errorMessage="Please choose a future time and date."
											>
											 function (val, fieldNode, ruleValue) {
													var form = Liferay.Form.get('<portlet:namespace />fm');
													var flag = false;
													var date = new Date(val);
													var today = new Date();
													flag = date >= today;
													/*var isDateValid = true;
													if(!flag){
														isDateValid = dayValidation(form, fieldNode, val);
													}*/
													errorFormHandling();
													return flag;
											  }
										</aui:validator>
										</aui:input>
									</div>
									<div class="box-input">
										<aui:select id="time2" name="time2" cssClass="cs-form option"
											label=""
										>
											<%
												for (String time : times) {
																boolean selected = false;
																if (time.equals(time2Str)) {
																	selected = true;
																}
											%>
											<aui:option value="<%=time%>" selected="<%=selected%>"><%=time%></aui:option>
											<%
												}
											%>
										</aui:select>
									</div>
								</div>
							</div>
							<!-- Date 3 -->
							<div class="section-exams">
								<div class="section-title">Selection 3</div>
								<div class="toggle-select" data-id="#three">Select Date
									&amp; Time</div>
								<div class="box-date-select" id="three">
									<div class="box-input date-input">
										<label class="control-label" for="date3">Date<span
											class="red"
										>*</span></label>
										<aui:input name="date3" label="" showRequiredLabel=""
											cssClass="clearable cs-form date" type="date"
											value="${date3Str}" placeholder="DD/MM/YYYY" required=""
										>
											<aui:validator name="required" />
											<aui:validator name="custom"
												errorMessage="Please choose a future time and date."
											>
											 function (val, fieldNode, ruleValue) {
											 		var form = Liferay.Form.get('<portlet:namespace />fm');
													var flag = false;
													var date = new Date(val);
													var today = new Date();
													flag = date >= today;
													/*var isDateValid = true;
													if(!flag){
														isDateValid = dayValidation(form, fieldNode, val);
													}*/
													errorFormHandling();
													return flag;
											  }
										</aui:validator>
										</aui:input>
									</div>
									<div class="box-input">
										<aui:select id="time3" name="time3" cssClass="cs-form option"
											label=""
										>
											<%
												for (String time : times) {
																boolean selected = false;
																if (time.equals(time3Str)) {
																	selected = true;
																}
											%>
											<aui:option value="<%=time%>" selected="<%=selected%>"><%=time%></aui:option>
											<%
												}
											%>
										</aui:select>
									</div>
								</div>
							</div>
						</c:if>
						<c:if
							test="<%=listField.contains(CandidateInfoStepPortletKeys.ADDITIONAL_NOTES)%>"
						>
							<div class="additional-notes">Additional Notes</div>
							<div class="additional-box">
								<aui:input label="" name="additionalNotes" id="additionalNotes"
									type="textarea" value="${addNotes}"
								/>
							</div>
						</c:if>
						<c:if
							test="<%=listField.contains(CandidateInfoStepPortletKeys.CANCEL_REASON)%>"
						>
							<div class="additional-notes">Reason For Cancellation</div>
							<div class="additional-box">
								<aui:input label="" name="reasonForCancellation"
									id="reasonForCancellation" type="textarea"
									value="${reasonForCancellation}"
								/>
							</div>
						</c:if>
						<c:if test="<%=productCitrep%>">
							<%-- <div class="box-input form-check">
							<label class="form-check-label"> <input
								class="form-check-input" name="<portlet:namespace />citrep" 
								type="checkbox" onclick="showMe('boxContent')"
							/> Please select if you are applying for CITREP + funding
							</label>
						</div> --%>
							<div class="box-input form-check">
								<label class="form-check-label" for="citrep"> <aui:input
										id="citrep" name="citrep" type="checkbox"
										onClick="showMe('boxContent','citrep')" value="${citrep}"
										label="" cssClass="form-check-input"
										wrapperCssClass="inline-remover"
									>
									</aui:input> Please select if you are applying for CITREP + funding
								</label>
							</div>
						</c:if>
					</div>
				</div>
			</div>
			<!--END CONTENT RIGHT-->
			<div id="boxContent" style="display: none; margin-top: 30px;">
				<c:if test="<%=productCitrep%>">
					<div class="box-content-check">
						<div class="box-blue">
							<p>You have selected to apply for CITREP + Funding.</p>
							<p>Please note CITREP + Funding is for Singapore Citizen or
								Singapore Permanent Resident and above 21 years only.</p>
							<p>Please complete all the information below :</p>
						</div>
						<div class="box-border">
							<p>University/Polytechnic students & NSF who wants to take
								advantage of CITREP + funded certification will need to proceed
								to our counter at 73 Bras Basah Road #02 01, Singapore 189556 to
								purchase the examination. If you buy through this channel,
								please note you will not qualify for CITREP + Funding.</p>
							<p>Eligible Students/NSF who wish to apply for CITREP +
								funded certifications (exam) are required to produce a letter of
								support and proof of matriculation from the PSEIs of Singapore
								at the time of registration at the Counter. Full Time NSF or
								those that reached their ORD within 6 months will need to
								produce relevant PSEI gradutaion certifications and/or proof of
								National Service enlistment / ORD status at the time of
								registration at the counter.</p>
						</div>
						<div class="box-form">
							<div class="row box-text">
								<div class="col-md-6">
									<div class="box-input">
										<label class="control-label" for="citrepNric">Name (As per NRIC)<span
											class="red"
										>*</span>
										</label>
										<aui:input cssClass="clearable cs-form " name="citrepFullNameNric"
											label="" type="text" value="${citrepFullNameNric}"
										>
											<aui:validator name="required">
									                function() {
									                        return AUI.$('#<portlet:namespace />citrep').prop('checked');
									                }
									      	  </aui:validator>
										</aui:input>
									</div>
								</div>
								<div class="col-md-6">
									<div class="box-input date-input">
										<label class="control-label" for="citrepDob">Date of
											Birth (DD-MM-YYYY)<span class="red">*</span>
										</label>
										<aui:input name="citrepDob" label="" showRequiredLabel=""
											cssClass="clearable cs-form date" type="date"
											value="${citrepDobStr}" placeholder="DD/MM/YYYY" required=""
										>
											<aui:validator name="required">
									                function() {
									                        return AUI.$('#<portlet:namespace />citrep').prop('checked');
									                }
									      	  </aui:validator>
											<aui:validator name="custom"
												errorMessage="Date must not bigger than today"
											>
											 function (val, fieldNode, ruleValue) {
											 		var form = Liferay.Form.get('<portlet:namespace />fm');
													var flag = false;
													var date = new Date(val);
													var today = new Date();
													flag = date < today;
													return flag;
											  }
										</aui:validator>
										</aui:input>
									</div>
								</div>
								<div class="col-md-6">
									<div class="box-input">
										<label class="control-label" for="citrepEmail">Email
											Address <span class="red">*</span>
										</label>
										<aui:input cssClass="clearable cs-form " name="citrepEmail"
											label="" type="email" value="${citrepEmail}"
										>
											<aui:validator name="required">
									                function() {
									                        return AUI.$('#<portlet:namespace />citrep').prop('checked');
									                }
									     </aui:validator>
											<aui:validator name="email"
												errorMessage="Allows only an email address"
											/>
										</aui:input>
									</div>
								</div>
								<div class="col-md-6">
									<div class="box-input">
										<label class="control-label" for="citrepMobileNumber">Mobile
											Number <span class="red">*</span>
										</label>
										<aui:input cssClass="clearable cs-form "
											name="citrepMobileNumber" label="" type="text"
											value="${citrepMobileNumber}"
										>
											<aui:validator name="required">
									                function() {
									                        return AUI.$('#<portlet:namespace />citrep').prop('checked');
									                }
									      	  </aui:validator>
											<aui:validator name="number"
												errorMessage="Allows only numerical values"
											/>
										</aui:input>
									</div>
								</div>
								<div class="col-md-6">
									<div class="box-input">
										<label class="control-label" for="citrepNric">Profession
											<span class="red">*</span>
										</label>
										<aui:input cssClass="clearable cs-form "
											name="citrepProfession" label="" type="text"
											value="${citrepProfession}"
										>
											<aui:validator name="required">
									                function() {
									                        return AUI.$('#<portlet:namespace />citrep').prop('checked');
									                }
									      	  </aui:validator>
										</aui:input>
									</div>
								</div>
							</div>
							<div class="row box-radio">
								<div class="row w-100">
									<div class="col-md-6">
										Full time Student (University / Polytechnic) <span class="red">*</span><br />
										<aui:input cssClass="clearable cs-form" name="citrepStatus"
											id="citrepStatus1" label="" type="radio" value="false"
											checked="${citrepFullTimeStudent==false?true:false}"
										>
											<aui:validator name="required">
									          function() {
									               return AUI.$('#<portlet:namespace />citrep').prop('checked');
									          }
										</aui:validator>
										</aui:input>
										<label>No</label> <br />
										<aui:input cssClass="clearable cs-form" name="citrepStatus"
											id="citrepStatus2" label="" type="radio" value="true"
											checked="${citrepFullTimeStudent==true?true:false}"
										>
											<aui:validator name="required">
									          function() {
									               return AUI.$('#<portlet:namespace />citrep').prop('checked');
									          }
										</aui:validator>
										</aui:input>
										<label>Yes</label>
									</div>
									<div class="col-md-6">
										Colour of NRIC <span class="red">*</span><br />
										<c:set var="nationality1" value="false" />
										<c:forEach var="item" items="${citrepNationality}">
											<c:if test="${item eq 'Singapore Citizen (Pink NRIC)'}">
												<c:set var="nationality1" value="true" />
											</c:if>
										</c:forEach>
										<c:set var="nationality2" value="false" />
										<c:forEach var="item" items="${citrepNationality}">
											<c:if
												test="${item eq 'Singapore Permanent Resident (Blue NRIC)'}"
											>
												<c:set var="nationality2" value="true" />
											</c:if>
										</c:forEach>
										<aui:input cssClass="clearable cs-form"
											name="citrepNationality" id="citrepNationality1" label=""
											type="radio" value="Singapore Citizen (Pink NRIC)"
											checked="${nationality1}"
											onClick="yesnoCheck('citrepNationality2','nationality-detail','citrepNationalityDetail')"
										>
											<aui:validator name="required">
									          function() {
									               return AUI.$('#<portlet:namespace />citrep').prop('checked');
									          }
										</aui:validator>
										</aui:input>
										<label>Singapore Citizen (Pink NRIC)</label><br />
										<aui:input cssClass="clearable cs-form"
											name="citrepNationality" id="citrepNationality2" label=""
											type="radio" value="Singapore Permanent Resident (Blue NRIC)"
											checked="${nationality2}"
											onClick="yesnoCheck('citrepNationality2','nationality-detail','citrepNationalityDetail')"
										>
											<aui:validator name="required">
									          function() {
									               return AUI.$('#<portlet:namespace />citrep').prop('checked');
									          }
										</aui:validator>
										</aui:input>
										<label>Singapore Permanent Resident (Blue NRIC)</label></br>
										<div class="box-input" id="nationality-detail"
											style="display: none;"
										>
											<label class="control-label" for="citrepNationalityDetail">Nationality<span
												class="red"
											>*</span>
											</label>
											<aui:input cssClass="clearable cs-form "
												name="citrepNationalityDetail" label="" type="text"
												value="${citrepNationalityDetail}"
											>
												<aui:validator name="required">
									                function() {
									                        return AUI.$('#<portlet:namespace />citrepNationality2').prop('checked');
									                }
									      	  </aui:validator>
											</aui:input>
										</div>
									</div>
								</div>
								<div class="row w-100">
									<div class="col-md-6">
										<c:set var="citrepCompanySponsored1" value="false" />
										<c:forEach var="item" items="${citrepCompanySponsored}">
											<c:if test="${item eq 'No'}">
												<c:set var="citrepCompanySponsored1" value="true" />
											</c:if>
										</c:forEach>
										<c:set var="citrepCompanySponsored2" value="false" />
										<c:forEach var="item" items="${citrepCompanySponsored}">
											<c:if test="${item eq 'Yes'}">
												<c:set var="citrepCompanySponsored2" value="true" />
											</c:if>
										</c:forEach>
										Company Sponsored <span class="red">*</span><br />
										<aui:input cssClass="clearable cs-form"
											name="citrepCompanySponsored" id="citrepCompanySponsored1" label="" type="radio"
											value="No" checked="${ citrepCompanySponsored1}"
											onClick="yesnoCheck('citrepCompanySponsored2','company-name','citrepCompanyName')"
										>
											<aui:validator name="required">
									          function() {
									               return AUI.$('#<portlet:namespace />citrep').prop('checked');
									          }
										</aui:validator>
										</aui:input>
										<label>No</label> <br />
										<aui:input cssClass="clearable cs-form"
											name="citrepCompanySponsored" id="citrepCompanySponsored2"
											label="" type="radio" value="Yes"
											checked="${ citrepCompanySponsored2}"
											onClick="yesnoCheck('citrepCompanySponsored2','company-name','citrepCompanyName')"
										>
											<aui:validator name="required">
									          function() {
									               return AUI.$('#<portlet:namespace />citrep').prop('checked');
									          }
											</aui:validator>
										</aui:input>
										<label>Yes</label>
										<div class="box-input" id="company-name">
											<label class="control-label" for="citrepCompanyName">Company
												Name<span class="red">*</span>
											</label>
											<aui:input cssClass="clearable cs-form "
												name="citrepCompanyName" label="" type="text"
												value="${citrepCompanyName}"
											>
												<aui:validator name="required">
									                function() {
									                        return AUI.$('#<portlet:namespace />citrepCompanySponsored2').prop('checked');
									                }
									      	 	</aui:validator>
											</aui:input>
										</div>
									</div>
									<div class="col-md-6">
										Exam Date <span class="red">*</span><br />
										<aui:input cssClass="clearable cs-form" name="citrepExamDate" id="citrepExamDate1"
											label="" type="radio" value="false"
											checked="${citrepIsExamDate==false?true:false}"
											onClick="yesnoCheck('citrepExamDate2','exam-date-detail','citrepExamDateDetail')"
										>
											<aui:validator name="required">
									          function() {
									               return AUI.$('#<portlet:namespace />citrep').prop('checked');
									          }
											</aui:validator>
										</aui:input>
										<label>No</label> <br />
										<aui:input cssClass="clearable cs-form" name="citrepExamDate"
											id="citrepExamDate2" label="" type="radio" value="true"
											checked="${citrepIsExamDate==true?true:false}"
											onClick="yesnoCheck('citrepExamDate2','exam-date-detail','citrepExamDateDetail')"
										>
											<aui:validator name="required">
									          function() {
									               return AUI.$('#<portlet:namespace />citrep').prop('checked');
									          }
											</aui:validator>
										</aui:input>
										<label>Yes</label>
										<div class="box-input" id="exam-date-detail">
											<label class="control-label" for="citrepExamDateDetail">Exam
												Date<span class="red">*</span>
											</label>
											<aui:input name="citrepExamDateDetail" label=""
												showRequiredLabel="" cssClass="clearable cs-form date"
												type="date" value="${citrepExamDateStr}"
												placeholder="DD/MM/YYYY" required=""
											>
												<aui:validator name="required">
											          function() {
											               return AUI.$('#<portlet:namespace />citrepExamDate2').prop('checked');
											          }
												</aui:validator>
												<aui:validator name="custom"
													errorMessage="Date must bigger than today"
												>
												 function (val, fieldNode, ruleValue) {
												 		var form = Liferay.Form.get('<portlet:namespace />fm');
														var flag = false;
														var date = new Date(val);
														var today = new Date();
														flag = date >= today;
														errorFormHandling();
														return flag;
												  }
											</aui:validator>
											</aui:input>
										</div>
									</div>
								</div>
								<div class="col-md-12" style="margin-top: 70px;">
									Declaration <span class="red">*</span><br />
									<c:set var="declaration1" value="false" />
									<c:forEach var="item" items="${citrepDeclaration}">
										<c:if test="${item eq 'citrep-declaration-1'}">
											<c:set var="declaration1" value="true" />
										</c:if>
									</c:forEach>
									<c:set var="declaration2" value="false" />
									<c:forEach var="item" items="${citrepDeclaration}">
										<c:if test="${item eq 'citrep-declaration-2'}">
											<c:set var="declaration2" value="true" />
										</c:if>
									</c:forEach>
									<c:set var="declaration3" value="false" />
									<c:forEach var="item" items="${citrepDeclaration}">
										<c:if test="${item eq 'citrep-declaration-3'}">
											<c:set var="declaration3" value="true" />
										</c:if>
									</c:forEach>
									<div class="box-input form-check">
										<label class="form-check-label" for="declaration1"> <aui:input
												id="citrepDeclaration1" name="citrepDeclaration1"
												type="checkbox" checked="${ declaration1}"
												value="citrep-declaration-1" label=""
												cssClass="form-check-input" wrapperCssClass="inline-remover"
											>
												<aui:validator name="required">
											        function() {
											            return AUI.$('#<portlet:namespace />citrep').prop('checked');
											        }
											    </aui:validator>
											</aui:input> I acknowledge the use of these details are for CITREP +
											Funding enrolment.
										</label>
									</div>
									<div class="box-input form-check">
										<label class="form-check-label" for="declaration2"> <aui:input
												id="citrepDeclaration2" name="citrepDeclaration2"
												type="checkbox" checked="${ declaration2}"
												value="citrep-declaration-2" label=""
												cssClass="form-check-input" wrapperCssClass="inline-remover"
											>
												<aui:validator name="required">
											        function() {
											            return AUI.$('#<portlet:namespace />citrep').prop('checked');
											        }
											    </aui:validator>
											</aui:input> I understand I need to take my first exam ${citrepDateRange}
											to be eligible for CITREP + Funding and pass all exams within
											12 months from the first exam date.
										</label>
									</div>
									<div class="box-input form-check">
										<label class="form-check-label" for="declaration3"> <aui:input
												id="citrepDeclaration3" name="citrepDeclaration3"
												type="checkbox" checked="${ declaration3 }"
												value="citrep-declaration-3" label=""
												cssClass="form-check-input" wrapperCssClass="inline-remover"
											>
												<aui:validator name="required">
											        function() {
											            return AUI.$('#<portlet:namespace />citrep').prop('checked');
											        }
											    </aui:validator>
											</aui:input> All the information given in this form is true and accurate
											to the best of my knowledge. Any false or missleading
											declaration will result in claim application being rejected.
										</label>
									</div>
								</div>
								<%-- <input id="citrepDeclaration3" required name="<portlet:namespace />citrepDeclaration3" type="checkbox" ${ declaration3?'checked':''} value="citrep-declaration-3"/><label>All the information given in this form is true and accurate to the best of my knowledge. Any false or missleading declaration will result in claim application being rejected.</label><br /> --%>
								<div class="col-md-12">
									<label class="txt-area">Additional Notes :</label><br />
									<div class="additional-box">
										<aui:input label="" name="citrepAdditionalNotes"
											id="citrepAdditionalNotes" type="textarea"
											value="${citrepAdditionalNotes }"
										/>
									</div>
									<aui:input label="" name="citrepPdf"
											id="citrepPdf" type="hidden"
											value=""
									/>
									
									
									<div class="pull-right">
										<!-- <i class="fas fa-download"></i> <a onclick="downloadPdf()">Download
											CITREP</a>-->
										<c:if test="${not empty citrepPdf}">
										<i class="fas fa-download"></i> <a onclick="downloadCitrepPdf'${citrepPdf}')">Download
											CITREP</a>
										</c:if>
									</div>
								</div>
							</div>
						</div>
					</div>
				</c:if>
				<%
					int type = (int) request.getAttribute("accType");
					String authToken = AuthTokenUtil.getToken(request);
					String invoicePortletName = "NTUC_Invoice_Exam_Portlet";
					String invoiceUrl = "/account/examinvoices";
					Layout layout2 = LayoutLocalServiceUtil.getFriendlyURLLayout(themeDisplay.getSiteGroupId(), false, invoiceUrl);
					long plid2 = layout2.getPlid();
					List<PortletPreferences> preferences = PortletPreferencesLocalServiceUtil.getPortletPreferencesByPlid(plid2);
					List<PortletPreferences> filteredPreferences = preferences.stream().filter(x->x.getPortletId().contains(invoicePortletName)).collect(Collectors.toList());
					String portletId2 = filteredPreferences.get(0).getPortletId();
					PortletURL url = PortletURLFactoryUtil.create(request, portletId2, plid2 ,
						PortletRequest.RENDER_PHASE);
					url.setParameter("mvcRenderCommandName", "examOrderDetailRender");
					url.setParameter("accType", String.valueOf(type));
					url.setParameter("orderId", String.valueOf(commerceOrder.getCommerceOrderId()));
					url.setParameter("authToken", authToken);
					url.setWindowState(LiferayWindowState.MAXIMIZED);
					url.setPortletMode(LiferayPortletMode.VIEW);
				%>
				<aui:input label="" name="invoiceUrl"
					id="invoiceurl" type="hidden"
					value="<%=url %>"
				/>
				<aui:input label="" name="invoicePdf"
					id="invoicePdf" type="hidden"
					value=""
				/>
			</div>
			<%
				}
			%>
		</div>
		<!-- <div class="row">
			
		</div> -->
	</div>
</section>
<script type="text/javascript">
    var imgStr = "";
	$(document).ready(
			function() {
				getImgStr();
				$('.inline-remover').removeClass('form-inline');
				var citrep = "${citrep}";
				if (citrep) {
					showMe('boxContent', 'citrep');
				}
				var nationality2 = "${nationality2}";
				if (nationality2) {
					yesnoCheck('citrepNationality2', 'nationality-detail',
							'citrepNationalityDetail');
				}
				var companySponsored2 = "${citrepCompanySponsored2}";
				if (companySponsored2) {
					yesnoCheck('citrepCompanySponsored2', 'company-name',
							'citrepCompanyName');
				}
				var citrepIsExamDate = "${citrepIsExamDate}";
				if (citrepIsExamDate) {
					yesnoCheck('citrepExamDate2', 'exam-date-detail',
							'citrepExamDateDetail');
				}
				
				//set pdf value
				//var form = $('form[name="<portlet:namespace />fm"]');
				//var citrepPdf = $('#<portlet:namespace />citrepPdf');
				//form 
			});
	
	$('form[name="<portlet:namespace />fm"]').submit(function() {
		var citrepFullNameNRIC = $('#<portlet:namespace />citrepFullNameNric').val();
		var citrepDob = $('#<portlet:namespace />citrepDob').val();
		var citrepEmail = $('#<portlet:namespace />citrepEmail').val();
		var citrepMobileNumber = $('#<portlet:namespace />citrepMobileNumber').val();
		var citrepProfession = $('#<portlet:namespace />citrepProfession').val();
		var citrepStatus1 = $('#<portlet:namespace />citrepStatus1').is(':checked');
		var citrepStatus2 = $('#<portlet:namespace />citrepStatus2').is(':checked');
		var citrepNationality1 = $('#<portlet:namespace />citrepNationality1').is(':checked');
		var citrepNationality2 = $('#<portlet:namespace />citrepNationality2').is(':checked');
		var citrepNationalityDetail = $('#<portlet:namespace />citrepNationalityDetail').val();
		var citrepCompanySponsored1 = $('#<portlet:namespace />citrepCompanySponsored1').is(':checked');
		var citrepCompanySponsored2 = $('#<portlet:namespace />citrepCompanySponsored2').is(':checked');
		var citrepExamDate1 = $('#<portlet:namespace />citrepExamDate1').is(':checked');
		var citrepExamDate2 = $('#<portlet:namespace />citrepExamDate2').is(':checked');
		var citrepExamDateDetail = $('#<portlet:namespace />citrepExamDateDetail').val();
		var citrepDeclaration1 = $('#<portlet:namespace />citrepDeclaration1').is(':checked');
		var citrepDeclaration2 = $('#<portlet:namespace />citrepDeclaration2').is(':checked');
		var citrepDeclaration3 = $('#<portlet:namespace />citrepDeclaration3').is(':checked');
		var citrepAdditionalNotes = $('#<portlet:namespace />citrepAdditionalNotes').val();
		/*console.log("citrepFullNameNric = "+fullNameNRIC);
		console.log("citrepDob = "+citrepDob);
		console.log("citrepEmail = "+citrepEmail);
		console.log("citrepMobileNumber = "+citrepMobileNumber);
		console.log("citrepProfession = "+citrepProfession);
		console.log("citrepStatus1 = "+citrepStatus1);
		console.log("citrepStatus2 = "+citrepStatus2);
		console.log("citrepNationality1 = "+citrepNationality1);
		console.log("citrepNationality2 = "+citrepNationality2);
		console.log("citrepNationalityDetail = "+citrepNationalityDetail);
		console.log("citrepCompanySponsored1 = "+citrepCompanySponsored1);
		console.log("citrepCompanySponsored2 = "+citrepCompanySponsored2);
		console.log("citrepExamDate1 = "+citrepExamDate1);
		console.log("citrepExamDate2 = "+citrepExamDate2);
		console.log("citrepExamDateDetail = "+citrepExamDateDetail);
		console.log("citrepDeclaration1 = "+citrepDeclaration1);
		console.log("citrepDeclaration2 = "+citrepDeclaration2);
		console.log("citrepDeclaration3 = "+citrepDeclaration3);
		console.log("citrepAdditionalNotes = "+citrepAdditionalNotes);*/
		generateCitrepPdf(citrepFullNameNRIC,citrepDob,citrepEmail,citrepMobileNumber,citrepProfession,
				citrepStatus1,citrepStatus2,citrepNationality1,citrepNationality2,citrepNationalityDetail,
				citrepCompanySponsored1,citrepCompanySponsored2,citrepExamDate1,citrepExamDate2,
				citrepExamDateDetail,citrepDeclaration1,citrepDeclaration2,citrepDeclaration3,
				citrepAdditionalNotes);
		
		generateInvoicePdf();
	});

	function dayValidation(form, fieldNode, val) {
		var _ruleData = form.formValidator.get('fieldStrings')[fieldNode
				.get('name')];

		var weekendValidator = "Weekends not allowed";
		var pastValidator = "Date must bigger than today";
		var flag = true;
		var day = new Date(val).getUTCDay();
		if ([ 6, 0 ].includes(day)) {
			_ruleData.date1_custom = weekendValidator;
			flag = false;
		} else {
			_ruleData.date1_custom = pastValidator;
		}
		return flag;
	}

	function errorFormHandling() {
		$('.error-field').parents('.section-exams').find('.box-date-select')
				.addClass('active');
		$('.error-field').parents('.section-exams').find('.toggle-select')
				.addClass('active');
	}

	function showMe(box, targetId) {
		var chboxs = document.getElementsByName("<portlet:namespace />"
				+ targetId);
		var vis = "none";
		for (var i = 0; i < chboxs.length; i++) {
			if (chboxs[i].checked) {
				vis = "block";
				break;
			}
		}
		document.getElementById(box).style.display = vis;
	}

	function yesnoCheck(sourceId, targetId, targetFieldId) {
		if (document.getElementById('<portlet:namespace/>' + sourceId).checked) {
			document.getElementById(targetId).style.display = 'block';
		} else {
			document.getElementById(targetId).style.display = 'none';
			document.getElementById('<portlet:namespace/>' + targetFieldId).value = '';
		}

	}
	
	function getImgStr(){
		var src = "<%=request.getContextPath()%>/img/ntuclhub-logo.png";
		console.log(src);
 		var img = new Image();
 		img.crossOrigin = 'Anonymous';
 		img.onload = function() {
 			var canvas = document.createElement('CANVAS');
 			var ctx = canvas.getContext('2d');
 			var dataURL;
 			canvas.height = this.naturalHeight;
 			canvas.width = this.naturalWidth;
 			ctx.drawImage(this, 0, 0);
 			dataURL = canvas.toDataURL();
 			imgStr = dataURL;
 		};
 		img.src = src;
 		if (img.complete || img.complete === undefined) {
 			img.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
 			img.src = src;
 		}
 	}
	
	function downloadCitrepPdf(data) {
		var linkSource = 'data:application/pdf;base64,'+data;
	    var downloadLink = document.createElement('a');
	    document.body.appendChild(downloadLink);

	    downloadLink.href = linkSource;
	    downloadLink.target = '_self';
	    downloadLink.download = 'CITREP.pdf';
	    downloadLink.click(); 
		
	}
	
	function generateCitrepPdf(citrepFullNameNRIC,citrepDob,citrepEmail,citrepMobileNumber,citrepProfession,
			citrepStatus1,citrepStatus2,citrepNationality1,citrepNationality2,citrepNationalityDetail,
			citrepCompanySponsored1,citrepCompanySponsored2,citrepExamDate1,citrepExamDate2,
			citrepExamDateDetail,citrepDeclaration1,citrepDeclaration2,citrepDeclaration3,
			citrepAdditionalNotes) {
		try {
			var dd = {
				    footer: function (currentPage, pageCount) {
				        return {
				            table: {
				                body: [
				                    [
				                        {text:'FORM-BU-081K',fontSize:8,bold:true,margin:[50,0,0,0]},
				                        {text:'Data provided will be kept confidential and used for the purpose of this request',fontSize:8,margin: [30, 0, 0, 0]},
				                        { text: "Page " + currentPage.toString() + ' of ' + pageCount, alignment: 'right', margin: [30, 0, 0, 0],fontSize:8,bold:true }
				                    ],
				                ]
				            },
				            layout: 'noBorders'
				        };
				    },
				   content: [
				    {
				      columns: [
				        {
				         image: imgStr,
							width: 120
				        },
				        {
				            stack: [
				            'NTUC LEARNINGHUB PTE LTD',
				            '73 Bras Basah Road',
				            'NTUC Trade Union House, #02-01',
				            'Singapore 189556', 
				            'FAX 65 6486 7824',
				            'www.ntuclearninghub.com'
				          ],
				          fontSize: 8, color:'grey', alignment:'left', margin:[200,0,0,0]
				        },
				      ],
				    },
				    
				    
				    {
				        text: 'CITREP TRAINEE DETAIL FORM',
						alignment: 'center', margin:[0,23,0,0], bold:'true', fontSize:15
				    },
				     {
				        text: '(For Singapore citizens and PRs Only) ',
						alignment: 'center', fontSize:9,
						margin:[0,0,0,40]
				    },
				    
				    
				     {
				    	table: {
							body: [
								[
								    {
								        text:'Name (As per NRIC) :', fontSize:9, 
								        border: [false, false, false, false],
								        margin: [-5,0,10,0]
								    },
								    {
										border: [false, false, false, true],
										text: citrepFullNameNRIC,fontSize:9
									},
									{
										border: [false, false, false, false],
										text: ''
									}
									
								]
							],
							widths : ['auto','*','100'],
						}
			    },
			    {
				   	table: {
								body: [
									[
									    {
									        text:'DOB:', fontSize:9, 
									        border: [false, false, false, false],
									        margin: [-5,0,10,0]
									    },
									    {
											border: [false, false, false, true],
											text: citrepDob,fontSize:9
										},
										{
											border: [false, false, false, false],
											text: ''
										}
										
									]
								],
								widths : ['18%','*','100'],
							}
				    },
				    {
				   	table: {
								body: [
									[
									    {
									        text:'Colour of NRIC :', fontSize:9, 
									        border: [false, false, false, false],
									        margin: [-5,0,29,0]
									    },
										{
										    border: [false, false, false, false],
												table: {
													body: [
														[
														    {text: citrepNationality1 ? '\u221a' : ' ',
														        margin:[0,0,0,5]
														    },
														]
													]
												},
										},
										{
											text: 'Pink', fontSize:9,
											border: [false, false, false, false],
											margin: [-5,0,30,0]
										},
										{
										    border: [false, false, false, false],
												table: {
													body: [
														[
														    {text:citrepNationality2 ? '\u221a' : ' ',
														        margin:[0,0,0,5]
														    },
														]
													]
												},
										},
										{
											text: '*Blue', fontSize:9,
											border: [false, false, false, false],
											margin: [-5,0,30,0]
										},
									],
									
								]
							}
				    },
				    {
				        text: '* For PR, please state Nationality : ', margin:[99,5,0,0], fontSize:9
				    },
				    {
				   	table: {
								body: [
									[
										{
											border: [false, false, false, false],
											text: '',
											margin:[225,0,0,0]
										},
										{
											border: [false, true, false, false],
											text: citrepNationality2 ? citrepNationalityDetail : ' ',
											fontSize:9,
											margin:[0,-20,100,0]
										}
									],
									
								]
							}
				    },
				    
				    {
				   	table: {
								body: [
									[
									    {
									        text:'Status*:', fontSize:9, 
									        border: [false, false, false, false],
									        margin: [-5,0,59,0]
									    },
										{
										    border: [false, false, false, false],
												table: {
													body: [
														[
														    {text:citrepStatus2  ? '\u221a' : ' ',
														        margin:[0,0,0,5]
														    },
														]
													]
												},
										},
										{
											text: 'Full time Student (University / Polytechnic) ', fontSize:9,
											border: [false, false, false, false],
											margin: [-5,0,30,15]
										},
									],
									
								]
							}
				    },
				    {
					   	table: {
									body: [
										[
										    {
										        text:'Email Address :', fontSize:9, 
										        border: [false, false, false, false],
										        margin: [-5,0,0,0]
										    },
										    {
												border: [false, false, false, true],
												text: citrepEmail,fontSize:9
											},
											{
												border: [false, false, false, false],
												text: ''
											}
										],
									],
									widths:['18%','*',100]
								}
					    },
					    {
						   	table: {
										body: [
											[
											    {
											        text:'Contact Number :', fontSize:9, 
											        border: [false, false, false, false],
											        margin: [-5,0,0,0]
											    },
											    {
													border: [false, false, false, true],
													text: citrepMobileNumber,fontSize:9
												},
												{
													border: [false, false, false, false],
													text: ''
												}
											],
										],
										widths:['18%','*',100]
									}
						    },
				    
				     
				    {
				   	table: {
								body: [
									[
									    {
									        text:'Company Sponsored:', fontSize:9, 
									        border: [false, false, false, false],
									        margin: [-5,0,5,0]
									    },
										{
										    border: [false, false, false, false],
												table: {
													body: [
														[
														    {text:citrepCompanySponsored1 ? '\u221a' : ' ',
														        margin:[0,0,0,5]
														    },
														]
													]
												},
										},
										{
											colSpan: 1,
											text: 'No', fontSize:9,
											border: [false, false, false, false],
											margin: [-5,0,30,0]
										},
										{
										    border: [false, false, false, false],
												table: {
													body: [
														[
														    {text:citrepCompanySponsored2 ? '\u221a' : ' ',
														        margin:[0,0,0,5]
														    },
														]
													]
												},
										},
										{
											colSpan: 1,
											text: 'Yes', fontSize:9,
											border: [false, false, false, false],
											margin: [-5,0,30,0]
										},
										{
										    text: '(If Yes, state company\u0027s\n name): '+'${citrepCompanyName}', fontSize:9,
											border: [false, false, false, false],
											margin:[35,0,0,0]
										},
									],
									
								]
							}
				    },
				    {
				   	table: {
								body: [
									[
										{
											border: [false, false, false, false],
											text: '',
											margin:[89,0,0,0]
										},
										{
											border: [false, true, false, false],
											text: '',
											margin:[265,0,0,0]
										}
									],
									
								]
							}
				    },
				    
				    {
					   	table: {
									body: [
										[
										    {
										        text:'Profession :', fontSize:9, 
										        border: [false, false, false, false],
										        margin: [-5,0,0,0]
										    },
										    {
												border: [false, false, false, true],
												text: citrepProfession,fontSize:9
											},
											{
												border: [false, false, false, false],
												text: ''
											}
										],
									],
									widths:['18%','*',100]
								}
					    },
				    
				     {
				   	table: {
								body: [
									[
									    {
									        text:'Exam Date:', fontSize:9, 
									        border: [false, false, false, false],
									        margin: [-5,0,45,0]
									    },
											{
										    border: [false, false, false, false],
												table: {
													body: [
														[
														    {text:citrepExamDate1  ?  '\u221a' : ' ',
														        margin:[0,0,0,5]
														    },
														]
													]
												},
										},
										{
											colSpan: 1,
											text: 'No', fontSize:9,
											border: [false, false, false, false],
											margin: [-5,0,30,0]
										},
										{
										    border: [false, false, false, false],
												table: {
													body: [
														[
														    {text:citrepExamDate2   ? '\u221a' : ' ',
														        margin:[0,0,0,5]
														    },
														]
													]
												},
										},
										{
											colSpan: 1,
											text: 'Yes', fontSize:9,
											border: [false, false, false, false],
											margin: [-5,0,30,0]
										},
										{
										    text: '(If Yes, please state): ', fontSize:9,
											border: [false, false, false, false],
											margin:[35,0,0,0]
										},
										{
										    text: citrepExamDate2  ? citrepExamDateDetail :' ', fontSize:9,
											border: [false, false, false, false],
											margin:[0,0,0,0]
										},
									],
									
								]
							}
				    },
				    {
				   	table: {
								body: [
									[
										{
											border: [false, false, false, false],
											text: '(Mandatory for CITREP Funding Enrolment) ',
											fontSize:9,
											margin:[330,0,-140,0]
										},
										{
											border: [false, true, false, false],
											text: '',
											margin:[128,0,0,0]
										}
									],
									
								]
							}
				    },
				    
				    {text:'Note:',bold:true,fontSize:9,margin:[0,0,0,3]},
				    
				    {
							ol: [
								{text: 'Exam Date & time subjected to changes', margin:[0,0,0,3], fontSize:9},
								{text: 'For University/Polytechnic students, you are to provide the following documents to us before course start date', fontSize:9},
							]
						},
					{text:'Student* (Singapore citizen only):',bold:true,fontSize:9,margin:[20,3,0,0]},
				    {
				        type:'square',
						ul: [
							{text: 'Proof of matriculation', margin:[20,0,0,3], fontSize:9},
							{text: [
									{text:'Recommendation by PSEI (', fontSize:9},
									{text:'IDA Form 1: CITREP+ Student\n Supporting Recommendation Form',italics:true, fontSize:9},
									{text:')', fontSize:9},
									],
									margin:[20,0,0,3]
							},
							{text: [
									{text:'For below age of 18 as of 1 Jan of the current year require\n', fontSize:9},
									{text:'parent / guardian consent (', fontSize:9},
									{text:'IDA Form 1A: Application Form\n for Student / Full-Time National Service (NSF) For Claims\n under CITREP+',italics:true, fontSize:9},
									{text:')', fontSize:9},
									],
									margin:[20,0,0,3]
							},
						]
						},
						
					{text:'Exam Title (Please tick)',bold:true,fontSize:9,margin:[0,10,0,5],decoration:'underline'},
				     {
							table: {
								body: [
									[
										[
											{text: '', margin:[12,5,12,0]
										},
											{
												table: {
													body: [
														[
														    {
														        text: '\u221a', margin: [0,0,0,5]
														    },
														],
													]
												},
											}
										],
										{text: [
									{text:'${productName} (', fontSize:8},
									{text:'${citrepDateRange}',bold:true, fontSize:8},
									{text:')', fontSize:9},
									],
									margin:[5,3,50,3]
							},
										
									]
								]
							}
						},
					
					{text:'Declaration:',bold:true,fontSize:9,margin:[0,5,0,0]},
					{
				   	table: {
								body: [
									[
										{
										    border: [false, false, false, false],
												table: {
													body: [
														[
														    {text:citrepDeclaration1  ? '\u221a' : ' ',
														        margin:[0,0,0,5]
														    },
														]
													]
												},
										},
										{
											text: 'I acknowledge the use of these details are for CITREP+ funding enrolment', fontSize:9,
											border: [false, false, false, false],
											margin: [15,0,0,0]
										},
									],
									
								]
							}
				    },
				    {
				   	table: {
								body: [
									[
										{
										    border: [false, false, false, false],
												table: {
													body: [
														[
														    {text:citrepDeclaration2  ? '\u221a' : ' ',
														        margin:[0,0,0,5]
														    },
														]
													]
												},
										},
										{text: [
				        					{text:'I understand that CITREP+ funding for LHUB candidates expires on ', fontSize:9},
				        					{text:'${citrepDateRange} ',bold:true, fontSize:9},
				        					{text:'and I need to take my ', fontSize:9},
				        					{text:'first', fontSize:9,decoration:'underline'},
				    						{text:' exam before this date to be eligible for funding.', fontSize:9},
				        					],
				        					margin:[15,0,0,0], border: [false, false, false, false]
				        					},
									],
									
								]
							}
				    },
				    {
				    table: {
								body: [
									[
										{
										    border: [false, false, false, false],
												table: {
													body: [
														[
														    {text:citrepDeclaration3  ? '\u221a' : ' ',
														        margin:[0,0,0,5]
														    },
														]
													]
												},
										},
										{
											text: 'All the information given in this form is true and accurate to the best of my knowledge. Any false or misleading\n declaration will result in claim application being rejected. ', fontSize:9,
											border: [false, false, false, false],
											margin: [15,0,0,30]
										},
									],
									
								]
							}
				    },
				    
				    {
				   	table: {
								body: [
									[
										{
											border: [false, false, false, false],
											text: '',
											margin:[0,0,0,0]
										},
										{
											border: [false, true, false, false],
											text: '',
											margin:[120,0,0,5]
										}
									],
									
								]
							}
				    },
				    
				    {text:'Signature & Date',bold:true,fontSize:9,margin:[10,0,0,0],
				    
				    //pageBreak
				    pageBreak: 'after'},
				    
				    {
				        columns: [
				        {
				         image: imgStr,
							width: 120
				        },
				        {
				            stack: [
				            'NTUC LEARNINGHUB PTE LTD',
				            '73 Bras Basah Road',
				            'NTUC Trade Union House, #02-01',
				            'Singapore 189556', 
				            'FAX 65 6486 7824',
				            'www.ntuclearninghub.com'
				          ],
				          fontSize: 8, color:'grey', alignment:'left', margin:[200,0,0,0]
				        },
				      ],
				    },
				    
				    
				    {
				        text: 'CITREP TRAINEE DETAIL FORM',
						alignment: 'center', margin:[0,23,0,0], bold:'true', fontSize:15
				    },
				     {
				        text: '(For Singapore citizens and PRs Only) ',
						alignment: 'center', fontSize:9,
						margin:[0,0,0,40]
				    },
				    
				    {text:'Claim Conditions',bold:true, fontSize:10,margin:[0,0,0,10]},
				    {text:'For Certification Fees Support ',fontSize:10,decoration:'underline',margin:[0,0,0,10]},
				    {text: 'During registration, if you paid the exam fee, and provide NTUC Learninghub with the CITREP+ details for\n enrolment and fulfill the requirements for funding, we will enroll you for CITREP+ funding',
					fontSize:10,margin:[0,0,0,10]},
					{text: 'IMDA will send you a weblink with instructions within 5 days of enrolment.\n Trainee must authenticate their details in MYInfo.gov.sg within 5 days of receiving the weblink from IMDA',
					fontSize:10,margin:[0,0,0,10]},
					{text: 'The trainee must commence with the 1st examination of the endorsed certification between 17th June 2019 \u002D \n31st December 2020.',
					fontSize:10,margin:[0,0,0,10]},
					{text: 'The trainee must complete and pass all examinations required by the endorsed certification to achieve the\n final certification status within the Qualifying Period defined as twelve (12) months from the commencement\n date.',
					fontSize:10,margin:[0,0,0,10]},
					{text: 'The Applicant must ensure that the trainee is enrolled for CITREP+ by the Course Provider for the endorsed\n course/certification in the ICMS system before the commencement of the courses/examinations',
					fontSize:10,margin:[0,0,0,10]},
					{text: 'Please note that certification examination registered with a non-endorsed course provider will not be\n supported. Kindly refer to the list of endorsed course providers via online Library of Courses available on the\n ICMS (https://eservice.imda.gov.sg/icms) or email to citrep\u0040imda.gov.sg for assistance. ',
					fontSize:10,margin:[0,0,0,10]},
					{text: 'Full payment must be made by the Applicant to the endorsed Course Provider prior to submitting the claim\n application ',
					fontSize:10,margin:[0,0,0,10]}
				    
				  ],
				  styles : {
				         symbol: {
                         font: 'FontAwesome'
                       }
				    }
				};
			var today = new Date();
 			var date = today.getFullYear() + '' + (today.getMonth() + 1) + ''
 					+ today.getDate();
 			//var fileName = 'CITREP-' + date;
 			//pdfMake.createPdf(dd).download(fileName);
 			var pdfDocGenerator = pdfMake.createPdf(dd);
 			pdfDocGenerator.getBase64((data) => {
 				$('#<portlet:namespace />citrepPdf').val(data);
 				return data;
 			});
		} catch (err) {
 			alert(err.message);
 		}
	}
	
	function generateInvoicePdf() {
 		var taxAmount = "<%=CurrencyUtil.roundUpDollarAmount(String.valueOf(commerceOrder.getTaxAmount()))%>";
 		var netPrice = "<%=CurrencyUtil.roundUpDollarAmount(String.valueOf(commerceOrder.getCommerceOrderItems().get(0).getFinalPrice()))%>";
 		var amount = "<%=CurrencyUtil.roundUpDollarAmount(String.valueOf(commerceOrder.getTotal()))%>";
 		var courseTitle = "${productName}";
 		var sku = "<%=commerceOrder.getCommerceOrderItems().get(0).getSku()%>";
 		var quantity = "<%=commerceOrder.getCommerceOrderItems().get(0).getQuantity()%>";
 		var unitPrice =  "<%=CurrencyUtil.roundUpDollarAmount(String.valueOf(commerceOrder.getCommerceOrderItems().get(0).getUnitPrice()))%>";
 		var discount =  "<%=CurrencyUtil.roundUpDollarAmount(String.valueOf(commerceOrder.getCommerceOrderItems().get(0).getDiscountAmount()))%>";
 		var fullName = "${user.fullName}";
 		var companyName = "${companyName}";
 		var accType = "${accType}";
 		var displayName = accType == '2' ? companyName : fullName;
 		var paymentMethod = "Stripe";
 		var status = "Completed";
 		var invoiceNo = "<%=commerceOrder.getCommerceOrderId()%>";			
 		var purchaseDate = "<%=sdf.format(commerceOrder.getCreateDate())%>";
 		var taxRate = "<%=commerceTaxFixedRate%>";
 		var taxPercentage = taxRate * 0.01;
 		// address
 		var billingName = "<%=Validator.isNotNull(commerceOrder.getBillingAddress()) ? commerceOrder.getBillingAddress().getName() : ""%>";
 		var street1 = "<%=Validator.isNotNull(commerceOrder.getBillingAddress()) ? commerceOrder.getBillingAddress().getStreet1() : ""%>";
 		var street2 = "<%=Validator.isNotNull(commerceOrder.getBillingAddress()) ? commerceOrder.getBillingAddress().getStreet2() : ""%>";
 		var street3 = "<%=Validator.isNotNull(commerceOrder.getBillingAddress()) ? commerceOrder.getBillingAddress().getStreet3() : ""%>";
 		var address = street1 + street2 + street3;
 		var city = "<%=Validator.isNotNull(commerceOrder.getBillingAddress()) ? commerceOrder.getBillingAddress().getCity() : ""%>";
 		var zip = "<%=Validator.isNotNull(commerceOrder.getBillingAddress()) ? commerceOrder.getBillingAddress().getZip() : ""%>";
 		var country = "<%=Validator.isNotNull(commerceOrder.getBillingAddress()) ? commerceOrder.getBillingAddress().getCommerceCountry().getName(themeDisplay.getLocale()) : ""%>";
 		var phone = "<%=Validator.isNotNull(commerceOrder.getBillingAddress()) ? commerceOrder.getBillingAddress().getPhoneNumber() : ""%>";
 		
 		var rows = [];
 		rows.push([ 'Item', 'SKU', 'Qty', 'Unit Price', 'Discount', 'Net Price', 'GST', 'Total' ]);
		rows.push([ courseTitle, sku, quantity, unitPrice, discount, netPrice, taxAmount, amount ]);

 		try {
 			var dd = {
 				footer : {
 					columns : [ {
 						text : 'NTUC Learning Hub',
 						alignment : 'center',
 						style: 'boldHeader'
 					} ]
 				},
 				content : [
 						{
 							columns : [
 									{
 										// auto-sized columns have their widths based on their content
 										width : 120,
 										image : imgStr
 									// image: 'sampleImage.jpg'
 									},
 									{
										stack : [
												{text:'Tax Invoice/Receipt', style:"boldHeader"},
												{text:'GST Reg No.: 20-0409359-E ',style:"boldHeader", margin:[0,5,0,5]},
												'NTUC LEARNINGHUB PTE LTD',
												'73 BRAS BASAH ROAD',
												'#02-01 NTUC TRADE UNION HOUSE',
												'SINGAPORE 189556',
												'FAX 65 64867824\nwww.ntuclearninghub.com',
												'Company Registration Number: 200409359E' 
												],
										style: 'header',
										width : '*',
										margin: [70,0,0,0]
										
									} ],
							// optional space between columns
 							columnGap : 130
 						},
 						'\n\n',
 						{
 							columns : [
 									{
 										// auto-sized columns have their widths based on their content
 										//width : 150,
 										//text : 'Bill-To:\n' + '${fullName}' + '\n001 Bedok Reservoir Rd\n#01-1110\nSINGAPORE 470719\nREP. OF SINGAPORE\n+65124121255'
 										table : {
											widths : [ '*' ],
											body : [ [ {text: 'Bill-To:', style:'boldHeader'} ],
													[ billingName ],
													[ address ],
													[ city + ' ' + zip ],
													[ country ], [ phone ] ]
										},
										layout : 'noBorders',
										style:'content',
										width : '*'
 									},
 									{
 										// % width
 										//width : 300,
										//text : '\nInvoice No. : ' + invoiceNo + '\nInvoice Date : ' + purchaseDate + '\n Payment Method : ' + paymentMethod + '\nTerm : ' + status
 										table : {
											widths : [ '*', 'auto', 'auto' ],
											body : [
													[ 'Invoice No.', ':', invoiceNo],
													[ 'Invoice Date', ':', purchaseDate ],
													[ 'Payment Method', ':', paymentMethod ],
													[ 'Term', ':', status ] ]
										},
										layout : 'noBorders',
										style:'content',
										width: '*'
 									} ],
 							// optional space between columns
 							columnGap : 130
 						},
 						'\n\n',
						{text: 'Attn: ' + fullName, style:'content'},
						'\n',
 						{
 							style : 'content',
 							table : {
 								widths : [ '*', 'auto', 'auto', 'auto', 'auto','auto','auto','auto' ],
 								body : rows
 							}
 						},
 						{text : '\nFull name per ID (NRIC/FIN/Passport):',style:'content'},
 						{text : fullName,style:'content'},
 						'\n\n',
 						{
 							columns : [
 									{
 										// auto-sized columns have their widths based on their content
 										//width : 150,
										//text : '7% GST on SGD ' + amount + '\n(GST calculation is based on full course fee and services renderer)'
										table: {
 											widths : ['*'],
 											body : [
 												[Math.round(taxRate)+'% GST on SGD $'+ netPrice],
 												['(GST calculation is based on full course fee and services renderer)']
 											]
 										},
 										style:'content',
 										layout: 'noBorders'
 									},
 									{
 										// % width
 										//width : 220,
 										//text : '\nSubtotal SGD ' + amount + '\n\nGST (7%) SGD ' + roundTaxPrice + '\nAmount Payable SGD ' + totalPrice + ' ',
 										//style : 'rightalign',
 										table: {
 											widths : ['*','auto','auto'],
 											body : [
 												['Subtotal', ':', {text:'$ '+unitPrice , style: 'rightalign'}],
 												['Total Discount', ':', {text:'$ '+discount, style: 'rightalign'}],
 												['Net Price', ':', {text:'$ '+netPrice, style: 'rightalign'}],
 												['GST ('+Math.round(taxRate)+'%)', ':', {text:'$ '+taxAmount , style: 'rightalign'}],
 												['Amount Payable', ':', {text:'$ '+amount, style: 'rightalign'}],
 											]
 										},
 										style:'content',
 										layout: 'noBorders'
 									} ],
 							// optional space between columns
 							columnGap : 130
 						}, ],
 				styles : {
 					rightalign : {
						alignment : 'right'
					},
					content : {
						fontSize : 8
					},
					header : {
						fontSize: 8, color:'grey', alignment:'left'
					},
					boldHeader : {
						fontSize: 10, bold:true,
					}
 				}
 			};
 			var today = new Date();
 			var date = today.getFullYear() + '' + (today.getMonth() + 1) + ''
 					+ today.getDate();
 			//var fileName = 'invoice-' + date;
 			var pdfDocGenerator = pdfMake.createPdf(dd);
 			pdfDocGenerator.getBase64((data) => {
 				$('#<portlet:namespace />invoicePdf').val(data);
 				return data;
 			});
 		} catch (err) {
 			alert(err.message);
 		}
 	}
</script>
