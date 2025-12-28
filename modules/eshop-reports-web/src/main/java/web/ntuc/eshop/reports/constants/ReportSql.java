package web.ntuc.eshop.reports.constants;

public class ReportSql {
    public static String bestProductQuery = "SELECT PR.*, \r\n" + 
    		" (PR.grossSellingPrice - PR.costOfProduct) as grossProfit,\r\n" + 
    		" ((PR.grossSellingPrice - PR.costOfProduct)  / PR.grossSellingPrice * 100) as gpMargin,\r\n" + 
    		" GROUP_CONCAT(DISTINCT(AC.name) ORDER BY AC.treePath SEPARATOR ', ') AS category \r\n" + 
    		" FROM ( \r\n" + 
    		" SELECT  \r\n" + 
    		" CONCAT(MIN(COI.createDate)) AS transactionStart,\r\n" + 
    		" CONCAT(MAX(COI.createDate)) AS transactionEnd,\r\n" + 
    		" COI.CProductId,\r\n" + 
    		" ExtractValue(COI.name, ?) AS productName,\r\n" + 
    		" CPI.cost AS costPrice,\r\n" + 
    		" CPI.price  AS basePrice,\r\n" + 
    		" SUM(COI.Quantity) * CPI.price  as grossSellingPrice,\r\n" + 
    		" SUM(COI.Quantity) * CPI.cost  as costOfProduct,\r\n" +  
    		" CPI.sku,\r\n" + 
    		" SUM(COI.Quantity) AS soldQty,\r\n" + 
    		" EV.data_ AS admin_fee,\r\n" + 
    		" CP.publishedCPDefinitionId AS publishedCPDefinitionId\r\n" + 
    		" FROM CommerceOrderItem AS COI\r\n" + 
    		" INNER JOIN CommerceOrder AS CO on CO.commerceOrderId = COI.commerceOrderId\r\n" + 
    		" INNER JOIN CProduct AS CP ON COI.CProductId = CP.CProductId\r\n" + 
    		" INNER JOIN CPDefinition CPD ON CP.CProductId = CPD.CProductId \r\n" + 
    		" INNER JOIN CPInstance AS CPI ON CPD.CPDefinitionId  = CPI.CPDefinitionId \r\n" + 
    		" INNER JOIN CommercePriceEntry CPE on CP.CProductId = CPE.CProductId\r\n" + 
    		" LEFT JOIN ExpandoValue EV on CPE.commercePriceEntryId = EV.classPK and EV.columnId = \r\n" + 
    		" (Select columnId from ExpandoColumn EC where EC.name='Admin Fee')\r\n" + 
    		" %s \r\n" + 
    		" GROUP BY COI.CProductId\r\n" + 
    		" ) AS PR \r\n" + 
    		" INNER JOIN AssetEntry AS AE ON PR.publishedCPDefinitionId = AE.classPK\r\n" + 
    		" INNER JOIN AssetEntryAssetCategoryRel AS AE_rel ON AE.entryId = AE_rel.assetEntryId\r\n" + 
    		" INNER JOIN AssetCategory AS AC ON AE_rel.assetCategoryId = AC.categoryId\r\n" + 
    		" GROUP BY PR.CProductId" +
    		" LIMIT ?, ?";

    public static String bestProductQueryCount = "SELECT COUNT(DISTINCT (PR.CProductId)) AS countItem\r\n" + 
    		" FROM ( \r\n" + 
    		" SELECT  \r\n" + 
    		" CONCAT(MIN(COI.createDate)) AS transactionStart,\r\n" + 
    		" CONCAT(MAX(COI.createDate)) AS transactionEnd,\r\n" + 
    		" COI.CProductId,\r\n" + 
    		" ExtractValue(COI.name, ?) AS productName,\r\n" + 
    		" CPI.cost AS costPrice,\r\n" + 
    		" CPI.price  AS basePrice,\r\n" + 
    		" SUM(COI.Quantity) * CPI.price  as grossSellingPrice,\r\n" + 
    		" SUM(COI.Quantity) * CPI.cost  as costOfProduct,\r\n" + 
    		" CPI.sku,\r\n" + 
    		" SUM(COI.Quantity) AS soldQty,\r\n" + 
    		" EV.data_ AS admin_fee,\r\n" + 
    		" CP.publishedCPDefinitionId AS publishedCPDefinitionId\r\n" + 
    		" FROM CommerceOrderItem AS COI\r\n" + 
    		" INNER JOIN CommerceOrder AS CO on CO.commerceOrderId = COI.commerceOrderId\r\n" + 
    		" INNER JOIN CProduct AS CP ON COI.CProductId = CP.CProductId\r\n" + 
    		" INNER JOIN CPDefinition CPD ON CP.CProductId = CPD.CProductId \r\n" + 
    		" INNER JOIN CPInstance AS CPI ON CPD.CPDefinitionId  = CPI.CPDefinitionId \r\n" + 
    		" INNER JOIN CommercePriceEntry CPE on CP.CProductId = CPE.CProductId\r\n" + 
    		" LEFT JOIN ExpandoValue EV on CPE.commercePriceEntryId = EV.classPK and EV.columnId = \r\n" + 
    		" (Select columnId from ExpandoColumn EC where EC.name='Admin Fee')\r\n" + 
    		" %s \r\n" + 
    		" GROUP BY COI.CProductId\r\n" + 
    		" ) AS PR \r\n" + 
    		" INNER JOIN AssetEntry AS AE ON PR.publishedCPDefinitionId = AE.classPK\r\n" + 
    		" INNER JOIN AssetEntryAssetCategoryRel AS AE_rel ON AE.entryId = AE_rel.assetEntryId\r\n" + 
    		" INNER JOIN AssetCategory AS AC ON AE_rel.assetCategoryId = AC.categoryId"; 

    public static String orderDetailQuery =  "SELECT\r\n" + 
    		"O.createDate AS orderCreatedDate,\r\n" + 
    		"CO.commerceOrderId AS orderId,\r\n" + 
    		"CA.email as custEmail,\r\n" + 
    		"CA.name as custName,\r\n" + 
    		"O.orderStatus AS status,\r\n" + 
    		"CO.CProductId AS productId,\r\n" + 
    		"ExtractValue(CO.name, ?) AS productName,\r\n" + 
    		"CPI.sku,\r\n" + 
    		"CO.quantity AS quantity,    \r\n" + 
    		"CPI.price AS unitPrice,\r\n" + 
    		"CO.discountAmount AS discount, \r\n" +
    		"O.total AS basePrice,\r\n" + 
    		"O.taxAmount as gst,\r\n" + 
    		"CO.finalPriceWithTaxAmount * CO.quantity AS total,\r\n" + 
    		"COP.content AS content\r\n" + 
    		"FROM CommerceOrderItem AS CO\r\n" + 
    		"INNER JOIN CommerceOrder AS O on O.commerceOrderId = CO.commerceOrderId\r\n" + 
    		"INNER JOIN CProduct AS CP ON CO.CProductId = CP.CProductId\r\n" + 
    		"INNER JOIN CPDefinition CPD ON CP.CProductId = CPD.CProductId\r\n" + 
    		"INNER JOIN CPInstance AS CPI ON CPD.CPDefinitionId  = CPI.CPDefinitionId\r\n"+
    		"INNER JOIN CommerceAccount AS CA ON O.commerceAccountId = CA.commerceAccountId\r\n" + 
    		"INNER JOIN CommerceOrderPayment 	AS COP 	ON COP.commerceOrderId = O.commerceOrderId \r\n" + 
    		"WHERE CO.name LIKE ? %s %s %s\r\n" +
    		"AND COP.status = 0 \r\n"+
    		"AND O.total != 0 \r\n"+
    		"GROUP BY O.commerceOrderId \r\n"+
            "ORDER BY %s %s\r\n" +
            "LIMIT ?, ?";

    public static String orderDetailQueryCount =  "SELECT\n" +
            "\tCOUNT(*) AS countItem\n" +
            "FROM (\n" +
            "SELECT\r\n" + 
            "O.createDate AS orderCreatedDate, \r\n" + 
            "CO.commerceOrderId AS orderId, \r\n" + 
            "CA.email as custEmail, \r\n" + 
            "CA.name as custName, \r\n" + 
            "O.orderStatus AS status,\r\n" + 
            "CO.CProductId AS productId,\r\n" + 
            "ExtractValue(CO.name, ?) AS productName,\r\n" + 
            "sku, \r\n" + 
            "CO.quantity AS quantity,     \r\n" + 
            "CO.finalPriceWithTaxAmount AS unitPrice, \r\n" + 
            "CO.discountAmount AS discount, \r\n" + 
            "O.total AS basePrice, \r\n" + 
            "O.taxAmount as gst, \r\n" + 
            "CO.finalPriceWithTaxAmount * CO.quantity AS total,\r\n" + 
            "COP.content AS content\r\n" + 
            "FROM CommerceOrderItem AS CO \r\n" + 
            "INNER JOIN CommerceOrder 	AS O 	ON O.commerceOrderId = CO.commerceOrderId \r\n" + 
            "INNER JOIN CommerceOrderPayment 	AS COP 	ON COP.commerceOrderId = O.commerceOrderId \r\n" + 
            "INNER JOIN CommerceAccount AS CA ON O.commerceAccountId = CA.commerceAccountId\r\n" + 
            "WHERE CO.name LIKE ? %s %s %s \r\n" + 
            "AND COP.status = 0 \r\n"+
            "AND O.total != 0 \r\n"+
            "GROUP BY O.commerceOrderId \r\n"+
            ") AS C\n";

    public static String totalOrderSummary = 
    		"SELECT *, (e.grossPrice - e.costPrice) as grossProfit FROM ( \n"+ 
    		"SELECT \n" +
            "CO.createDate AS orderDate,\n" +
            "CO.commerceOrderId AS orderId,\n" +
            "CO.total AS grossPrice,\n" +
            "COP.status,\n" +
            "SUM(CPI.cost) as costPrice, \n"+
            "COP.content \n"+
            "FROM CommerceOrder AS CO\n" +
            "INNER JOIN CommerceOrderPayment as COP on COP.commerceOrderId = CO.commerceOrderId \n"+
            "INNER JOIN CommerceOrderItem as COI  on COI.commerceOrderId = CO.commerceOrderId \n"+
            "INNER JOIN CPInstance as CPI on COI.CPInstanceId  = CPI.CPInstanceId  \n"+
            "WHERE COP.status = 0 %s \n" +
            "GROUP BY CO.commerceOrderId) as e \n"+
            "ORDER BY %s %s\r\n" +
            "LIMIT ?, ?";

    public static String totalOrderSummaryCount = "SELECT \n" +
            "SUM(CO.total) AS totalOrder,\n" +
            "COUNT(DISTINCT(CO.commerceOrderId)) AS countItem\n" +
            "FROM CommerceOrder as CO\n" +
            "INNER JOIN CommerceOrderPayment as COP on COP.commerceOrderId = CO.commerceOrderId";
    		
    public static String specialOrderQuery = " SELECT * FROM \r\n" + 
    		" ( SELECT\r\n" + 
    		"    ExtractValue(AE.title, ?) AS productName,\r\n" + 
    		"    IFNULL(EV.data_, 'false') AS citrepStatus\r\n" + 
    		"FROM CProduct AS CP\r\n" + 
    		"INNER JOIN AssetEntry AS AE ON CP.publishedCPDefinitionId = AE.classPK\r\n" + 
    		"LEFT JOIN ExpandoValue as EV ON CP.publishedCPDefinitionId = EV.classPK\r\n" + 
    		"AND EV.columnId =  ( \r\n" + 
    		"SELECT columnId FROM ExpandoColumn EC\r\n" + 
    		"INNER JOIN ExpandoTable ET on EC.tableId  = ET.tableId \r\n" + 
    		"INNER JOIN ClassName_ CN on CN.classNameId = ET.classNameId\r\n" + 
    		"WHERE EC.name = ?\r\n" + 
    		"AND CN.value = ?\r\n" + 
    		")\r\n" +  
    		"GROUP BY AE.title, EV.data_) AS PR \r\n" + 
    		"WHERE PR.citrepStatus LIKE 'true'" +
            "ORDER BY %s %s\n" +
            "LIMIT ?, ?";
    
    public static String specialOrderQueryCount = "SELECT COUNT(*) AS countItem FROM (\r\n" + 
    		"select * from \r\n" + 
    		" ( SELECT\r\n" + 
    		" ExtractValue(AE.title, ?) AS productName," + 
    		" IFNULL(EV.data_, 'false') AS citrepStatus\r\n" + 
    		"FROM CProduct AS CP\r\n" + 
    		"INNER JOIN AssetEntry AS AE ON CP.publishedCPDefinitionId = AE.classPK\r\n" + 
    		"LEFT JOIN ExpandoValue as EV ON CP.publishedCPDefinitionId = EV.classPK\r\n" + 
    		"AND EV.columnId =  ( \r\n" + 
    		"SELECT columnId FROM ExpandoColumn EC\r\n" + 
    		"INNER JOIN ExpandoTable ET on EC.tableId  = ET.tableId \r\n" + 
    		"INNER JOIN ClassName_ CN on CN.classNameId = ET.classNameId\r\n" + 
    		"WHERE EC.name = ?\r\n" + 
    		"AND CN.value = ?\r\n" + 
    		")\r\n" + 
    		"GROUP BY AE.title, EV.data_) as PR \r\n" + 
    		"WHERE PR.citrepStatus like 'true') as C";
    
    public static String productInventoryDetailsQuery = 
    		" SELECT PR.*,\r\n" + 
    		" ExtractValue(AE.title, ?) AS productName,\r\n" + 
    		" (PR.basePrice - PR.costPrice) as grossProfit,\r\n" + 
    		"((PR.basePrice - PR.costPrice) / PR.basePrice) * 100 as gpMargin, \r\n" + 
    		" GROUP_CONCAT(DISTINCT(AC.name)ORDER BY AC.treePath ASC SEPARATOR ', ') AS category\r\n" + 
    		" FROM (\r\n" + 
    		" SELECT \r\n" + 
    		" CONCAT (MIN(CWI.createDate)) AS productCreationDate,\r\n" + 
    		" SUM(CWI.quantity) AS availableStock,\r\n" + 
    		" CPI.cost  AS costPrice,\r\n" + 
    		" CPI.price AS basePrice,\r\n" + 
    		" CP.publishedCPDefinitionId AS publishedCPDefinitionId,\r\n" + 
    		" CPI.status AS status,\r\n" + 
    		" CWI.sku AS sku,\r\n" + 
    		" SUM(COI.Quantity) AS soldQty,\r\n" + 
    		" CP.CProductId AS productId, \r\n" + 
    		" SUM(COI.Quantity) * CPI.cost  as grossSellingPrice,\r\n" + 
    		" SUM(COI.Quantity) * CPI.price  as costOfProduct,\r\n" + 
    		" EV.data_ AS admin_fee \r\n" + 
    		" FROM CIWarehouseItem AS CWI\r\n" + 
    		" INNER JOIN CPInstance AS CPI ON CWI.sku = CPI.sku \r\n" + 
    		" INNER JOIN CPDefinition AS CPD ON CPI.CPDefinitionId = CPD.CPDefinitionId\r\n" + 
    		" INNER JOIN CProduct AS CP ON CPD.CProductId = CP.CProductId \r\n" + 
    		" INNER JOIN CommercePriceEntry CPE on CP.CProductId = CPE.CProductId\r\n" + 
    		" LEFT JOIN CommerceOrderItem AS COI ON CP.CProductId = COI.CProductId \r\n" + 
    		" LEFT JOIN ExpandoValue EV on CPE.commercePriceEntryId = EV.classPK and EV.columnId = \r\n" + 
    		" (Select columnId from ExpandoColumn EC where EC.name='Admin Fee') \r\n" + 
    		" GROUP BY CP.CProductId \r\n" + 
    		"  ) AS PR\r\n" + 
    		" INNER JOIN AssetEntry AS AE ON PR.publishedCPDefinitionId = AE.classPK\r\n" + 
    		" INNER JOIN AssetEntryAssetCategoryRel AS AE_rel ON AE.entryId = AE_rel.assetEntryId\r\n" + 
    		" INNER JOIN AssetCategory AS AC ON AE_rel.assetCategoryId = AC.categoryId\r\n" + 
    		"  WHERE AC.vocabularyId = 2043483 \r\n" + //prod
//    		"  WHERE AC.vocabularyId = 1680226 \r\n" + //uat
    		" GROUP BY PR.productId" + 
    		" %s \r\n"+
    		" ORDER BY %s %s \r\n"+
    		" LIMIT ?, ?";
    
    public static String productInventoryDetailsQueryCount = "SELECT \r\n" + 
    		" COUNT(*) AS countItem\r\n" + 
    		" FROM (\r\n" + 
    		" SELECT \r\n" + 
    		" GROUP_CONCAT(DISTINCT(AC.name) SEPARATOR ', ') AS category,\r\n" + 
    		" CONCAT (MIN(CWI.createDate)) AS productCreationDate,\r\n" + 
    		" SUM(CWI.quantity) AS availableStock,\r\n" + 
    		" CPI.cost  AS costPrice,\r\n" + 
    		" CPI.price AS basePrice,\r\n" + 
    		" CP.publishedCPDefinitionId AS publishedCPDefinitionId,\r\n" + 
    		" CPI.status AS status,\r\n" + 
    		" CWI.sku AS sku,\r\n" + 
    		" SUM(COI.Quantity) AS soldQty,\r\n" + 
    		" CP.CProductId AS productId, \r\n" + 
    		" SUM(COI.Quantity) * CPI.cost  as grossSellingPrice,\r\n" + 
    		" SUM(COI.Quantity) * CPI.price  as costOfProduct,\r\n" + 
    		" EV.data_ AS admin_fee \r\n" + 
    		" FROM CIWarehouseItem AS CWI\r\n" + 
    		" INNER JOIN CPInstance AS CPI ON CWI.sku = CPI.sku \r\n" + 
    		" INNER JOIN CPDefinition AS CPD ON CPI.CPDefinitionId = CPD.CPDefinitionId\r\n" + 
    		" INNER JOIN CProduct AS CP ON CPD.CProductId = CP.CProductId \r\n" + 
    		" INNER JOIN CommercePriceEntry CPE on CP.CProductId = CPE.CProductId\r\n" + 
    		" LEFT JOIN CommerceOrderItem AS COI ON CP.CProductId = COI.CProductId \r\n" + 
    		" LEFT JOIN ExpandoValue EV on CPE.commercePriceEntryId = EV.classPK and EV.columnId = \r\n" + 
    		" (Select columnId from ExpandoColumn EC where EC.name='Admin Fee') \r\n" + 
    		"  INNER JOIN AssetEntry AS AE ON CP.publishedCPDefinitionId = AE.classPK\r\n" + 
    		" INNER JOIN AssetEntryAssetCategoryRel AS AE_rel ON AE.entryId = AE_rel.assetEntryId\r\n" + 
    		" INNER JOIN AssetCategory AS AC ON AE_rel.assetCategoryId = AC.categoryId\r\n" + 
    		" WHERE AC.vocabularyId = 2043483 \r\n" + //prod
//    		" WHERE AC.vocabularyId = 1680226 \r\n" + //uat
    		" GROUP BY CP.CProductId \r\n" + 
    		"  ) AS PR\r\n" + 
    		" %s ";
    		
    public static String lowStockQuery = 
    		"SELECT PR.*,\r\n" + 
    		"PR.availableStock AS lowStock,\r\n" + 
    		"ExtractValue(AE.title, ?) AS productName,\r\n" + 
    		"GROUP_CONCAT(DISTINCT(AC.name) ORDER BY AC.treePath SEPARATOR ', ') AS category\r\n" + 
    		"FROM (\r\n" + 
    		"SELECT\r\n" + 
    		"CONCAT(MIN(CWI.createDate)) AS productCreationDate,\r\n" + 
    		"CWI.quantity AS availableStock,\r\n" + 
    		"CPI.cost AS costPrice,\r\n" + 
    		"CPI.price AS basePrice,\r\n" + 
    		"CP.publishedCPDefinitionId AS publishedCPDefinitionId,\r\n" + 
    		"CPI.status AS status,\r\n" + 
    		"CWI.sku AS sku,\r\n" + 
    		"CP.CProductId AS productId,\r\n" + 
    		"EV.data_ AS admin_fee\r\n" + 
    		"FROM CIWarehouseItem AS CWI\r\n" + 
    		"INNER JOIN CPInstance AS CPI ON CWI.sku = CPI.sku \r\n" + 
    		"INNER JOIN CPDefinition AS CPD ON CPI.CPDefinitionId = CPD.CPDefinitionId\r\n" + 
    		"INNER JOIN CProduct AS CP ON CPD.CProductId = CP.CProductId \r\n" + 
    		"INNER JOIN CommercePriceEntry CPE on CP.CProductId = CPE.CProductId\r\n" + 
    		"LEFT JOIN ExpandoValue EV on CPE.commercePriceEntryId = EV.classPK and EV.columnId = \r\n" + 
    		" (SELECT columnId FROM ExpandoColumn EC WHERE EC.name='Admin Fee')\r\n" +
    		"GROUP BY CP.CProductId \r\n" +  
    		") AS PR\r\n" + 
    		"INNER JOIN AssetEntry AS AE ON PR.publishedCPDefinitionId = AE.classPK\r\n" + 
    		"INNER JOIN AssetEntryAssetCategoryRel AS AE_rel ON AE.entryId = AE_rel.assetEntryId\r\n" + 
    		"INNER JOIN AssetCategory AS AC ON AE_rel.assetCategoryId = AC.categoryId\r\n" + 
      		" WHERE AC.vocabularyId = 2043483 \r\n"+ //prod
//      		" WHERE AC.vocabularyId = 1680226 \r\n"+ //uat
    		"GROUP BY PR.productId \r\n" + 
    		"HAVING lowStock  < 100\r\n" + 
    		" %s \r\n" +  
    		"ORDER BY lowStock DESC" +
    		" LIMIT ?, ?";
    
    public static String lowStockQueryCount = "SELECT \r\n" + 
    		" COUNT(*) AS countItem\r\n" + 
    		" FROM (\r\n" + 
    		" select\r\n" + 
    		" GROUP_CONCAT(DISTINCT(AC.name) ORDER BY AC.treePath SEPARATOR ', ') AS category,\r\n" + 
    		" ExtractValue(AE.title, ?) AS productName,\r\n" +
    		" CONCAT(MIN(CWI.createDate)) AS productCreationDate,\r\n" + 
    		" CWI.quantity as availableStock,\r\n" + 
    		" CPI.cost  AS costPrice,\r\n" + 
    		" CPI.price AS basePrice,\r\n" + 
    		" CP.publishedCPDefinitionId AS publishedCPDefinitionId,\r\n" + 
    		" CPI.status AS status,\r\n" + 
    		" CWI.sku AS sku\r\n" + 
    		" FROM CIWarehouseItem AS CWI\r\n" + 
    		" INNER JOIN CPInstance AS CPI ON CWI.sku = CPI.sku \r\n" + 
    		"INNER JOIN CPDefinition AS CPD ON CPI.CPDefinitionId = CPD.CPDefinitionId\r\n" + 
    		"INNER JOIN CProduct AS CP ON CPD.CProductId = CP.CProductId  \r\n" + 
    		"  INNER JOIN AssetEntry AS AE ON CP.publishedCPDefinitionId = AE.classPK\r\n" + 
    		" INNER JOIN AssetEntryAssetCategoryRel AS AE_rel ON AE.entryId = AE_rel.assetEntryId\r\n" + 
    		" INNER JOIN AssetCategory AS AC ON AE_rel.assetCategoryId = AC.categoryId\r\n" + 
    		" GROUP BY CP.CProductId \r\n" + 
    		" ) AS PR\r\n" + 
    		" WHERE availableStock < 100\r\n" +
    		" %s \r\n";
    
    public static String discountQuery = "SELECT PR.* FROM\r\n" + 
    		" (SELECT \r\n" + 
    		" CD.displayDate AS startDate,\r\n" + 
    		" CD.expirationDate AS endDate,\r\n" + 
    		" CD.title AS discountDescription,\r\n" + 
    		" IFNULL(CD.couponCode, 'Not found') AS discountCode,\r\n" + 
    		" CASE\r\n" + 
    		" WHEN CD.active_ = 0 THEN 'INACTIVE'\r\n" + 
    		" WHEN CD.active_ = 1 THEN 'ACTIVE'\r\n" + 
    		" ELSE 'The quantity is under 30'\r\n" + 
    		" END AS statusDiscount,\r\n" + 
    		" CASE\r\n" + 
    		" WHEN CD.usePercentage  = 0 THEN 'Fixed Amount'\r\n" + 
    		" WHEN CD.usePercentage = 1 THEN 'Percentage'\r\n" + 
    		" ELSE 'The quantity is under 30'\r\n" + 
    		" END AS discountType\r\n" + 
    		" FROM CommerceDiscount AS CD) as PR\r\n" + 
    		" WHERE EXISTS (SELECT PR.discountCode FROM CommerceDiscount WHERE PR.discountCode LIKE ?)\r\n" + 
    		" AND EXISTS (SELECT PR.discountType FROM CommerceDiscount WHERE PR.discountType LIKE ?)\r\n"+
    		" %s %s\r\n"+
    		" ORDER BY %s %s \r\n"+
    		" LIMIT ?, ?";
    
    public static String discountQueryCount = 
    		" SELECT COUNT(*) AS countItem\r\n" + 
    		" FROM(\r\n" + 
    		" SELECT \r\n" + 
    		" CD.displayDate AS startDate,\r\n" + 
    		" CD.expirationDate AS endDate,\r\n" + 
    		" CD.title AS discountDescription,\r\n" + 
    		" IFNULL(CD.couponCode, 'Not found') AS discountCode,\r\n" + 
    		" CASE\r\n" + 
    		" WHEN CD.active_ = 0 THEN 'INACTIVE'\r\n" + 
    		" WHEN CD.active_ = 1 THEN 'ACTIVE'\r\n" + 
    		" ELSE 'The quantity is under 30'\r\n" + 
    		" END AS statusDiscount,\r\n" + 
    		" CASE\r\n" + 
    		" WHEN CD.usePercentage  = 0 THEN 'Fixed Amount'\r\n" + 
    		" WHEN CD.usePercentage = 1 THEN 'Percentage'\r\n" + 
    		" ELSE 'The quantity is under 30'\r\n" + 
    		" END AS discountType\r\n" + 
    		" FROM CommerceDiscount AS CD) AS PR\r\n" + 
    		" WHERE  EXISTS (SELECT PR.discountCode FROM CommerceDiscount WHERE PR.discountCode LIKE ?)\r\n" +  
    		" AND EXISTS (SELECT PR.discountType FROM CommerceDiscount WHERE PR.discountType LIKE ?)\r\n"+
    		" %s %s\r\n"+
    		" ORDER BY startDate DESC";
    
    public static String categoryLists = 
    		" SELECT DISTINCT GROUP_CONCAT(DISTINCT(AC.name)ORDER BY AC.treePath ASC SEPARATOR ', ') AS category\r\n" + 
    		" FROM CIWarehouseItem AS CWI\r\n" + 
    		" INNER JOIN CPInstance AS CPI ON CWI.sku = CPI.sku \r\n" + 
    		" INNER JOIN CPDefinition AS CPD ON CPI.CPDefinitionId = CPD.CPDefinitionId\r\n" + 
    		" INNER JOIN CProduct AS CP ON CPD.CProductId = CP.CProductId  \r\n" + 
    		" INNER JOIN AssetEntry AS AE ON CP.publishedCPDefinitionId = AE.classPK\r\n" + 
    		" INNER JOIN AssetEntryAssetCategoryRel AS AE_rel ON AE.entryId = AE_rel.assetEntryId\r\n" + 
    		" INNER JOIN AssetCategory AS AC ON AE_rel.assetCategoryId = AC.categoryId\r\n" + 
    		" WHERE AC.vocabularyId = 2043483\r\n" + //prod
//    		" WHERE AC.vocabularyId = 1680226\r\n" + //uat
    		" GROUP BY CP.CProductId";
    
//    public static String subCategoryLists = 
//    		" SELECT \r\n" + 
//    		" DISTINCT (AC.name) AS subCategory,\r\n" + 
//    		" AC.treePath \r\n" + 
//    		" FROM CIWarehouseItem AS CWI\r\n" + 
//    		" INNER JOIN CPInstance AS CPI ON CWI.sku = CPI.sku \r\n" + 
//    		" INNER JOIN CPDefinition AS CPD ON CPI.CPDefinitionId = CPD.CPDefinitionId\r\n" + 
//    		" INNER JOIN CProduct AS CP ON CPD.CProductId = CP.CProductId  \r\n" + 
//    		" INNER JOIN AssetEntry AS AE ON CP.publishedCPDefinitionId = AE.classPK\r\n" + 
//    		" INNER JOIN AssetEntryAssetCategoryRel AS AE_rel ON AE.entryId = AE_rel.assetEntryId\r\n" + 
//    		" INNER JOIN AssetCategory AS AC ON AE_rel.assetCategoryId = AC.categoryId\r\n" +  
//    		" WHERE AC.parentCategoryId  IS NOT NULL\r\n" + 
//    		" AND AC.vocabularyId = 1680226\r\n" + 
//    		" AND LENGTH (AC.treePath) > 9\r\n" + 
//    		" GROUP BY CP.CProductId \r\n" + 
//    		" ORDER BY AC.treePath ASC";
    
}
