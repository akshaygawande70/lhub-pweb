package api.ntuc.nlh.content.engine;

import java.io.Serializable;

public class SearchResultFieldDisplayContext implements Serializable {

	private static final long serialVersionUID = 1L;

	public float getBoost() {
		return boost;
	}

	public String getName() {
		return name;
	}

	public String getValuesToString() {
		return valuesToString;
	}

	public boolean isArray() {
		return array;
	}

	public boolean isNumeric() {
		return numeric;
	}

	public boolean isTokenized() {
		return tokenized;
	}

	public void setArray(boolean array) {
		this.array = array;
	}

	public void setBoost(float boost) {
		this.boost = boost;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumeric(boolean numeric) {
		this.numeric = numeric;
	}

	public void setTokenized(boolean tokenized) {
		this.tokenized = tokenized;
	}

	public void setValuesToString(String valuesToString) {
		this.valuesToString = valuesToString;
	}

	private boolean array;
	private float boost;
	private String name;
	private boolean numeric;
	private boolean tokenized;
	private String valuesToString;

}
