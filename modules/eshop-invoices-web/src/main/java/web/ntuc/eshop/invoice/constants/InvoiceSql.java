package web.ntuc.eshop.invoice.constants;

public class InvoiceSql {
	public static String merchandizeSql = "SELECT O.commerceOrderId AS transactionId,\r\n" + 
			"O.total AS amount,\r\n" + 
			"O.createDate AS orderDate,\r\n" + 
			"O.orderStatus AS orderStatus,\r\n" + 
			"COP.status AS paymentStatus \r\n" + 
			"FROM CommerceOrderItem as CO\r\n" + 
			"INNER JOIN CommerceOrder AS O on O.commerceOrderId = CO.commerceOrderId\r\n" + 
			"INNER JOIN CProduct AS CP ON CO.CProductId = CP.CProductId\r\n" + 
			"INNER JOIN CPDefinition CPD ON CP.CProductId = CPD.CProductId\r\n" + 
			"INNER JOIN CommerceOrderPayment AS COP ON COP.commerceOrderId = O.commerceOrderId\r\n" + 
			"INNER JOIN Group_ AS G ON CP.groupId = G.groupId \r\n" + 
			"INNER JOIN CommerceCatalog AS CC ON G.classPK = CC.commerceCatalogId \r\n"+
			"WHERE O.commerceAccountId = ?\r\n" + 
//			"AND CC.name = 'NTUC1' \r\n" +
			"AND CC.name = 'Merchandise' \r\n" +
			"GROUP BY O.commerceOrderId \r\n"+
			"LIMIT ?, ?";
	
	public static String merchandizeSqlCount = "SELECT COUNT(*) AS countItem FROM (SELECT O.commerceOrderId AS transactionId,\r\n" + 
			"O.total AS amount,\r\n" + 
			"O.createDate AS orderDate,\r\n" + 
			"O.orderStatus AS orderStatus,\r\n" + 
			"COP.status AS paymentStatus \r\n" + 
			"FROM CommerceOrderItem as CO\r\n" + 
			"INNER JOIN CommerceOrder AS O on O.commerceOrderId = CO.commerceOrderId\r\n" + 
			"INNER JOIN CProduct AS CP ON CO.CProductId = CP.CProductId\r\n" + 
			"INNER JOIN CPDefinition CPD ON CP.CProductId = CPD.CProductId\r\n" + 
			"INNER JOIN CommerceOrderPayment AS COP ON COP.commerceOrderId = O.commerceOrderId\r\n" + 
			"INNER JOIN Group_ AS G ON CP.groupId = G.groupId \r\n" + 
			"INNER JOIN CommerceCatalog AS CC ON G.classPK = CC.commerceCatalogId \r\n"+
			"WHERE O.commerceAccountId = ?\r\n" + 
//			"AND CC.name = 'NTUC1' \r\n" +
			"AND CC.name = 'Merchandise' \r\n" +
			"GROUP BY O.commerceOrderId) AS totalCount \r\n"+
			"LIMIT ?, ?";
	
	public static String examSql = "SELECT O.commerceOrderId AS transactionId,\r\n" + 
			"O.total AS amount,\r\n" + 
			"O.createDate AS orderDate,\r\n" + 
			"O.orderStatus AS orderStatus,\r\n" + 
			"COP.status AS paymentStatus \r\n" + 
			"FROM CommerceOrderItem as CO\r\n" + 
			"INNER JOIN CommerceOrder AS O on O.commerceOrderId = CO.commerceOrderId\r\n" + 
			"INNER JOIN CProduct AS CP ON CO.CProductId = CP.CProductId\r\n" + 
			"INNER JOIN CPDefinition CPD ON CP.CProductId = CPD.CProductId\r\n" + 
			"INNER JOIN CommerceOrderPayment AS COP ON COP.commerceOrderId = O.commerceOrderId\r\n" + 
			"INNER JOIN Group_ AS G ON CP.groupId = G.groupId \r\n" + 
			"INNER JOIN CommerceCatalog AS CC ON G.classPK = CC.commerceCatalogId \r\n"+
			"WHERE O.commerceAccountId = ?\r\n" + 
			"AND CC.name = 'e-shop' \r\n" +
//			"AND CC.name = 'e-shop' \r\n" +
			"GROUP BY O.commerceOrderId \r\n"+
			"LIMIT ?, ?";
	
	public static String examSqlCount = "SELECT COUNT(*) AS countItem FROM (SELECT O.commerceOrderId AS transactionId,\r\n" + 
			"O.total AS amount,\r\n" + 
			"O.createDate AS orderDate,\r\n" + 
			"O.orderStatus AS orderStatus,\r\n" + 
			"COP.status AS paymentStatus \r\n" + 
			"FROM CommerceOrderItem as CO\r\n" + 
			"INNER JOIN CommerceOrder AS O on O.commerceOrderId = CO.commerceOrderId\r\n" + 
			"INNER JOIN CProduct AS CP ON CO.CProductId = CP.CProductId\r\n" + 
			"INNER JOIN CPDefinition CPD ON CP.CProductId = CPD.CProductId\r\n" + 
			"INNER JOIN CommerceOrderPayment AS COP ON COP.commerceOrderId = O.commerceOrderId\r\n" + 
			"INNER JOIN Group_ AS G ON CP.groupId = G.groupId \r\n" + 
			"INNER JOIN CommerceCatalog AS CC ON G.classPK = CC.commerceCatalogId \r\n"+
			"WHERE O.commerceAccountId = ?\r\n" + 
			"AND CC.name = 'e-shop' \r\n" +
//			"AND CC.name = 'e-shop' \r\n" +
			"GROUP BY O.commerceOrderId) AS totalCount \r\n"+
			"LIMIT ?, ?";
	
	public static String examDetailSql = "SELECT O.commerceOrderId AS invoiceNumber,\r\n" + 
			"ExtractValue(CO.name, ?) AS examTitle, \r\n" +
			"CO.sku AS examCode, \r\n"+
			"CO.quantity AS quantity, \r\n"+
			"CO.unitPrice AS perUnit, \r\n"+
			"O.total AS amount,\r\n" +  
			"O.orderStatus AS orderStatus,\r\n" + 
			"COP.status AS paymentStatus \r\n" +
			"FROM CommerceOrderItem as CO\r\n" + 
			"INNER JOIN CommerceOrder AS O on O.commerceOrderId = CO.commerceOrderId\r\n" + 
			"INNER JOIN CProduct AS CP ON CO.CProductId = CP.CProductId\r\n" + 
			"INNER JOIN CPDefinition CPD ON CP.CProductId = CPD.CProductId\r\n" + 
			"INNER JOIN CommerceOrderPayment AS COP ON COP.commerceOrderId = O.commerceOrderId\r\n" + 
			"INNER JOIN Group_ AS G ON CP.groupId = G.groupId \r\n" + 
			"INNER JOIN CommerceCatalog AS CC ON G.classPK = CC.commerceCatalogId \r\n"+
			"WHERE O.commerceAccountId = ?\r\n" + 
			"AND O.commerceOrderId = ? \r\n" +
			"AND CC.name = 'e-shop' \r\n" +
//			"AND CC.name = 'e-shop' \r\n" +
			"GROUP BY O.commerceOrderId \r\n"+
			"LIMIT ?, ?";
	
	public static String examDetailSqlCount = "SELECT COUNT(*) AS countItem FROM (SELECT O.commerceOrderId AS invoiceNumber,\r\n" + 
			"ExtractValue(CO.name, ?) AS examTitle, \r\n" +
			"CO.sku AS examCode, \r\n"+
			"CO.quantity AS quantity, \r\n"+
			"CO.unitPrice AS perUnit, \r\n"+
			"O.total AS amount,\r\n" +  
			"O.orderStatus AS orderStatus,\r\n" + 
			"COP.status AS paymentStatus \r\n" + 
			"FROM CommerceOrderItem as CO\r\n" + 
			"INNER JOIN CommerceOrder AS O on O.commerceOrderId = CO.commerceOrderId\r\n" + 
			"INNER JOIN CProduct AS CP ON CO.CProductId = CP.CProductId\r\n" + 
			"INNER JOIN CPDefinition CPD ON CP.CProductId = CPD.CProductId\r\n" + 
			"INNER JOIN CommerceOrderPayment AS COP ON COP.commerceOrderId = O.commerceOrderId\r\n" + 
			"INNER JOIN Group_ AS G ON CP.groupId = G.groupId \r\n" + 
			"INNER JOIN CommerceCatalog AS CC ON G.classPK = CC.commerceCatalogId \r\n"+
			"WHERE O.commerceAccountId = ?\r\n" + 
			"AND O.commerceOrderId = ? \r\n" +
			"AND CC.name = 'e-shop' \r\n" +
//			"AND CC.name = 'e-shop' \r\n" +
			"GROUP BY O.commerceOrderId) AS totalCount \r\n"+
			"LIMIT ?, ?";
	
	public static String merchandizeDetailSql = "SELECT O.commerceOrderId AS invoiceNumber,\r\n" + 
			"ExtractValue(CO.name, ?) AS merchTitle, \r\n" +
			"CO.sku AS merchCode, \r\n"+
			"CO.quantity AS quantity, \r\n"+
			"CO.unitPrice AS perUnit, \r\n"+
			"O.total AS amount,\r\n" +  
			"O.orderStatus AS orderStatus,\r\n" + 
			"COP.status AS paymentStatus \r\n" +
			"FROM CommerceOrderItem as CO\r\n" + 
			"INNER JOIN CommerceOrder AS O on O.commerceOrderId = CO.commerceOrderId\r\n" + 
			"INNER JOIN CProduct AS CP ON CO.CProductId = CP.CProductId\r\n" + 
			"INNER JOIN CPDefinition CPD ON CP.CProductId = CPD.CProductId\r\n" + 
			"INNER JOIN CommerceOrderPayment AS COP ON COP.commerceOrderId = O.commerceOrderId\r\n" + 
			"INNER JOIN Group_ AS G ON CP.groupId = G.groupId \r\n" + 
			"INNER JOIN CommerceCatalog AS CC ON G.classPK = CC.commerceCatalogId \r\n"+
			"WHERE O.commerceAccountId = ?\r\n" + 
			"AND O.commerceOrderId = ? \r\n" +
			"AND CC.name = 'Merchandise' \r\n" +
//			"AND CC.name = 'NTUC1' \r\n" +
			"GROUP BY O.commerceOrderId \r\n"+
			"LIMIT ?, ?";
	
	public static String merchDetailSqlCount = "SELECT COUNT(*) AS countItem FROM (SELECT O.commerceOrderId AS invoiceNumber,\r\n" + 
			"ExtractValue(CO.name, ?) AS merchTitle, \r\n" +
			"CO.sku AS merchCode, \r\n"+
			"CO.quantity AS quantity, \r\n"+
			"CO.unitPrice AS perUnit, \r\n"+
			"O.total AS amount,\r\n" +  
			"O.orderStatus AS orderStatus,\r\n" + 
			"COP.status AS paymentStatus \r\n" + 
			"FROM CommerceOrderItem as CO\r\n" + 
			"INNER JOIN CommerceOrder AS O on O.commerceOrderId = CO.commerceOrderId\r\n" + 
			"INNER JOIN CProduct AS CP ON CO.CProductId = CP.CProductId\r\n" + 
			"INNER JOIN CPDefinition CPD ON CP.CProductId = CPD.CProductId\r\n" + 
			"INNER JOIN CommerceOrderPayment AS COP ON COP.commerceOrderId = O.commerceOrderId\r\n" + 
			"INNER JOIN Group_ AS G ON CP.groupId = G.groupId \r\n" + 
			"INNER JOIN CommerceCatalog AS CC ON G.classPK = CC.commerceCatalogId \r\n"+
			"WHERE O.commerceAccountId = ?\r\n" + 
			"AND O.commerceOrderId = ? \r\n" +
			"AND CC.name = 'Merchandise' \r\n" +
//			"AND CC.name = 'NTUC1' \r\n" +
			"GROUP BY O.commerceOrderId) AS totalCount \r\n"+
			"LIMIT ?, ?";
}
