<%@page import="web.ntuc.nlh.parameter.constants.MVCCommandNames"%>
<%@page import="svc.ntuc.nlh.parameter.model.ParameterGroup"%>
<%@ include file="../init.jsp"%>

<portlet:actionURL var="actionURL"
	name="<%=MVCCommandNames.UPDATE_GROUP_ACTION%>">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:actionURL>

<portlet:renderURL var="backURL">
</portlet:renderURL>

<section class="CX-main CX-unit-widget">
	<div class="container CX-wrapper">
		<div class="box-unit">
			<div class="box-back mb-4">
				<a href="${backURL}" class="btn btn-md btn-custom-default btn-round-30"><em
					class="fas fa-chevron-left"></em> <liferay-ui:message key="back" /></a>
			</div>
			<div class="box-dynamic-slider">
				<div class="box-dynamic-item">
					<div class="box-unit-inner">
						<div class="box-unit-header">
							<div class="box-unit-filter full">
								<label><liferay-ui:message key="parameter-group" /></label>
							</div>
						</div>
						<div class="box-unit-content">
							<div class="box-unit-content-inner" style="display: block;">
								<div class="box-input-content">
									<aui:form action="${actionURL}" method="post" name="fm"
										data-senna-off="true">
										<aui:model-context bean="${parameterGroup}"
											model="<%=ParameterGroup.class %>" />
										<aui:input name="parameterGroupId" type="hidden" />
										<div class="row">
											<div class="col-sm-12">
												<div class="box-input-inner">
													<div class="box-select">
														<aui:select name="parentId" label="parent">
															<aui:option value="">
																<liferay-ui:message key="please-select" />
															</aui:option>
															<c:forEach items="${groupParent}" var="gr">
																<aui:option value="${gr.parameterGroupId }"
																	label="${gr.groupName}"
																	selected="${gr.parameterGroupId==pg.parentId }" />
															</c:forEach>
														</aui:select>

													</div>
												</div>
											</div>
										</div>

										<div class="row">
											<div class="col-sm-12">
												<div class="box-input-inner">
													<aui:input name="groupCode" required="true">
														<aui:validator name="maxLength">75</aui:validator>
													</aui:input>
												</div>
											</div>
										</div>

										<div class="row">
											<div class="col-sm-12">
												<div class="box-input-inner">
													<aui:input name="groupName" required="true">
														<aui:validator name="maxLength">75</aui:validator>
													</aui:input>
												</div>
											</div>
										</div>

										<div class="row">
											<div class="col-sm-12">
												<div class="box-input-inner">
													<aui:input cssClass="field input-large" name="description"
														type="textarea" row="5">
														<aui:validator name="maxLength">1000</aui:validator>
													</aui:input>
												</div>
											</div>
										</div>
										<div class="row">
											<div class="col-sm-12">
												<div class="box-act">
													<aui:button type="submit" value="save"
														cssClass="btn btn btn-lg btn-custom" />
													<a href="${backURL}" class="btn btn-lg btn-custom-default">
														<liferay-ui:message key="cancel" />
													</a>

												</div>
											</div>
										</div>
									</aui:form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>