package web.ntuc.nlh.search.result.constants;

public class CustomSqlConstant {

	public static String getCategoryFromExams = "select " + 
			"	distinct(ac.categoryId) as id , ac.name as name " + 
			"from " + 
			"	AssetEntry ae left join AssetEntryAssetCategoryRel aeacr on ae.entryId=aeacr.assetEntryId " + 
			"    left join AssetCategory ac on aeacr.assetCategoryId = ac.categoryId " + 
			"    left join AssetVocabulary vocab on ac.vocabularyId = vocab.vocabularyId " + 
			"where ae.classPK in #ListDefId# " + 
			"and lower(vocab.name) in #ListVocabName# ";
	
	public static String getDefinitionId= "select " + 
			"	distinct(ae.classPK) as definitionId " + 
			"from " + 
			"	AssetEntry ae left join AssetEntryAssetCategoryRel aeacr on ae.entryId=aeacr.assetEntryId " + 
			"where aeacr.assetCategoryId in #ListCategoryId# ";
}
