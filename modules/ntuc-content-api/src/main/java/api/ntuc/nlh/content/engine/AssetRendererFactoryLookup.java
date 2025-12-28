package api.ntuc.nlh.content.engine;

import com.liferay.asset.kernel.model.AssetRendererFactory;

public abstract interface AssetRendererFactoryLookup {

	public abstract AssetRendererFactory<?> getAssetRendererFactoryByClassName(String paramString);

}
