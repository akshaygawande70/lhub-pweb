package api.ntuc.nlh.content.engine;

public abstract interface SearchResultPreferences {

	public abstract boolean isDisplayResultsInDocumentForm();

	public abstract boolean isHighlightEnabled();

	public abstract boolean isViewInContext();

}
