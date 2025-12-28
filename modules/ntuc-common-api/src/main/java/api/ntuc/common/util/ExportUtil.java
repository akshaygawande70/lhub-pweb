package api.ntuc.common.util;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;

import java.util.List;
import java.util.StringJoiner;

public class ExportUtil {
	
	public static String exportDataToStringBuilderCsv(String[] headers, List<JSONObject> datas,String[] dataKeys ,String delimiter ) {
		StringBuilder sb = new StringBuilder();
		
		//		set header
		StringJoiner headerJoiner = new StringJoiner(delimiter);
		for (String header : headers) {
			headerJoiner.add(header);
		}
		sb.append(headerJoiner.toString());
		sb.append("\n");

		//		set content
		for (int x = 0; x < datas.size(); x++) {
			StringJoiner joiner = new StringJoiner(delimiter);
			for (int y = 0; y < dataKeys.length; y++) {
				
				String data = datas.get(x).getString(dataKeys[y]);
				if(data.contains(StringPool.COMMA)) {
					data = StringPool.QUOTE+data.trim()+StringPool.QUOTE;
				}
				joiner.add(data);
			}
			sb.append(joiner.toString());
			if(x != datas.size() - 1)  sb.append("\n");
		}

		return sb.toString();
	}
	
}
