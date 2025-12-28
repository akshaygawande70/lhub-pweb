package com.ntuc.notification.portlet.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;

import com.ntuc.notification.model.CourseSchedule;
import com.ntuc.notification.service.CourseScheduleLocalServiceUtil;

import java.io.IOException;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

@Component(
        immediate = true,
        property = {
                "com.liferay.portlet.display-category=category.ntuc",
                "com.liferay.portlet.instanceable=true",
                "javax.portlet.display-name=DT5 Course Schedule",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.init-param.view-template=/course_schedule/view.jsp",
                "javax.portlet.resource-bundle=content.Language",
                "javax.portlet.security-role-ref=power-user,user"
        },
        service = Portlet.class
)
public class CourseSchedulePortlet extends MVCPortlet {

    @Override
    public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {

        // Server-side pagination scaffolding (does NOT change your current UI unless JSP starts using it)
        int delta = ParamUtil.getInteger(
                renderRequest, SearchContainer.DEFAULT_DELTA_PARAM, 25);

        int cur = ParamUtil.getInteger(
                renderRequest, SearchContainer.DEFAULT_CUR_PARAM, 1);

        if (cur < 1) {
            cur = 1;
        }

        if (delta < 1) {
            delta = 25;
        }

        int total = CourseScheduleLocalServiceUtil.getCourseSchedulesCount();

        int start = (cur - 1) * delta;
        int end = start + delta;

        // Guard bounds
        if (start < 0) {
            start = 0;
        }
        if (start > total) {
            start = total;
        }
        if (end > total) {
            end = total;
        }

        // Current UI uses DataTables client-side; we still fetch a page window to prepare for server-side pagination.
        // If you want to keep fetching ALL for now, set start=0 and end=total; but that blocks server-side pagination later.
        List<CourseSchedule> courseSchedules =
                CourseScheduleLocalServiceUtil.getCourseSchedules(start, end);

        renderRequest.setAttribute("courseSchedules", courseSchedules);

        // Pagination metadata for future JSP/server-side DataTables mode
        renderRequest.setAttribute("courseSchedulesTotal", total);
        renderRequest.setAttribute("courseSchedulesCur", cur);
        renderRequest.setAttribute("courseSchedulesDelta", delta);
        renderRequest.setAttribute("courseSchedulesStart", start);
        renderRequest.setAttribute("courseSchedulesEnd", end);

        super.doView(renderRequest, renderResponse);
    }
}
