<%@page import="java.util.HashSet"%>
<%@page import="com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil"%>
<%@page import="com.liferay.asset.entry.rel.service.AssetEntryAssetCategoryRelLocalServiceUtil"%>
<%@page import="com.liferay.asset.entry.rel.model.AssetEntryAssetCategoryRel"%>
<%@page import="com.liferay.portal.kernel.theme.ThemeDisplay"%>
<%@page import="javax.portlet.ActionRequest"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.liferay.commerce.service.CommerceOrderLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.service.ServiceContextFactory"%>
<%@page import="com.liferay.portal.kernel.service.ServiceContext"%>
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
<%@page import="com.liferay.commerce.product.model.CPInstance"%>
<%@page import="com.liferay.petra.string.StringPool"%>
<%@page import="java.util.StringJoiner"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="com.liferay.commerce.product.model.CPDefinition"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.commerce.product.type.virtual.constants.VirtualCPTypeConstants"%>
<%@ include file="/init.jsp"%>
<liferay-ui:error key="email-fail" message="email-fail" />
<liferay-ui:error key="dob-fail" message="dob-fail" />
<liferay-ui:error key="dob-null" message="dob-null" />
<liferay-ui:error key="examdatevalidation-fail" message="examdatevalidation-fail" />
<%
	CandidateInfoStepDisplayContext candidateInfoStepDisplayContext = (CandidateInfoStepDisplayContext) request
			.getAttribute(CandidateInfoStepPortletKeys.COMMERCE_CHECKOUT_CUSTOM_CANDIDATE_INFO_DISPLAY_CONTEXT);
	CommerceOrder commerceOrder = candidateInfoStepDisplayContext.getCommerceOrder();
	List<CommerceOrderItem> commerceOrderItems = commerceOrder.getCommerceOrderItems();
	CommerceOrderItem commerceOrderItem = commerceOrderItems.get(0);
	
	ArrayList<String> times = (ArrayList<String>) request.getAttribute("times");
	String time1Str = (String) request.getAttribute("newTime1");
	String time2Str = (String) request.getAttribute("newTime2");
	String time3Str = (String) request.getAttribute("newTime3");
	
	List<String> listTimesDefault = candidateInfoStepDisplayContext
			.generateTimeRange(CandidateInfoStepPortletKeys.START_TIME, CandidateInfoStepPortletKeys.END_TIME);
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.UK);
	double commerceTaxFixedRate = CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).size()>0?
			CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).get(0).getRate():0;
	ArrayList<String> productTypeList = (ArrayList<String>) request.getAttribute("productTypeList");
	String examItemName = (String) request.getAttribute("examItemName");
// 	String subStringExamItemName = examItemName.substring(119,148);
	String examItemNameLocale = (String) request.getAttribute("examItemNameLocale");
	
    CPDefinition cpDefinitionForField = commerceOrderItem.getCPDefinition();
    boolean productCitrepForField = (Boolean) cpDefinitionForField.getExpandoBridge().getAttribute("CITREP");
    AssetEntry assetEntryForField = AssetEntryLocalServiceUtil.fetchEntry(CPDefinition.class.getName(),
            cpDefinitionForField.getCPDefinitionId());
    List<AssetCategory> categories = assetEntryForField.getCategories();
	Map<String, AssetCategory> categoryMap = candidateInfoStepDisplayContext.getSingleCategory(categories);
	Set<String> listField = new HashSet<String>();
	
	for(CommerceOrderItem item : commerceOrder.getCommerceOrderItems()){
		CPDefinition commerceDefinition = item.getCPDefinition();
		AssetEntry assetEntry = AssetEntryLocalServiceUtil.fetchEntry(CPDefinition.class.getName(), commerceDefinition.getCPDefinitionId());
		if(commerceDefinition.getProductTypeName().equals(VirtualCPTypeConstants.NAME) == true){
			categories = assetEntry.getCategories();
			Map<String, AssetCategory> categoryMapVirtual = candidateInfoStepDisplayContext.getSingleCategory(categories);
			listField = candidateInfoStepDisplayContext.getListField(commerceOrder, categoryMapVirtual);
			System.out.println("categoryMapVirtual = "+categoryMapVirtual);
		}
	}
	
	System.out.println("categoryMap = "+categoryMap);
	System.out.println("listField = "+listField);
	
%>

<portlet:resourceURL id="/commerce_cadidate_info/apply_citrep_pdf" var="CitrepPdfResourceURL" >
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>

<br>
<br>
<br>
<div class="section-step-two">
	<div class="container">
		<div class="row">
			<div class="col-md-12 col-xl-8">
				<div class="box-candidate">
					<div class="title">CANDIDATE INFORMATION</div>
					<div class="decs">Please check if all information in the
						fields are correct. All fileds are mandatory.</div>
					<div class="abstrak">Personal Detail:</div>
					<div class="notice-alert alert-blue">
						<img alt="" src="/o/web.ntuc.eshop.register/img/Info_Icon.png" />
						<div>Please ensure that the information filled here is for
							the individual sitting for the exam.</div>
					</div>
					<div class="box-field">
						<div class="box-input">
                            <div class="box-input">
							<label class="control-label" for="<portlet:namespace />full_name">Full Name (NRIC/Passport) <span class="red">*</span>
							</label>
							<c:if test="${empty candidateFullName}">
								<aui:input cssClass="clearable cs-form" name="full_name" label="" type="text" value="${user.fullName}">
									<aui:validator name="required" />
								</aui:input>
							</c:if>
							<c:if test="${not empty candidateFullName}">
								<aui:input cssClass="clearable cs-form" name="full_name" label="" type="text" value="${candidateFullName}">
									<aui:validator name="required" />
								</aui:input>
							</c:if>
						</div>
                        </div>
                        
						<div class="box-input">	
							<div class="box-input">
							<label for="<portlet:namespace />dob" class="control-label">Date of Birth <span
								class="red"
							>*</span></label>
							<c:if test="${empty candidateEmailAddress}">
								<aui:input cssClass="clearable cs-form date" name="dob" label="" type="date" value="${dob}">
									<aui:validator name="required" />
									<aui:validator name="custom" errorMessage="Date must not bigger than today">
										function (val, fieldNode, ruleValue) {
												console.log('val = '+val);
											   var flag = false;
											   var date = new Date(val);
											   var today = new Date();
											   flag = date < today;
											   return flag;
										 }
								   </aui:validator>
								</aui:input>
							</c:if>
							<c:if test="${not empty candidateEmailAddress}">
								<aui:input cssClass="clearable cs-form date" name="dob" label="" type="date" value="${dobNew}">
									<aui:validator name="required" />
									<aui:validator name="custom" errorMessage="Date must not bigger than today">
										 function (val, fieldNode, ruleValue) {
										 		console.log('val = '+val);
												var flag = false;
												var date = new Date(val);
												var today = new Date();
												flag = date < today;
												return flag;
										  }
									</aui:validator>
								</aui:input>
							</c:if> 
						</div>
						</div>
						
						<div class="box-input">
                            <div class="form-group">
                                <label for="email">Email Address<span class="red">*</span></label>
								<c:if test="${empty candidateEmailAddress}">
									<aui:input cssClass="clearable cs-form"
										name="email_address" label="" type="email" value="${user.emailAddress}">
										<aui:validator name="email" errorMessage="Allows only an email address"/>
									</aui:input>
								</c:if>
								<c:if test="${not empty candidateEmailAddress}">
									<aui:input cssClass="clearable cs-form"
										name="email_address" label="" type="email" value="${candidateEmailAddress}">
										<aui:validator name="email" errorMessage="Allows only an email address"/>
									</aui:input>
								</c:if>
                            </div>
                        </div>
						
						<div class="box-input">
							<div class="form-group">
								<c:if test="<%=commerceOrder.isGuestOrder()%>">
									<label class="control-label" for="<portlet:namespace/>email">Email
										<span style="color: red;">*</span>
									</label>
									<aui:input name="email" type="text" cssClass="clearable cs-form " wrapperCssClass="form-group-item">
										<aui:validator name="email" />
										<aui:validator name="required" />
									</aui:input>
								</c:if>
							</div>
						</div>
						
						<div class="box-input">
							<div class="form-group">							
                                <c:if
									test="<%=listField.contains(CandidateInfoStepPortletKeys.ACCA_REG_NO)%>"
								>
									<div class="box-input">
										<label for="accaRegistrationNo">ACCA
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
										<label for="msId">Microsoft ID <span
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
										<label for="pmiId">PMI ID <span
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
										<label for="pmiMembership">PMI
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
                                
                                
							</div>
						</div>
					</div>
					
					
					
					
					
					<div class="abstrak">Please proceed to select the date and
						time for your exam.</div>
					<div class="notice-alert alert-red">
						<img alt="" src="/o/web.ntuc.eshop.register/img/Info_Icon.png" />
						<div>
							Selected dates/times are subject to availability. A confirmation
							email will be sent once booked successfully. You should receive
							it within the <b>next 2-3 working days.</b>
						</div>
					</div>
					<div class="select-time">Select your 3 preferred date and
						time for exam below: </div>
					
					<div class="box-date-exams">
						<table>
							<%
								for (CommerceOrderItem item : commerceOrder.getCommerceOrderItems()) {
									CPDefinition cpDefinition = item.getCPDefinition();
									boolean productCitrep = (Boolean) cpDefinition.getExpandoBridge().getAttribute("CITREP");
									AssetEntry assetEntry = AssetEntryLocalServiceUtil.fetchEntry(CPDefinition.class.getName(),
											cpDefinition.getCPDefinitionId());
									if(cpDefinition.getProductTypeName().equals(VirtualCPTypeConstants.NAME) == true ){
							%>
						
							<%
								CPInstance cpInstance = item.fetchCPInstance();
		
								long cpDefinitionId = 0;
		
								String thumbnailSrc = StringPool.BLANK;
		
								StringJoiner stringJoiner = new StringJoiner(StringPool.COMMA);
								
								if (cpInstance != null) {
									cpDefinitionId = cpDefinition.getCPDefinitionId();
									thumbnailSrc = candidateInfoStepDisplayContext.getCommerceOrderItemThumbnailSrc(item); 
								}
							%> 
							<tr>
								<td><div class="image-course">
									<img alt="" class="image-course" src="<%=thumbnailSrc%>" style="width:60;height:40;"/>
								</div> </td>
								
								<td><div class="box-name">
		                            <div class="desc-exams"><%=HtmlUtil.escape(item.getName("en-GB"))%></div>
		                            <%
									long entryId = assetEntry.getEntryId();
									List<AssetEntryAssetCategoryRel> aeacRelLocalService = AssetEntryAssetCategoryRelLocalServiceUtil.getAssetEntryAssetCategoryRelsByAssetEntryId(entryId);
									String categoryName = "";
									try {
										long assetCategoryId = aeacRelLocalService.get(0).getAssetCategoryId();
										AssetEntryAssetCategoryRel assetEntryAssetEntryCategoryRel = AssetEntryAssetCategoryRelLocalServiceUtil.fetchAssetEntryAssetCategoryRel(entryId, assetCategoryId);
										long categoryId = assetEntryAssetEntryCategoryRel.getAssetCategoryId();
										AssetCategory assetCategory = AssetCategoryLocalServiceUtil.fetchAssetCategory(categoryId);
										categoryName = assetCategory.getName();
								    } catch (Exception e) {
								        
								    }
									%>
									<div class="category-general"><%=categoryName%></div>
		                        </div> </td>
							</tr>
	                        <%
									}
								}
							%>
						</table>
					</div>				
					
					<!--Selcetion exam one-->
					<div class="section-exams">
						<div class="section-title">Selection 1 :</div>
						<div class="toggle-select" data-id="#one" id="toggle-select-one">Select Date &amp; Time</div>
						<div class="box-date-select active" id="one">
							<div class="box-input">
								<div class="form-group">
									<label for="date">Date<span class="red">*</span>
									</label> 
									<aui:input name="date1" label="" showRequiredLabel="" id="date-input-one"
										cssClass="cs-form date" type="date"
										value="${date1Str}" placeholder="DD/MM/YYYY" required="">
										<aui:validator name="required" />
										<aui:validator name="custom"
											errorMessage="Please choose a future time and date."
										>
										 function (val, fieldNode, ruleValue) {
										 		var form = Liferay.Form.get('<portlet:namespace />fm');
												var flag = false;
												var date = new Date(val);
												
												var today = new Date();
												today.setDate(today.getDate() + 1);
												
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
							</div>
							<div class="box-input">
								<div class="form-group">
									<label>Time <span class="red">*</span>
									</label> 
									
									<aui:select id="time1" name="time1" cssClass="cs-form-option" label="">
										<%
											for (String time : listTimesDefault) {
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
							<div class="btn" id="submit-one">submit</div>
						</div>
						<div class="box-value d-none" id="box-value-one">
							<span class="val-target" id="val-target-one"></span> <span
								class="koma">, </span> <span class="val-target-time"
								id="val-target-time-one"></span> <span class="edit-mode d-none"
								id="edit-one">edit <i class="fas fa-edit"></i>
							</span>
						</div>
						<script>
                $('#edit-one').on('click', function() {
                  $('#toggle-select-one').removeClass('d-flex');
                  $('#toggle-select-one').removeClass('d-none');
                  $('#one').removeClass('d-none');
                  $('#one').addClass('active');
                  $('#box-value-one').addClass('d-none');
                });
                $('#submit-one').on('click', function() {
                  const month = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
                  var value = document.getElementById("<portlet:namespace/>date-input-one").value;
                  var date = new Date(value);
                  var day = date.getDate();
                  var namemonth = month[date.getMonth()];
                  var year = date.getFullYear();
                  var val = document.getElementById("<portlet:namespace/>time1").value;
                  $('#val-target-one').text(day + " " + namemonth + " " + year);
                  $('#val-target-time-one').text(val);
                  if (date == "Invalid Date") {
                    $('#one').addClass('active');
                    $('#box-value-one').addClass('d-none');
                    alert("please input date")
                  } else {
                    $('#one').addClass('d-none');
                    $('#one').removeClass('d-flex');
                    $('#edit-one').removeClass('d-none');
                    $('#edit-one').addClass('d-block');
                    $('#toggle-select-one').addClass('d-none');
                    $('#box-value-one').removeClass('d-none');
                  }
                });
              </script>
					</div>
					<!--Selcetion exam one end-->
					<!--Selcetion exam two-->
					<div class="section-exams">
						<div class="section-title">Selection 2 :</div>
						<div class="toggle-select" data-id="#two" id="toggle-select-two">Select
							Date &amp; Time</div>
						<div class="box-date-select" id="two">
							<div class="box-input">
								<div class="form-group">
									<label for="date">Date <span class="red">*</span>
									</label> 
									<aui:input name="date2" label="" showRequiredLabel="" id="date-input-two"
										cssClass="cs-form date" type="date"
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
												today.setDate(today.getDate() + 1);
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
							</div>
							<div class="box-input">
								<div class="form-group">
									<label for="date">Time <span class="red">*</span>
									</label> 
									
									
									<aui:select id="time2" name="time2" cssClass="cs-form-option" label="">
										<%
											for (String time : listTimesDefault) {
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
							<div class="btn" id="submit-two">submit</div>
						</div>
						<div class="box-value d-none" id="box-value-two">
							<span class="val-target" id="val-target-two"></span> <span
								class="koma">, </span> <span class="val-target-time"
								id="val-target-time-two"></span> <span class="edit-mode d-none"
								id="edit-two">edit <i class="fas fa-edit"></i>
							</span>
						</div>
						<script>
                $('#edit-two').on('click', function() {
                  $('#toggle-select-two').removeClass('d-flex');
                  $('#toggle-select-two').removeClass('d-none');
                  $('#two').removeClass('d-none');
                  $('#two').addClass('active');
                  $('#box-value-two').addClass('d-none');
                });
                $('#submit-two').on('click', function() {
                  const month = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
                  var value = document.getElementById("<portlet:namespace/>date-input-two").value;
                  var date = new Date(value);
                  var day = date.getDate();
                  var namemonth = month[date.getMonth()];
                  var year = date.getFullYear();
                  var val = document.getElementById("<portlet:namespace/>time2").value;
                  $('#val-target-two').text(day + " " + namemonth + " " + year);
                  $('#val-target-time-two').text(val);
                  if (date == "Invalid Date") {
                    $('#two').addClass('active');
                    $('#box-value-two').addClass('d-none');
                    alert("please input date")
                  } else {
                    $('#two').addClass('d-none');
                    $('#two').removeClass('d-flex');
                    $('#edit-two').removeClass('d-none');
                    $('#edit-two').addClass('d-block');
                    $('#toggle-select-two').addClass('d-none');
                    $('#box-value-two').removeClass('d-none');
                  }
                });
              </script>
					</div>
					<!--Selcetion exam two end-->
					<!--Selcetion exam three-->
					<div class="section-exams">
						<div class="section-title">Selection 3 :</div>
						<div class="toggle-select" data-id="#three"
							id="toggle-select-three">Select Date &amp; Time</div>
						<div class="box-date-select" id="three">
							<div class="box-input">
								<div class="form-group">
									<label for="date">Date <span class="red">*</span>
									</label> 
									<aui:input name="date3" label="" showRequiredLabel="" id="date-input-three"
										cssClass="cs-form date" type="date"
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
												today.setDate(today.getDate() + 1);
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
							</div>
							<div class="box-input">
								<div class="form-group">
									<label for="date">Time <span class="red">*</span>
									</label> 
									<aui:select id="time3" name="time3" cssClass="cs-form-option" label="">										
										<%
											for (String time : listTimesDefault) {
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
							<div class="btn" id="submit-three">submit</div>
						</div>
						<div class="box-value d-none" id="box-value-three">
							<span class="val-target" id="val-target-three"></span> <span
								class="koma">, </span> <span class="val-target-time"
								id="val-target-time-three"></span> <span
								class="edit-mode d-none" id="edit-three">edit <i
								class="fas fa-edit"></i>
							</span>
						</div>
						<script>
                $('#edit-three').on('click', function() {
                  $('#toggle-select-three').removeClass('d-flex');
                  $('#toggle-select-three').removeClass('d-none');
                  $('#three').removeClass('d-none');
                  $('#three').addClass('active');
                  $('#box-value-three').addClass('d-none');
                });
                $('#submit-three').on('click', function() {
                  const month = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
                  var value = document.getElementById("<portlet:namespace/>date-input-three").value;                
                  var date = new Date(value);
                  var day = date.getDate();
                  var namemonth = month[date.getMonth()];
                  var year = date.getFullYear();                 
                  var val = document.getElementById("<portlet:namespace/>time3").value;
                  $('#val-target-three').text(day + " " + namemonth + " " + year);
                  $('#val-target-time-three').text(val);
                  if (date == "Invalid Date") {
                    $('#three').addClass('active');
                    $('#box-value-three').addClass('d-none');
                    alert("please input date")
                  } else {
                    $('#three').addClass('d-none');
                    $('#three').removeClass('d-flex');
                    $('#edit-three').removeClass('d-none');
                    $('#edit-three').addClass('d-block');
                    $('#toggle-select-three').addClass('d-none');
                    $('#box-value-three').removeClass('d-none');
                  }
                });
              </script>
					</div>
					<!--Selcetion exam three end-->
					<div class="box-additional">
						<div class="additional-notes">Additional Notes</div>
						<aui:input label="" name="additionalNotes" id="additionalNotes" class="additional-box"
							type="textarea" value="${addNotes}"
						/> 
					</div>
					
					
					<%
						boolean productCitrepFlag2 = false;
						for (CommerceOrderItem item : commerceOrder.getCommerceOrderItems()) {
							CPDefinition cpDefinition = item.getCPDefinition();
							productCitrepFlag2 = (Boolean) cpDefinition.getExpandoBridge().getAttribute("CITREP");							
							
							if(productCitrepFlag2){
								break;
							}
						}
						//System.out.println("productCitrepFlag2 : "+ productCitrepFlag2);
							
					%>
					
					<c:if test="<%=productCitrepFlag2==true%>">						
						<div class="citred">
							<img alt="" src="/o/web.ntuc.eshop.register/img/Info_Icon.png" />
							If you would like to get funding for this, please apply for <a class="citredshowhide" href="javascript:void(0)">CITREP+funding</a> here. 													
							<aui:input type="hidden" id="citrepNewFlag" name="citrepNewFlag" value="false" />
							
							<aui:script>
							    $(".citredshowhide").click(function(){
							    	$(".box-billing-address").toggle();
							    	var val = $(".box-billing-address").is(':visible');
	                              	if(val===true){
	                              		AUI().use('node', function(A){
										   A.one('#<portlet:namespace/>citrepName').set('required', true); 
										   A.one('#<portlet:namespace/>NRIC').set('required', true); 
										   A.one('#<portlet:namespace/>citrepEmail').set('required', true); 
										   A.one('#<portlet:namespace/>citrepMobileNumber').set('required', true); 
										   A.one('#<portlet:namespace/>nationalityDetail').set('required', true);
										   A.one('#<portlet:namespace/>pmiId').set('required', true);
										   A.one('#<portlet:namespace/>citrepDeclaration1').set('required', true);
										   A.one('#<portlet:namespace/>citrepDeclaration2').set('required', true);
										   A.one('#<portlet:namespace/>citrepDeclaration3').set('required', true);
										   A.one('#<portlet:namespace/>citrepDeclaration4').set('required', true);
										   A.one('#<portlet:namespace/>citrepNationality1').set('required', true);
										   A.one('#<portlet:namespace/>citrepNationality2').set('required', true);
										   A.one('#<portlet:namespace/>citrepCompanySponsored1').set('required', true);
										   A.one('#<portlet:namespace/>citrepCompanySponsored2').set('required', true);
										});
	                              		
	                              		document.getElementById("<portlet:namespace />citrepNewFlag").value = "true";
	                              	}
	                              	else{
	                              		AUI().use('node', function(A){
										   A.one('#<portlet:namespace/>citrepName').set('required', false); 
										   A.one('#<portlet:namespace/>NRIC').set('required', false); 
										   A.one('#<portlet:namespace/>citrepEmail').set('required', false); 
										   A.one('#<portlet:namespace/>citrepMobileNumber').set('required', false); 
										   A.one('#<portlet:namespace/>nationalityDetail').set('required', false); 
										   A.one('#<portlet:namespace/>pmiId').set('required', false);
										   A.one('#<portlet:namespace/>citrepDeclaration1').set('required', false);
										   A.one('#<portlet:namespace/>citrepDeclaration2').set('required', false);
										   A.one('#<portlet:namespace/>citrepDeclaration3').set('required', false);
										   A.one('#<portlet:namespace/>citrepDeclaration4').set('required', false);
										   A.one('#<portlet:namespace/>citrepNationality1').set('required', false);
										   A.one('#<portlet:namespace/>citrepNationality2').set('required', false);
										   A.one('#<portlet:namespace/>citrepCompanySponsored1').set('required', false);
										   A.one('#<portlet:namespace/>citrepCompanySponsored2').set('required', false);
										});
	                              		document.getElementById("<portlet:namespace />citrepNewFlag").value = "false";
	                              	}
							    });
							</aui:script>
						</div>
						
						<div class="box-billing-address" style="display: none">
                           <section class="section-citred">
                               <div class="container">
                                   <div class="row justify-content-center">
                                       <div class="col-md-12">
                                           <div class="box-citrep">
                                               <div class="row">
                                                   <div class="col-12">
                                                       <div class="title">CITREP TRAINEE DETAIL FORM</div>

                                                       <div class="desc">For Singapore citizens and PRs Only</div>
                                                   </div>
                                               </div>

                                               <div class="row">
                                                   <div class="col-12">
                                                       <div class="box-input">
                                                           <div class="form-group">
                                                           		   <label for="name ">Name (As per NRIC)<span class="red">*</span></label>
                                                                   <aui:input cssClass="clearable cs-form " id="citrepName" name="citrepFullNameNric" label="" 
                                                                   		type="text" value="${citrepFullNameNric}">
                                                                   </aui:input>
                                                           </div>
                                                       </div>
                                                   </div>
                                               </div>

                                               <div class="row">
                                                   <div class="col-12">
                                                       <div class="box-input">
                                                           <div class="form-group">
                                                           			<label for="NRIC ">NRIC Number<span lass="red">*</span></label>
                                                                   <aui:input cssClass="clearable cs-form " id="NRIC" name="nricNumber" label="" 
                                                               		  type="text" value="${citrepNric}">
	                                                               </aui:input>
                                                           </div>
                                                       </div>
                                                   </div>
                                               </div>

                                               <div class="row">
                                                   <div class="col-12 col-md-3">
                                                       <div class="row check-citred">
                                                           <div class="col-12">
                                                               <div class="box-input">Colour of NRIC <span class="red">*</span></div>
                                                           </div>                                                           
                                                            <c:set var="nationality1" value="false" />
															<c:forEach var="item" items="${citrepNationality}">
																<c:if test="${item eq 'Singapore Citizen (Pink NRIC)'}">
																	<c:set var="nationality1" value="true" />
																</c:if>
															</c:forEach>
															<c:set var="nationality2" value="false" />
															<c:forEach var="item" items="${citrepNationality}">
																<c:if test="${item eq 'Singapore Permanent Resident (Blue NRIC)'}">
																	<c:set var="nationality2" value="true" />
																</c:if>
															</c:forEach>
															<div class="col-6">
																<label class="form-check-label position-relative">
																	<aui:input cssClass="clearable cs-form"
																		name="citrepNationality" id="citrepNationality1" label=""
																		type="radio" value="Singapore Citizen (Pink NRIC)"
																		checked="${nationality1}"
																		onClick="yesnoCheck('citrepNationality2','nationality-detail','citrepNationalityDetail')">
																		<aui:validator name="required">
																		  function() {
																			   return AUI.$('#<portlet:namespace />citrep').prop('checked');
																		  }
																		</aui:validator>
																	</aui:input>Pink
																</label>
															</div>
															
															<div class="col-6">
																<label class="form-check-label position-relative">
																	<aui:input cssClass="clearable cs-form"
																		name="citrepNationality" id="citrepNationality2" label=""
																		type="radio" value="Singapore Permanent Resident (Blue NRIC)"
																		checked="${nationality2}"
																		onClick="yesnoCheck('citrepNationality2','nationality-detail','citrepNationalityDetail')">
																		<aui:validator name="required">
																		  function() {
																			   return AUI.$('#<portlet:namespace />citrep').prop('checked');
																		  }
																		</aui:validator>
																	</aui:input>Blue
																</label></br>
															</div>
															
                                                       </div>
                                                   </div>

                                                   <div class="col-9">
                                                       <div class="box-input">
                                                           <div class="form-group">
                                                           		<label for="Nationality">For PR, please state Nationality:<span class="red">*</span></label>
                                                           		<aui:input cssClass="clearable cs-form " id="nationalityDetail" name="nationalityDetail" label="" type="text" value="${citrepNationalityDetail}">
                                                                </aui:input>
                                                           	</div>
                                                       </div>
                                                   </div>
                                               </div>

                                               <div class="row">
                                                   <div class="col-12">
                                                       <div class="box-input">
                                                           <div class="form-group">
                                                           		   <label for="email">Email Address<span class="red">*</span></label>
                                                                   <aui:input cssClass="clearable cs-form " id="citrepEmail" label="" name="citrepEmail" type="text" value="${citrepEmail}">
                                                                   </aui:input>
                                                           </div>
                                                       </div>
                                                   </div>
                                               </div>

                                               <div class="row">
                                                   <div class="col-12">
                                                       <div class="box-input">
                                                           <div class="form-group">
                                                           		<label for="email">Contact Number<span class="red">*</span></label>
                                                                <aui:input cssClass="clearable cs-form " id="citrepMobileNumber" name="citrepMobileNumber" label="" type="text" value="${citrepMobileNumber}">
                                                                </aui:input>
                                                           </div>
                                                       </div>
                                                   </div>
                                               </div>

                                               <div class="row">
                                                   <div class="col-12">
                                                       <div class="row check-citred">
                                                           <div class="col-12">
                                                               <div class="box-input">Company Sponsored<span
                                                                       class="red">*</span></div>
                                                           </div>
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
															
															<div class="col-12 col-md-3">
																<div class="row">
																	<div class="col-6">
																		<label class="form-check-label position-relative"> 
																			<aui:input cssClass="clearable cs-form"
																				name="citrepCompanySponsored" id="citrepCompanySponsored2"
																				label="" type="radio" value="Yes"
																				checked="${ citrepCompanySponsored2}"
																				onClick="yesnoCheck('citrepCompanySponsored2','company-name','citrepCompanyName')">
																				<aui:validator name="required">
																		          function() {
																		               return AUI.$('#<portlet:namespace />citrep').prop('checked');
																		          }
																				</aui:validator>
																			</aui:input>Yes
																		</label>
																	</div>
																	
																	<div class="col-6">
																		<label class="form-check-label position-relative"> 
																			<aui:input cssClass="clearable cs-form"
																				name="citrepCompanySponsored" id="citrepCompanySponsored1" label="" type="radio"
																				value="No" checked="${ citrepCompanySponsored1}"
																				onClick="yesnoCheck('citrepCompanySponsored2','company-name','citrepCompanyName')">
																				<aui:validator name="required">
																			          function() {
																			               return AUI.$('#<portlet:namespace />citrep').prop('checked');
																			          }
																				</aui:validator>
																			</aui:input>No
																		</label>
																	</div>
																</div>
															</div>
                                                       </div>
                                                   </div>
                                               </div><br />

                                               <div class="row">
                                                   <div class="col-12">
                                                       <div class="box-input">
                                                           <div class="form-group">
                                                           		<label for="omi">PMI ID<span class="red">*</span></label> 
                                                           		<!-- <input class="cs-form" id="pmi" type="text" /> -->
                                                           		<aui:input cssClass="clearable cs-form " id="pmiId" name="pmiId" label="" type="text" value="${pmiId}">
                                                           		</aui:input>
                                                           </div>
                                                       </div>
                                                   </div>
                                               </div>

                                               <div class="row box-text-check">
                                                   <div class="col-12">
                                                       <div class="title-underline">Exam Title (Please tick)</div>
                                                   </div>
                                                   
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
													<c:set var="declaration4" value="false" />
													<c:forEach var="item" items="${citrepDeclaration}">
														<c:if test="${item eq 'Citrep Declaration'}">
															<c:set var="declaration4" value="true" />
														</c:if>
													</c:forEach>
                                                   
                                                   <div class="box-input form-check">
														<label class="form-check-label" for="<portlet:namespace />citrepDeclaration4"> <aui:input
																id="citrepDeclaration4" name="citrepDeclaration4"
																type="checkbox" checked="${ declaration4}"
																value="Citrep Declaration" label=""
																cssClass="form-check-input" wrapperCssClass="inline-remover"
															>
																<aui:validator name="required">
															        function() {
															            return AUI.$('#<portlet:namespace />citrep').prop('checked');
															        }
															    </aui:validator>
															</aui:input> 
															<%=examItemName%>
															<aui:input label="" name="examItemNameLocale"
																id="examItemNameLocale" type="hidden"
																value="<%=examItemNameLocale%>"
															/>
														</label>
													</div>
													
                                                   <div class="col-12">
                                                       <div class="title-underline">Declaration :</div>
                                                   </div>                                                                                                      
													
													<div class="box-input form-check">
														<label class="form-check-label" for="<portlet:namespace />citrepDeclaration1"> 
															<aui:input id="citrepDeclaration1" name="citrepDeclaration1"
																type="checkbox" checked="${ declaration1}"
																value="citrep-declaration-1" label=""
																cssClass="form-check-input" wrapperCssClass="inline-remover">
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
														<label class="form-check-label" for="<portlet:namespace />citrepDeclaration2"> <aui:input
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
														<label class="form-check-label" for="<portlet:namespace />citrepDeclaration3"> <aui:input
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

                                                   <div class="col-12">
                                                       <div class="title-underline">Claim Conditions :</div>
                                                   </div>

                                                   <div class="col-12">
                                                       <div class="box-claims">
                                                           <p>For Certification Fees Support:<br />
                                                               <br />
                                                               During registration, if you pain the exam fee, and
                                                               provide NTUC LearningHub with the CITREP+ details for
                                                               enrolment and fulfil the requirements for funding, we
                                                               will enrolled you for CITREP+ funding.<br />
                                                               <br />
                                                               IMDA will send you a weblink with instructions within 5
                                                               days of enrolment. Trainee must authenticate their
                                                               details in MYinfo.gov.sg within 5 days of receiving the
                                                               weblink from IMDA.<br />
                                                               <br />
                                                               The trainee must commence with the 1st examination
                                                               attempt if the endorsed certification<span
                                                                   id="dots">...</span><span id="more"> between <b>19
                                                                       May 2021 - 31 March 2022.</b><br />
                                                                   <br />
                                                                   The trainee must complete and pass all examinations
                                                                   required by the endorsed certification to achieve
                                                                   the final certification status within the Qualifying
                                                                   Period defined as twelve (12) months from the
                                                                   commencement date of the first exam attempt.<br />
                                                                   <br />
                                                                   The Applicant must ensure that the trainee is
                                                                   enrolled for CITREP+ by the Course Provider for the
                                                                   endorsed course/certification in the ICMS system
                                                                   before the commencement of the
                                                                   courses/examinations.<br />
                                                                   <br />
                                                                   Please note that certification examination
                                                                   registered with a non-endorsed Course Provider will
                                                                   not be supported. Kindly refer to the list of
                                                                   endorsed Course Providers via online Library of
                                                                   Courses available on the ICMS
                                                                   (https://eservice.imda.goc.sg/icms) or email to
                                                                   citrep@imda.gov.sg for assistance.<br />
                                                                   <br />
                                                                   Full payment must be made by the Applicant to the
                                                                   endorsed Course Provider prior to submitting the
                                                                   claim application</span><span id="myBtn"
                                                                   onclick="myFunction()">See More</span></p>
                                                       </div>
                                                   </div>
                                               </div>
                                           </div>
                                       </div>
                                   </div>
                               </div>
                           </section>
                           <script>
                               function myFunction() {
                                   var dots = document.getElementById("dots");
                                   var moreText = document.getElementById("more");
                                   var btnText = document.getElementById("myBtn");

                                   if (dots.style.display === "none") {
                                       dots.style.display = "inline";
                                       btnText.innerHTML = "See More";
                                       moreText.style.display = "none";
                                   } else {
                                       dots.style.display = "none";
                                       btnText.innerHTML = "See less";
                                       moreText.style.display = "inline";
                                   }
                               }
                           </script>
                       </div>
					</c:if>
					<div class="check-tnc">
						<div class="box-input form-check">
							<label class="form-check-label"> 
								<aui:input id="termAndCondition" name="termAndCondition"
									type="checkbox" label=""
									cssClass="form-check-input" wrapperCssClass="inline-remover">
									<aui:validator name="required" />
								</aui:input> I agree to the <a target="_blank"
								href="https://www.ntuclearninghub.com/ereg-terms-of-use">terms and conditions. </a> By checking this box, I consent and agree to submit <br>
								 information relating to another indivdual, I represent and warrant that the individuals also <br>
								 consents to NTUC LearningHub's terms and conditions. <span style="color: #E00000">*</span>
							</label>
						</div>
					</div>
					
					<hr>
						
				</div>
			</div>
			
			<div class="col-md-12 col-xl-4">
				<div class="box-stick-global">
					<div class="box-header">
						<liferay-portlet:runtime portletName="com_liferay_commerce_cart_content_web_internal_portlet_CommerceCartContentTotalPortlet"> </liferay-portlet:runtime>						
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<%
	int type = (int) request.getAttribute("accType");
	String authToken = AuthTokenUtil.getToken(request);
	String invoicePortletName = "NTUC_Invoice_ExamMerchandize_Portlet"; 
	String invoiceUrl = "/account/examinvoices";
	Layout layout2 = LayoutLocalServiceUtil.getFriendlyURLLayout(themeDisplay.getSiteGroupId(), false, invoiceUrl);
	long plid2 = layout2.getPlid();
	List<PortletPreferences> preferences = PortletPreferencesLocalServiceUtil.getPortletPreferencesByPlid(plid2);
	List<PortletPreferences> filteredPreferences = preferences.stream().filter(x->x.getPortletId().contains(invoicePortletName)).collect(Collectors.toList());
	
	PortletURL url = null;
	if(filteredPreferences.size()>0){
		String portletId2 = filteredPreferences.get(0).getPortletId();
		
		url = PortletURLFactoryUtil.create(request, portletId2, plid2 ,
			PortletRequest.RENDER_PHASE);
		
		url.setParameter("mvcRenderCommandName", "examMerchandizeOrderDetailRender");
		url.setParameter("accType", String.valueOf(type));
		url.setParameter("orderId", String.valueOf(commerceOrder.getCommerceOrderId()));
		url.setParameter("authToken", authToken);
		url.setWindowState(LiferayWindowState.MAXIMIZED);
		url.setPortletMode(LiferayPortletMode.VIEW);
	}
%> 
<aui:input label="" name="invoiceUrl"
	id="invoiceurl" type="hidden"
	value="<%=url %>"
/>

<aui:input label="DIPS" name="citrepPdf"
	id="citrepPdf" type="hidden"
	value="${citrepPdf}"
/>

<script>
$('#<portlet:namespace/>citrepName').change(function(){
	if($('#<portlet:namespace />citrepName').val()!="" &&
			$('#<portlet:namespace />NRIC').val()!="" &&
			$('#<portlet:namespace />citrepEmail').val()!="" &&
			$('#<portlet:namespace />citrepMobileNumber').val()!="" &&
			$('#<portlet:namespace />nationalityDetail').val()!="" &&
			$('#<portlet:namespace />pmiId').val()!="")
	{
		fillCitrepPdfValue();
	}
	else{
		
	}
});
$('#<portlet:namespace/>NRIC').change(function(){
	if($('#<portlet:namespace />citrepName').val()!="" &&
			$('#<portlet:namespace />NRIC').val()!="" &&
			$('#<portlet:namespace />citrepEmail').val()!="" &&
			$('#<portlet:namespace />citrepMobileNumber').val()!="" &&
			$('#<portlet:namespace />nationalityDetail').val()!="" &&
			$('#<portlet:namespace />pmiId').val()!="")
	{
		fillCitrepPdfValue();
	}
	else{
		
	}
});
$('#<portlet:namespace/>nationalityDetail').change(function(){
	if($('#<portlet:namespace />citrepName').val()!="" &&
			$('#<portlet:namespace />NRIC').val()!="" &&
			$('#<portlet:namespace />citrepEmail').val()!="" &&
			$('#<portlet:namespace />citrepMobileNumber').val()!="" &&
			$('#<portlet:namespace />nationalityDetail').val()!="" &&
			$('#<portlet:namespace />pmiId').val()!="")
	{
		fillCitrepPdfValue();
	}
	else{
		
	}
});
$('#<portlet:namespace/>citrepEmail').change(function(){
	if($('#<portlet:namespace />citrepName').val()!="" &&
			$('#<portlet:namespace />NRIC').val()!="" &&
			$('#<portlet:namespace />citrepEmail').val()!="" &&
			$('#<portlet:namespace />citrepMobileNumber').val()!="" &&
			$('#<portlet:namespace />nationalityDetail').val()!="" &&
			$('#<portlet:namespace />pmiId').val()!="")
	{
		fillCitrepPdfValue();
	}
	else{
		
	}
});
$('#<portlet:namespace/>citrepMobileNumber').change(function(){
	if($('#<portlet:namespace />citrepName').val()!="" &&
			$('#<portlet:namespace />NRIC').val()!="" &&
			$('#<portlet:namespace />citrepEmail').val()!="" &&
			$('#<portlet:namespace />citrepMobileNumber').val()!="" &&
			$('#<portlet:namespace />nationalityDetail').val()!="" &&
			$('#<portlet:namespace />pmiId').val()!="")
	{
		fillCitrepPdfValue();
	}
	else{
		
	}
});
$('#<portlet:namespace/>pmiId').change(function(){
	if($('#<portlet:namespace />citrepName').val()!="" &&
			$('#<portlet:namespace />NRIC').val()!="" &&
			$('#<portlet:namespace />citrepEmail').val()!="" &&
			$('#<portlet:namespace />citrepMobileNumber').val()!="" &&
			$('#<portlet:namespace />nationalityDetail').val()!="" &&
			$('#<portlet:namespace />pmiId').val()!="")
	{
		fillCitrepPdfValue();
	}
	else{
		
	}
});

function fillCitrepPdfValue(){
	var citrepFullNameNRIC = $('#<portlet:namespace />citrepName').val();
	var citrepDob = $('#<portlet:namespace />citrepDob').val();
	var citrepEmail = $('#<portlet:namespace />citrepEmail').val();
	var citrepMobileNumber = $('#<portlet:namespace />citrepMobileNumber').val();
	var citrepProfession = $('#<portlet:namespace />citrepProfession').val();
	var citrepStatus1 = $('#<portlet:namespace />citrepStatus1').is(':checked');
	var citrepStatus2 = $('#<portlet:namespace />citrepStatus2').is(':checked');
	var citrepNationality1 = $('#<portlet:namespace />citrepNationality1').is(':checked');
	var citrepNationality2 = $('#<portlet:namespace />citrepNationality2').is(':checked');
	var citrepNationalityDetail = $('#<portlet:namespace />nationalityDetail').val();
	var citrepCompanySponsored1 = $('#<portlet:namespace />citrepCompanySponsored1').is(':checked');
	var citrepCompanySponsored2 = $('#<portlet:namespace />citrepCompanySponsored2').is(':checked');
	var citrepExamDate1 = $('#<portlet:namespace />citrepExamDate1').is(':checked');
	var citrepExamDate2 = $('#<portlet:namespace />citrepExamDate2').is(':checked');
	var citrepExamDateDetail = $('#<portlet:namespace />citrepExamDateDetail').val();
	var citrepDeclaration1 = true;
	var citrepDeclaration2 = true;
	var citrepDeclaration3 = true;
	var citrepDeclaration4 = true;
	var citrepAdditionalNotes = $('#<portlet:namespace />citrepAdditionalNotes').val();
	var examItemNameLocale = $('#<portlet:namespace />examItemNameLocale').val();
	var preferredDate1 = $('#<portlet:namespace />date-input-one').val();
	var preferredDate2 = $('#<portlet:namespace />date-input-two').val();
	var preferredDate3 = $('#<portlet:namespace />date-input-three').val();
	var time1 = $('#<portlet:namespace />time1').val();
	var time2 = $('#<portlet:namespace />time2').val();
	var time3 = $('#<portlet:namespace />time3').val();

	generateCitrepPdf(citrepFullNameNRIC,citrepDob,citrepEmail,citrepMobileNumber,citrepProfession,
			citrepStatus1,citrepStatus2,citrepNationality1,citrepNationality2,citrepNationalityDetail,
			citrepCompanySponsored1,citrepCompanySponsored2,citrepExamDate1,citrepExamDate2,
			citrepExamDateDetail,citrepDeclaration1,citrepDeclaration2,citrepDeclaration3,
			citrepAdditionalNotes,examItemNameLocale,preferredDate1, preferredDate2, preferredDate3, time1, time2, time3);
}
</script>


<script type="text/javascript">
    var imgStr = "";
	$(document).ready(
			function() {
				
				$('#<portlet:namespace />citrepPdf').val(sessionStorage.getItem("citrepPdfValue"));
				
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
			});
	
	$('form[name="<portlet:namespace />fm"]').submit(function() {
		
		fillCitrepPdfValue();
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
			citrepAdditionalNotes, examItemNameLocale, preferredDate1, preferredDate2, preferredDate3, time1, time2, time3) {
		
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
									        text:'Colour of NRIC :', fontSize:9, 
									        border: [false, false, false, false],
									        margin: [-5,0,29,0]
									    },
										{
										    border: [false, false, false, false],
												table: {
													widths: [11],
													body: [
														[
														    {text: citrepNationality1 ? '\u221a' : ' ',
														        margin:[0,0,0,0], alignment: 'center'
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
													widths: [11],
													body: [
														[
														    {text:citrepNationality2 ? '\u221a' : ' ',
														        margin:[0,0,0,0], alignment: 'center'
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
													widths: [11],
													body: [
														[
														    {text:citrepCompanySponsored1 ? '\u221a' : ' ',
														        margin:[0,0,0,0], alignment: 'center'
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
													widths: [11],
													body: [
														[
														    {text:citrepCompanySponsored2 ? '\u221a' : ' ',
														        margin:[0,0,0,0], alignment: 'center'
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
									        text:'Exam Date :', fontSize:9, 
									        border: [false, false, false, false],
									        margin: [-5,0,0,0]
									    },
									    {
									    	text:preferredDate1+'  : ', fontSize:9, 
									        border: [false, false, false, false],
									        margin: [-5,0,0,0]
										},
										{
											text:time1, fontSize:9, 
									        border: [false, false, false, false],
									        margin: [-5,0,0,0]
										},
									],
								],
								widths:['18%','10%','18%']
							}
				    },
				    {
					   	table: {
									body: [
										[
										    {
										        text:'', fontSize:9, 
										        border: [false, false, false, false],
										        margin: [-5,0,0,0]
										    },
										    {
										    	text:preferredDate2+'  : ', fontSize:9, 
										        border: [false, false, false, false],
										        margin: [-5,0,0,0]
											},
											{
												text:time2, fontSize:9, 
										        border: [false, false, false, false],
										        margin: [-5,0,0,0]
											}
										],
									],
									widths:['18%','10%','18%']
								}
					    },
					    {
						   	table: {
										body: [
											[
											    {
											        text:'', fontSize:9, 
											        border: [false, false, false, false],
											        margin: [-5,0,0,0]
											    },
											    {
											    	text:preferredDate3+'  : ', fontSize:9, 
											        border: [false, false, false, false],
											        margin: [-5,0,0,0]
												},
												{
													text:time3, fontSize:9, 
											        border: [false, false, false, false],
											        margin: [-5,0,0,0]
												}
											],
										],
										widths:['18%','10%','18%']
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
												text: examItemNameLocale, fontSize:9,
												border: [false, false, false, false],
												margin: [15,0,0,0]
											},
										],
										
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
 			var pdfDocGenerator = pdfMake.createPdf(dd);
 			pdfDocGenerator.getBase64((data) => {
 				$('#<portlet:namespace />citrepPdf').val(data);
 				return data;
 			});
		} catch (err) {
 			alert(err.message);
 		}
	}
</script>
