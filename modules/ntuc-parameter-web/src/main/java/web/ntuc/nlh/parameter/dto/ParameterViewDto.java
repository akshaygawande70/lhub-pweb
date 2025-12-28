package web.ntuc.nlh.parameter.dto;

import java.util.Locale;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;

import api.ntuc.common.util.DateUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;

public class ParameterViewDto {
	
	private String code;
	private String name;
	private String groupCode;
	private String groupName;
	private String value;
	private String desc;
	private String createDate;
	private String createBy;
	private String modifiedDate;
	private String modifiedBy;

	public ParameterViewDto(String code, String name, String groupCode, String groupName, String value, String desc,
			String createDate, String createBy, String modifiedDate, String modifiedBy) {
		super();
		this.code = code;
		this.name = name;
		this.groupCode = groupCode;
		this.groupName = groupName;
		this.value = value;
		this.desc = desc;
		this.createDate = createDate;
		this.createBy = createBy;
		this.modifiedDate = modifiedDate;
		this.modifiedBy = modifiedBy;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static ParameterViewDto mapFromParameter(Parameter param) throws PortalException {
		ParameterGroup group = ParameterGroupLocalServiceUtil.getParameterGroup(param.getParameterGroupId());

		User userCreate = UserLocalServiceUtil.getUser(param.getCreatedBy());
		User userModifie = UserLocalServiceUtil.getUser(param.getModifiedBy());
		DateUtil dateUtil = new DateUtil();
		return new ParameterViewDto(param.getParamCode(), param.getParamName(), group.getGroupCode(),
				group.getGroupName(), param.getParamValue(), param.getDescription(),
				dateUtil.toString(param.getCreatedDate()), userCreate.getFullName(),
				dateUtil.toString(param.getModifiedDate()), userModifie.getFullName());
	}

	public static ParameterViewDto mapFromParameterGroup(ParameterGroup paramGroup) throws PortalException {
		User userCreate = UserLocalServiceUtil.getUser(paramGroup.getCreatedBy());
		User userModifie = UserLocalServiceUtil.getUser(paramGroup.getModifiedBy());
		DateUtil dateUtil = new DateUtil();
		return new ParameterViewDto(null, null, paramGroup.getGroupCode(), paramGroup.getGroupName(), null,
				paramGroup.getDescription(), dateUtil.toString(paramGroup.getCreatedDate()), userCreate.getFullName(),
				dateUtil.toString(paramGroup.getModifiedDate()), userModifie.getFullName());
	}
	
}
