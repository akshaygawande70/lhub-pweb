/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package svc.ntuc.nlh.course.admin.service.impl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.Validator;

import java.util.List;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.constants.CourseAdminConstant;
import api.ntuc.common.util.HttpApiUtil;
import svc.ntuc.nlh.course.admin.model.Course;
import svc.ntuc.nlh.course.admin.service.CourseLocalServiceUtil;
import svc.ntuc.nlh.course.admin.service.base.CourseServiceBaseImpl;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;

/**
 * The implementation of the course remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>svc.ntuc.nlh.course.admin.service.CourseService</code> interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see CourseServiceBaseImpl
 */
@Component(
	property = {
		"json.web.service.context.name=ntuc_course_admin",
		"json.web.service.context.path=Course"
	},
	service = AopService.class
)
public class CourseServiceImpl extends CourseServiceBaseImpl {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use <code>svc.ntuc.nlh.course.admin.service.CourseServiceUtil</code> to access the course remote service.
	 */
	
	public List<Course> getCourseByCourseCode(long groupId, String courseCode, boolean deleted) {
		return CourseLocalServiceUtil.getCourseByCourseCode(groupId, courseCode, deleted);
	}
	
	public JSONArray getAllActiveCourse(long groupId) {
		List<Course> courses = CourseLocalServiceUtil.getAllActiveCourse(groupId);
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
		for(Course course : courses) {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
			jsonObject.put("value", course.getCourseTitle().toUpperCase());
			jsonObject.put("code", course.getCourseCode());
			jsonObject.put("type", course.getCourseType());
			jsonArray.put(jsonObject);
		}
		return jsonArray;
	}
	
	public JSONArray getCourseScheduleFromTMS(long groupId, String courseCode) {
		JSONArray courseJsonArray = JSONFactoryUtil.createJSONArray();
		try {
			ParameterGroup parameterAuthGroup = ParameterGroupLocalServiceUtil
					.getByCode(CourseAdminConstant.PARAMETER_AUTH_GROUP_CODE, false);
			long siteGroupId = parameterAuthGroup.getGroupId();
			Parameter parameterClientId = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), CourseAdminConstant.PARAMETER_CLIENT_ID_CODE,
					false);
			Parameter parameterClientSecret = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), CourseAdminConstant.PARAMETER_CLIENT_SECRET_CODE,
					false);
			ParameterGroup parameterApiGroup = ParameterGroupLocalServiceUtil.getByCode(CourseAdminConstant.PARAMETER_URL_GROUP_CODE, false);	
			Parameter parameterApiGetCourse = ParameterLocalServiceUtil.getByGroupCode(siteGroupId, parameterApiGroup.getParameterGroupId(), CourseAdminConstant.PARAMETER_GET_COURSE_CODE, false);
			Object courseResponse = HttpApiUtil.request(parameterApiGetCourse.getParamValue()+"/"+courseCode, Http.Method.GET.name(), "", parameterClientId.getParamValue(), parameterClientSecret.getParamValue(),"");
			
			if(Validator.isNotNull(courseResponse)) {
				courseJsonArray = JSONFactoryUtil.createJSONArray(courseResponse.toString());
			}
			return courseJsonArray;
		} catch (JSONException e) {
			return null;
		}
		
	}
}