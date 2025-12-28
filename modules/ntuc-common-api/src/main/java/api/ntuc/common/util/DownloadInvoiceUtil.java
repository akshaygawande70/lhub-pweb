package api.ntuc.common.util;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
//import com.itextpdf.text.pdf.PdfWriter;
import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil;
import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.constants.CommerceOrderPaymentConstants;
import com.liferay.commerce.model.CommerceAddress;
import com.liferay.commerce.model.CommerceCountry;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.model.CommerceOrderPayment;
import com.liferay.commerce.service.CommerceAddressLocalServiceUtil;
import com.liferay.commerce.service.CommerceCountryLocalServiceUtil;
import com.liferay.commerce.service.CommerceOrderItemLocalServiceUtil;
import com.liferay.commerce.service.CommerceOrderLocalServiceUtil;
import com.liferay.commerce.service.CommerceOrderPaymentLocalServiceUtil;
import com.liferay.commerce.tax.engine.fixed.service.CommerceTaxFixedRateLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.RoleServiceUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
public class DownloadInvoiceUtil {

	private static final Log log = LogFactoryUtil.getLog(DownloadInvoiceUtil.class);
//	private static String ntucPdfPath = "/opt/liferay7.3/tomcat-9.0.82/temp/";
	private static String ntucPdfPath = "/opt/liferay7.3/ntucpdf/";
	private static String imageLogoPath = System.getProperties().getProperty("env.CATALINA_HOME");
	public static String downLoadInvoiceRetry(long commerceOrderId,User user,Locale locale){

		String base64Data="";
		try{
			CommerceOrder commerceOrder = CommerceOrderLocalServiceUtil.getCommerceOrder(commerceOrderId);
			String   discountFromCommerceOrder ="";
			String discount ="";
			double discountAmountFloat=0;
			double discountFromCommerceOrder1 = commerceOrder.getShippingDiscountAmount().doubleValue() + 
					commerceOrder.getSubtotalDiscountAmount().doubleValue()+
					commerceOrder.getTotalDiscountAmount().doubleValue(); 
			double commerceTaxFixedRate = CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).size()>0?
					CommerceTaxFixedRateLocalServiceUtil.getCommerceTaxFixedRates(-1, -1).get(0).getRate():0;
					double shippingAmount1 = commerceOrder.getShippingAmount().doubleValue();
					String fullNameCap = "";
					int accType = 0;
					String companyName = "";
					String purchaseDate ="";
					String invoiceNo = "";
					String    subtotalUnitPrice ="";
					String subtotalUnitPriceTernary="";
					String discountFromCommerceOrderTernary="";
					String totalUnitPriceTernary ="";
					String shippingAmountTernary ="";
					String amountPayable ="";
					long accountId = 0;
					List<Role> roleList = new ArrayList<Role>();
					for (Long roleId : user.getRoleIds()) {
						Role newRole = RoleServiceUtil.getRole(roleId);
						roleList.add(newRole);
					}

					CommerceAccount account2 = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(user.getUserId());
					if (RoleUtil.matchByFullRoleName(roleList, "Eshop_Corporate_Role")) {
						companyName = (String) account2.getExpandoBridge().getAttribute("Company Name");
						log.info(" company name: "+companyName);
						accType = 2;
					} else {
						if (!user.getMiddleName().isEmpty()) {
							fullNameCap = user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName();
						} else {
							fullNameCap = user.getFirstName() + " " + user.getLastName();
						}
						accType = 1;
						fullNameCap = fullNameCap.toUpperCase();
					}


					SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
					long orderId = commerceOrder.getCommerceOrderId();
					List<CommerceOrderItem> commerceOrderItemList = CommerceOrderItemLocalServiceUtil.getCommerceOrderItems(orderId, -1, -1);
					CommerceOrderPayment commerceOrderPayment = CommerceOrderPaymentLocalServiceUtil.fetchLatestCommerceOrderPayment(orderId);
					long commerceAddressId = commerceOrder.getBillingAddressId();
					CommerceAddress commerceAddress = CommerceAddressLocalServiceUtil.getCommerceAddress(commerceAddressId);
					long commerceCountryId = commerceAddress.getCommerceCountryId();
					CommerceCountry commerceCountry = CommerceCountryLocalServiceUtil.getCommerceCountry(commerceCountryId);

					String status = CommerceOrderConstants.getOrderStatusLabel(commerceOrder.getOrderStatus()).toUpperCase();
					String displayName = accType == 2 ? companyName : fullNameCap;
					String taxRate = String.valueOf(commerceTaxFixedRate);
					double taxRateDecimalDynamic = Double.parseDouble(taxRate) * 100 / 100;
					double taxPercentage = Double.parseDouble(taxRate) * 0.01;
					String totalTaxAmount = String.valueOf(commerceOrder.getTaxAmount());
					String totalTaxAmountDecimalDynamic = String.format("%.2f", Double.parseDouble(totalTaxAmount));
					List<String[]> rows = new ArrayList<>();
					rows.add(new String[] { "Item", "SKU", "Qty", "Unit Price", "Discount", "Net Price", "GST", "Total" });
					//
					double subtotalUnitPrice1 = 0;
					double totalUnitPrice1 = 0;
					DecimalFormat decimalFormat = new DecimalFormat("#.00");
					String courseTitle ="";
					String sku ="";
					String quantity ="";
					String unitPrice="";
					String discountTernary ="";
					String netPrice ="";
					String totalEachProduct="";
					String  taxAmountPerProduct="";
					//float getPriceUnit = 0;
					for(int a=0;a<commerceOrderItemList.size();a++) { 
						float getPriceUnit = commerceOrderItemList.get(a).getPromoPrice()!=null && commerceOrderItemList.get(a).getPromoPrice().floatValue()>0 ? 
								commerceOrderItemList.get(a).getPromoPrice().floatValue() : commerceOrderItemList.get(a).getUnitPrice().floatValue() ;
								float getPriceQty = getPriceUnit * commerceOrderItemList.get(a).getQuantity();
								discountAmountFloat = commerceOrderItemList.get(a).getDiscountAmount().floatValue();
								discountFromCommerceOrder1 = discountFromCommerceOrder1 + discountAmountFloat;
								double taxAmountPerProduct1 = getPriceQty * (commerceTaxFixedRate) * 0.01;
								double totalEachProduct1 = taxAmountPerProduct1 + getPriceQty - discountAmountFloat;
						 		totalUnitPrice1 = totalUnitPrice1 + getPriceQty;
								subtotalUnitPrice1 = subtotalUnitPrice1 + commerceOrderItemList.get(a).getFinalPrice().doubleValue();

								 netPrice = String.valueOf(CurrencyUtil.roundUpDollarAmount(String.valueOf(commerceOrderItemList.get(a).getFinalPrice())));
								 courseTitle = commerceOrderItemList.get(a).getName(locale);
								 sku = commerceOrderItemList.get(a).getSku();
								 quantity = String.valueOf(commerceOrderItemList.get(a).getQuantity());
								 unitPrice = decimalFormat.format(getPriceUnit);
								 
								 log.info(unitPrice+"::::::::::::unitPrice:::::::::::::::::"+getPriceUnit);
								 log.info(discountTernary+":::::::::::::::::discount::::::::::::"+discount);
								 
								invoiceNo = String.valueOf(commerceOrderItemList.get(a).getCommerceOrderId());
								purchaseDate = sdf.format(commerceOrderItemList.get(a).getCreateDate());
								  taxAmountPerProduct = decimalFormat.format(taxAmountPerProduct1);
								  totalEachProduct = decimalFormat.format(totalEachProduct1);
								amountPayable = decimalFormat.format(commerceOrder.getTotal());
								String  totalUnitPrice = decimalFormat.format(totalUnitPrice1);

								subtotalUnitPrice = decimalFormat.format(subtotalUnitPrice1);
								String    shippingAmount = decimalFormat.format(shippingAmount1);

								 discountTernary = discount.equals(".00") ? "0" : discount;
								totalUnitPriceTernary = String.valueOf(totalUnitPrice).equals(".00") ? "0" : String.valueOf(totalUnitPrice);
								discountFromCommerceOrderTernary = String.valueOf(discountFromCommerceOrder).equals(".00") ? "0" : String.valueOf(discountFromCommerceOrder);
								subtotalUnitPriceTernary = String.valueOf(subtotalUnitPrice).equals(".00") ? "0" : String.valueOf(subtotalUnitPrice);
								shippingAmountTernary = String.valueOf(shippingAmount).equals(".00") ? "0" : String.valueOf(shippingAmount);

								// List<String[]> rows = new ArrayList<>();
								//rows.add(new String[]{courseTitle, sku, quantity, unitPrice, discountTernary, netPrice, decimalFormat.format(Float.parseFloat(taxAmountPerProduct)), totalEachProduct});
								//convert code start

					}
					discount = decimalFormat.format(discountAmountFloat);
					discountFromCommerceOrder = decimalFormat.format(discountFromCommerceOrder1);

					String totalOtherDiscount = discountFromCommerceOrder;
					String totalDiscount = totalOtherDiscount + discount;
					String billingName =commerceAddress.getName();
					String street1 = commerceAddress.getStreet1();
					String street2 =commerceAddress.getStreet2();
					String street3 =commerceAddress.getStreet3();
					String address = street1 + street2 + street3;
					String city = commerceAddress.getCity();
					String zip = commerceAddress.getZip();
					String phone =commerceAddress.getPhoneNumber();

					String country =commerceCountry.getName(locale);

					Document document = new Document(PageSize.A4);

				
					Date today = new Date();
					String date = ""+today.getTime();
					String fullName = accType == 2 ? companyName : fullNameCap;
					FileOutputStream fos = new FileOutputStream(new File(ntucPdfPath+date+".pdf"));
//					PdfWriter pdfWriter = PdfWriter.getInstance(document, fos);
					log.info("image logo path env.CATALINA_HOME::::::::"+imageLogoPath);

					String stringBuilderHtml1 = "<html>\r\n" + 
							"\r\n" + 
							"<head>\r\n" + 
							"<meta http-equiv=Content-Type content=\"text/html; charset=utf-8\">\r\n" + 
							"<link href=\"https://fonts.googleapis.com/css2?family=Roboto&display=swap\" rel=\"preload\" as=\"style\" crossorigin/>\r\n" + 
							"<link href=\"https://fonts.googleapis.com/css2?family=Roboto&display=swap\" rel=\"stylesheet\"/>\r\n" + 
							"<link rel=\"preload\" href=\"https://fonts.gstatic.com/s/roboto/v30/KFOmCnqEu92Fr1Mu4mxKKTU1Kg.woff2\" crossorigin as=\"font\" type=\"font/woff2\"/>\r\n"+
							"<style>\r\n" + 
							"<!--\r\n" + 
							" /* Font Definitions */\r\n" + 
							" @font-face\r\n" + 
							"	{font-family:'Roboto', sans-serif;\r\n" + 
							"	panose-1:2 4 5 3 5 4 6 3 2 4;}\r\n" + 
							"@font-face\r\n" + 
							"	{font-family:'Roboto', sans-serif;\r\n" + 
							"	panose-1:2 15 5 2 2 2 4 3 2 4;}\r\n" + 
							" /* Style Definitions */\r\n" + 
							" p.MsoNormal, li.MsoNormal, div.MsoNormal\r\n" + 
							"	{margin-top:0cm;\r\n" + 
							"	margin-right:0cm;\r\n" + 
							"	margin-bottom:8.0pt;\r\n" + 
							"	margin-left:0cm;\r\n" + 
							"	line-height:107%;\r\n" + 
							"	font-size:11.0pt;\r\n" + 
							"	font-family:'Roboto',sans-serif;\r\n" + 
							"	color:black;}\r\n" + 
							"h1\r\n" + 
							"	{mso-style-link:\"Heading 1 Char\";\r\n" + 
							"	margin-top:0cm;\r\n" + 
							"	margin-right:0cm;\r\n" + 
							"	margin-bottom:0cm;\r\n" + 
							"	margin-left:320.0pt;\r\n" + 
							"	text-align:center;\r\n" + 
							"	text-indent:0cm;\r\n" + 
							"	line-height:107%;\r\n" + 
							"	page-break-after:avoid;\r\n" + 
							"	font-size:8.0pt;\r\n" + 
							"	font-family:'Roboto',sans-serif;\r\n" + 
							"	color:gray;\r\n" + 
							"	font-weight:normal;}\r\n" + 
							"span.Heading1Char\r\n" + 
							"	{mso-style-name:\"Heading 1 Char\";\r\n" + 
							"	mso-style-link:\"Heading 1\";\r\n" + 
							"	font-family:'Roboto',sans-serif;\r\n" + 
							"	color:gray;}\r\n" + 
							".MsoPapDefault\r\n" + 
							"	{margin-bottom:8.0pt;\r\n" + 
							"	line-height:107%;}\r\n" + 
							"@page WordSection1\r\n" + 
							"	{size:595.3pt 841.9pt;\r\n" + 
							"	margin:40.0pt 77.3pt 28.3pt 40.0pt;}\r\n" + 
							"div.WordSection1\r\n" + 
							"	{page:WordSection1;}\r\n" + 
							"@page WordSection2\r\n" + 
							"	{size:595.3pt 841.9pt;\r\n" + 
							"	margin:40.0pt 386.5pt 28.3pt 40.0pt;}\r\n" + 
							"div.WordSection2\r\n" + 
							"	{page:WordSection2;}\r\n" + 
							"@page WordSection3\r\n" + 
							"	{size:595.3pt 841.9pt;\r\n" + 
							"	margin:40.0pt 253.45pt 28.3pt 253.45pt;}\r\n" + 
							"div.WordSection3\r\n" + 
							"	{page:WordSection3;}\r\n" + 
							" /* List Definitions */\r\n" + 
							" ol\r\n" + 
							"	{margin-bottom:0cm;}\r\n" + 
							"ul\r\n" + 
							"	{margin-bottom:0cm;}\r\n" + 
							"-->\r\n" + 
							"</style>\r\n" + 
							"\r\n" + 
							"</head>\r\n" + 
							"\r\n" + 
							"<body lang=EN-IN style='word-wrap:break-word'>\r\n" + 
							"\r\n" + 
							"<div class=WordSection1>\r\n" + 
							"<p class=MsoNormal style='margin-top:0cm;margin-right:0cm;margin-bottom:1.7pt;\r\n" + 
							"margin-left:320.0pt'><span style='font-size:10.0pt;line-height:107%;color:gray'><b>Tax Invoice/Receipt</b></span></p>\r\n" +
							"<p class=MsoNormal align=center style='margin-top:0cm;margin-right:0cm;\r\n" + 
							"margin-bottom:3.55pt;margin-left:249.95pt;text-align:center'><img width=160\r\n" + 
							"height=51 src=\""+imageLogoPath+"/webapps/ROOT/img/ntuclhub-logo.png\" style=\"margin-left: -338px;\" align=left hspace=12></p>\r\n" + 
							"\r\n" + 
							"<p class=MsoNormal style='margin-top:0cm;margin-right:0cm;margin-bottom:4.7pt;\r\n" + 
							"margin-left:320.0pt'><span style='font-size:10.0pt;line-height:107%;color:gray'><b>GST\r\n" + 
							"Reg No.: 20-0409359-E</b> </span></p>\r\n" + 
							"\r\n" + 
							"<p class=MsoNormal style='margin-top:0cm;margin-right:0cm;margin-bottom:0cm;\r\n" + 
							"margin-left:319.75pt;text-indent:-.5pt'><span style='font-size:8.0pt;\r\n" + 
							"line-height:107%;color:gray'>NTUC LEARNINGHUB PTE LTD</span></p>\r\n" + 
							"<p class=MsoNormal style='margin-top:0cm;margin-right:0cm;margin-bottom:0cm;\r\n" + 
							"margin-left:319.75pt;text-indent:-.5pt'><span style='font-size:8.0pt;\r\n" + 
							"line-height:107%;color:gray'>73 BRAS BASAH ROAD</span></p>\r\n" + 
							"\r\n" + 
							"<p class=MsoNormal style='margin-top:0cm;margin-right:0cm;margin-bottom:0cm;\r\n" + 
							"margin-left:319.75pt;text-indent:-.5pt'><span style='font-size:8.0pt;\r\n" + 
							"line-height:107%;color:gray'>#02-01 NTUC TRADE UNION HOUSE</span></p>\r\n" + 
							"\r\n" + 
							"<p class=MsoNormal style='margin-top:0cm;margin-right:63.05pt;margin-bottom:10.75pt;\r\n" + 
							"0cm;margin-left:319.75pt;text-indent:-.5pt'><span style='font-size:8.0pt;\r\n" + 
							"line-height:107%;color:gray'>SINGAPORE 189556\r\n" + 
							"</span></p>\r\n" + 
							"\r\n" + 
							"<p class=MsoNormal style='margin-top:0cm;margin-right:63.05pt;margin-bottom:\r\n" + 
							"0cm;margin-left:319.75pt;text-indent:-.5pt'>"
							+ ""
							+ "<span style='font-size:8.0pt;\r\n" + 
							"line-height:107%;color:gray'>\r\n" + 
							"www.ntuclearninghub.com</span>"
							+ ""
							+ ""
							+ "<br/><span style='font-size:8.0pt;\r\n" + 
							"line-height:107%;color:gray'>FAX 65 64867824\r\n" + 
							"</span></p>\r\n" + 
							"\r\n" + 
							"<p class=MsoNormal style='margin-top:0cm;margin-right:0cm;margin-bottom:19.6pt;\r\n" + 
							"margin-left:319.75pt;text-indent:-.5pt'><span style='font-size:8.0pt;\r\n" + 
							"line-height:107%;color:gray'>Company Registration Number: 200409359E</span></p>\r\n" + 
							"\r\n" + 
							"<table class=TableGrid border=0 cellspacing=0 cellpadding=0 width=687\r\n" + 
							" style='width:515.3pt;border-collapse:collapse'>\r\n" + 
							" <tr style='height:25.1pt'>\r\n" + 
							"  <td width=362 valign=top style='width:271.85pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:25.1pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:.7pt'><span style='font-size:10.0pt;\r\n" + 
							"  line-height:107%'><b>Bill-To:</b></span></p>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:2.65pt;'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>"+billingName+"</span></p>\r\n" + 
							"<p class=MsoNormal style='margin-top:0cm;margin-right:0cm;margin-bottom:2.65pt;\r\n" + 
							"margin-left:-.25pt;text-indent:-.5pt;line-height:110%'><span style='font-size:\r\n" + 
							"8.0pt;line-height:110%'>"+address+"</span></p>\r\n" + 
							"\r\n" + 
							"<p class=MsoNormal style='margin-top:0cm;margin-right:0cm;margin-bottom:2.65pt;\r\n" + 
							"margin-left:-.25pt;text-indent:-.5pt;line-height:110%'><span style='font-size:\r\n" + 
							"8.0pt;line-height:110%'>"+city + " " + zip+"</span></p>\r\n" + 
							"\r\n" + 
							"<p class=MsoNormal style='margin-top:0cm;margin-right:0cm;margin-bottom:2.65pt;\r\n" + 
							"margin-left:-.25pt;text-indent:-.5pt;line-height:110%'><span style='font-size:\r\n" + 
							"8.0pt;line-height:110%'>"+country+"</span></p>\r\n" + 
							"\r\n" + 
							"<p class=MsoNormal style='margin-top:0cm;margin-right:0cm;margin-bottom:28.75pt;\r\n" + 
							"margin-left:-.25pt;text-indent:-.5pt;line-height:110%'><span style='font-size:\r\n" + 
							"8.0pt;line-height:110%'>"+phone+"</span></p>\r\n" + 
							"\r\n" + 
							"<p class=MsoNormal style='margin-top:0cm;margin-right:0cm;margin-bottom:10.65pt;\r\n" + 
							"margin-left:-.25pt;text-indent:-.5pt;line-height:110%'><span style='font-size:\r\n" + 
							"8.0pt;line-height:110%'>Attn: "+user.getFullName()+"</span></p>" + 
							"" + 
							
							"  </td>\r\n" + 
							"  <td width=191 valign=top style='width:143.1pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:25.1pt'>\r\n" + 
							"  <p class=MsoNormal align=center style='margin-top:0cm;margin-right:1.9pt;\r\n" + 
							"  margin-bottom:2.85pt;margin-left:0cm;text-align:center'><span\r\n" + 
							"  style='font-size:8.0pt;line-height:107%'>Invoice No.</span></p>\r\n" + 
							"  <p class=MsoNormal align=center style='margin-top:0cm;margin-right:0cm;\r\n" + 
							"  margin-bottom:2.85pt;margin-left:2.2pt;text-align:center'><span\r\n" + 
							"  style='font-size:8.0pt;line-height:107%'>Invoice Date</span></p>\r\n" + 
							"  <p class=MsoNormal align=center style='margin-top:0cm;margin-right:0cm;\r\n" + 
							"  margin-bottom:2.85pt;margin-left:16pt;text-align:center'><span\r\n" + 
							"  style='font-size:8.0pt;line-height:107%'>Payment Method</span></p>\r\n" + 
							"  <p class=MsoNormal align=center style='margin-top:0cm;margin-right:0cm;\r\n" + 
							"  margin-bottom:0cm;margin-left:-22.2pt;text-align:center'><span\r\n" + 
							"  style='font-size:8.0pt;line-height:107%'>Term</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=30 valign=top style='width:22.5pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:25.1pt'>\r\n" + 
							"  <p class=MsoNormal align=center style='margin-top:0cm;margin-right:7.05pt;\r\n" + 
							" margin-bottom:2.85pt;margin-left:11.6pt;text-align:center'><span\r\n" + 
							"  style='font-size:8.0pt;line-height:107%'>:</span></p>\r\n" +
							"  <p class=MsoNormal align=center style='margin-top:0cm;margin-right:7.05pt;\r\n" + 
							" margin-bottom:2.85pt;margin-left:11.6pt;text-align:center'><span\r\n" + 
							"  style='font-size:8.0pt;line-height:107%'>:</span></p>\r\n" +
							"  <p class=MsoNormal align=center style='margin-top:0cm;margin-right:7.05pt;\r\n" + 
							" margin-bottom:2.85pt;margin-left:11.6pt;text-align:center'><span\r\n" + 
							"  style='font-size:8.0pt;line-height:107%'>:</span></p>\r\n" + 
							"  <p class=MsoNormal align=center style='margin-top:0cm;margin-right:7.05pt;\r\n" + 
							"  margin-bottom:2.85pt;margin-left:11.6pt;text-align:center'><span\r\n" + 
							"  style='font-size:8.0pt;line-height:107%'>:</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=104 valign=top style='width:80.8pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:25.1pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:2.85pt'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>"+invoiceNo+"</span></p>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:2.85pt;text-align:left;text-justify:\r\n" + 
							"  inter-ideograph'><span style='font-size:8.0pt;line-height:107%'>" +purchaseDate+ 
							"</span></p>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:2.85pt;text-align:justify;text-justify:\r\n" + 
							"  inter-ideograph'><span style='font-size:8.0pt;line-height:107%'>" +commerceOrder.getCommercePaymentMethodKey()+ 
							"</span></p>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:2.85pt;text-align:justify;text-justify:\r\n" + 
							"  inter-ideograph'><span style='text-transform:uppercase;font-size:8.0pt;line-height:107%'>" +CommerceOrderPaymentConstants.getOrderPaymentStatusLabel(commerceOrder.getPaymentStatus())+ 
							"</span></p>\r\n" + 
							"  </td>\r\n" + 
							" </tr>\r\n" + 
							"</table>\r\n" + 
							"\r\n" + 
						//cut
							"<table class=TableGrid border=0 cellspacing=0 cellpadding=0 width=686\r\n" + 
							" style='width:514.3pt;margin-left:.5pt;border-collapse:collapse'>\r\n" + 
							" <tr style='height:14.4pt'>\r\n" + 
							"  <td width=356 valign=top style='width:266.85pt;border:solid black 1.0pt;\r\n" + 
							"  padding:2.5pt 4.5pt 0cm 4.5pt;height:14.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>Item</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=51 valign=top style='width:38.45pt;border:solid black 1.0pt;\r\n" + 
							"  border-left:none;padding:2.5pt 4.5pt 0cm 4.5pt;height:14.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>SKU</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=28 valign=top style='width:20.9pt;border:solid black 1.0pt;\r\n" + 
							"  border-left:none;padding:2.5pt 4.5pt 0cm 4.5pt;height:14.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>Qty</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=58 valign=top style='width:43.25pt;border:solid black 1.0pt;\r\n" + 
							"  border-left:none;padding:2.5pt 4.5pt 0cm 4.5pt;height:14.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>Unit Price</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=54 valign=top style='width:40.5pt;border:solid black 1.0pt;\r\n" + 
							"  border-left:none;padding:2.5pt 4.5pt 0cm 4.5pt;height:14.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>Discount</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=56 valign=top style='width:41.65pt;border:solid black 1.0pt;\r\n" + 
							"  border-left:none;padding:2.5pt 4.5pt 0cm 4.5pt;height:14.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>Net Price</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=39 valign=top style='width:29.05pt;border:solid black 1.0pt;\r\n" + 
							"  border-left:none;padding:2.5pt 4.5pt 0cm 4.5pt;height:14.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>GST</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=45 valign=top style='width:33.55pt;border:solid black 1.0pt;\r\n" + 
							"  border-left:none;padding:2.5pt 4.5pt 0cm 4.5pt;height:14.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>Total</span></p>\r\n" + 
							"  </td>\r\n" + 
							" </tr>\r\n" + 
							" <tr style='height:14.4pt'>\r\n";
					
					if(discountTernary== null ||discountTernary.equals("")) {
						discountTernary="0";
					}
					if(discountFromCommerceOrderTernary==null ||discountFromCommerceOrderTernary.equals("")) {
						discountFromCommerceOrderTernary="0";
					}
					
					
					
					stringBuilderHtml1=stringBuilderHtml1+"  <td width=356 valign=top style='width:266.85pt;border:solid black 1.0pt;\r\n" + 
							"  border-top:none;padding:2.5pt 4.5pt 0cm 4.5pt;height:14.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>"+courseTitle+"</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=51 valign=top style='width:38.45pt;border-top:none;border-left:\r\n" + 
							"  none;border-bottom:solid black 1.0pt;border-right:solid black 1.0pt;\r\n" + 
							"  padding:2.5pt 4.5pt 0cm 4.5pt;height:14.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>"+sku+"</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=28 valign=top style='width:20.9pt;border-top:none;border-left:none;\r\n" + 
							"  border-bottom:solid black 1.0pt;border-right:solid black 1.0pt;padding:2.5pt 4.5pt 0cm 4.5pt;\r\n" + 
							"  height:14.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>"+quantity+"</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=58 valign=top style='width:43.25pt;border-top:none;border-left:\r\n" + 
							"  none;border-bottom:solid black 1.0pt;border-right:solid black 1.0pt;\r\n" + 
							"  padding:2.5pt 4.5pt 0cm 4.5pt;height:14.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>"+unitPrice+"</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=54 valign=top style='width:40.5pt;border-top:none;border-left:none;\r\n" + 
							"  border-bottom:solid black 1.0pt;border-right:solid black 1.0pt;padding:2.5pt 4.5pt 0cm 4.5pt;\r\n" + 
							"  height:14.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>"+discountTernary+"</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=56 valign=top style='width:41.65pt;border-top:none;border-left:\r\n" + 
							"  none;border-bottom:solid black 1.0pt;border-right:solid black 1.0pt;\r\n" + 
							"  padding:2.5pt 4.5pt 0cm 4.5pt;height:14.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>"+netPrice+"</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=39 valign=top style='width:29.05pt;border-top:none;border-left:\r\n" + 
							"  none;border-bottom:solid black 1.0pt;border-right:solid black 1.0pt;\r\n" + 
							"  padding:2.5pt 4.5pt 0cm 4.5pt;height:14.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>"+decimalFormat.format(Float.parseFloat(taxAmountPerProduct))+"</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=45 valign=top style='width:33.55pt;border-top:none;border-left:\r\n" + 
							"  none;border-bottom:solid black 1.0pt;border-right:solid black 1.0pt;\r\n" + 
							"  padding:2.5pt 4.5pt 0cm 4.5pt;height:14.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>"+totalEachProduct+"</span></p>\r\n" + 
							"  </td>\r\n";
					stringBuilderHtml1=stringBuilderHtml1+" </tr>\r\n" + 
							"</table>\r\n" + 
							"\r\n" + 
							"</div>\r\n" + 
							"\r\n" + 
							"<span style='font-size:11.0pt;line-height:107%;font-family:Roboto,sans-serif;\r\n" + 
							"color:black'><br clear=all style='page-break-before:auto'>\r\n" + 
							"</span>\r\n" + 
							"\r\n" + 
							"<div class=WordSection2>\r\n" + 
							"\r\n" + 
						
							"<p class=MsoNormal style='margin-top:0cm;margin-right:0cm;margin-bottom:2.65pt;\r\n" + 
							"margin-left:-.25pt;text-indent:-.5pt;line-height:110%'><span style='font-size:\r\n" + 
							"8.0pt;line-height:110%'>Full name per ID (NRIC/FIN/Passport):</span></p>\r\n" + 
							"\r\n" + 
							"<p class=MsoNormal style='margin-top:0cm;margin-right:0cm;margin-bottom: 28.95pt;\r\n" + 
							"margin-left:-.25pt;text-indent:-.5pt;line-height:110%'><span style='font-size:\r\n" + 
							"8.0pt;line-height:110%'>"+fullName+"</span></p>\r\n" +
														
							
							"\r\n" + 
							"<div style=\"float: left;\">\r\n" + 
							"<p class=MsoNormal style='margin-top:0cm;margin-right:0cm;margin-bottom:2.65pt;\r\n" + 
							"margin-left:-.25pt;text-indent:-.5pt;line-height:110%'><span style='font-size:\r\n" + 
							"8.0pt;line-height:110%'>"+String.format("%.0f", taxRateDecimalDynamic)+"% GST on SGD $"+subtotalUnitPrice+"</span></p>\r\n" + 
							"" + 
							"<p class=MsoNormal style='margin-top:0cm;margin-right:0cm;margin-bottom:2.65pt;\r\n" + 
							"margin-left:-.25pt;text-indent:-.5pt;line-height:110%'><span style='font-size:\r\n" + 
							"8.0pt;line-height:110%'>(GST calculation is based on full course fee and" + 
							"</span></p>" + 
							"" + 
							"<p class=MsoNormal style='margin-top:0cm;margin-right:0cm;margin-bottom:2.65pt;\r\n" + 
							"margin-left:-.25pt;text-indent:-.5pt;line-height:110%'><span style='font-size:\r\n" + 
							"8.0pt;line-height:110%'>" + 
							"services renderer)</span></p>" + 
							"</div>\r\n" + 
							"\r\n" + 
							"\r\n" + 
							"\r\n" + 
							"<div class=WordSection3 style=\"float: right;\">\r\n" + 
							"\r\n" + 
							"<table class=TableGrid border=0 cellspacing=0 cellpadding=0 width=257\r\n" + 
							" style='width:192.65pt;margin-left:109.2pt;border-collapse:collapse'>\r\n" + 
							" <tr style='height:11.4pt'>\r\n" + 
							"  <td width=179 valign=top style='width:134.0pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:11.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>Subtotal</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=37 valign=top style='width:27.6pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:11.4pt'>\r\n" + 
							"  <p class=MsoNormal align=center style='margin-top:0cm;margin-right:0cm;\r\n" + 
							"  margin-bottom:0cm;margin-left:9.65pt;text-align:center'><span\r\n" + 
							"  style='font-size:8.0pt;line-height:107%'>:</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=41 valign=top style='width:45.05pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:11.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm;text-align:justify;text-justify:\r\n" + 
							"  inter-ideograph'><span style='font-size:8.0pt;line-height:107%'>$ "+totalUnitPriceTernary+"</span></p>\r\n" + 
							"  </td>\r\n" + 
							" </tr>\r\n" + 
							" <tr style='height:11.4pt'>\r\n" + 
							"  <td width=179 valign=top style='width:134.0pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:11.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>Total Discount</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=37 valign=top style='width:27.6pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:11.4pt'>\r\n" + 
							"  <p class=MsoNormal align=center style='margin-top:0cm;margin-right:0cm;\r\n" + 
							"  margin-bottom:0cm;margin-left:9.65pt;text-align:center'><span\r\n" + 
							"  style='font-size:8.0pt;line-height:107%'>:</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=41 valign=top style='width:45.05pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:11.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm;text-align:justify;text-justify:\r\n" + 
							"  inter-ideograph'><span style='font-size:8.0pt;line-height:107%'>$ "+discountFromCommerceOrderTernary+"</span></p>\r\n" + 
							"  </td>\r\n" + 
							" </tr>\r\n" +
							" <tr style='height:13.4pt'>\r\n" + 
							"  <td width=179 valign=top style='width:134.0pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:13.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>Net Price</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=37 valign=top style='width:27.6pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:13.4pt'>\r\n" + 
							"  <p class=MsoNormal align=center style='margin-top:0cm;margin-right:0cm;\r\n" + 
							"  margin-bottom:0cm;margin-left:9.65pt;text-align:center'><span\r\n" + 
							"  style='font-size:8.0pt;line-height:107%'>:</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=41 valign=top style='width:35.05pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:13.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm;text-align:justify;text-justify:\r\n" + 
							"  inter-ideograph'><span style='font-size:8.0pt;line-height:107%'>$ "+subtotalUnitPriceTernary+"</span></p>\r\n" + 
							"  </td>\r\n" + 
							" </tr>\r\n" + 
							" <tr style='height:13.4pt'>\r\n" + 
							"  <td width=179 valign=top style='width:134.0pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:13.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>GST ("+String.format("%.0f", taxRateDecimalDynamic)+"%)</span></p>" + 
							"  </td>" + 
							"  <td width=37 valign=top style='width:27.6pt;padding:0cm 0cm 0cm 0cm;" + 
							"  height:13.4pt'>" + 
							"  <p class=MsoNormal align=center style='margin-top:0cm;margin-right:0cm;\r\n" + 
							"  margin-bottom:0cm;margin-left:9.65pt;text-align:center'><span\r\n" + 
							"  style='font-size:8.0pt;line-height:107%'>:</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=41 valign=top style='width:35.05pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:13.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm;text-align:justify;text-justify'>"
							+ "<span style='font-size:8.0pt;line-height:107%'>$ "+totalTaxAmountDecimalDynamic+"</span></p>\r\n" + 
							"  </td>\r\n" + 
							" </tr>\r\n" + 
							" <tr style='height:11.4pt'>\r\n" + 
							"  <td width=179 valign=top style='width:134.0pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:11.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>Shipping</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=37 valign=top style='width:27.6pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:11.4pt'>\r\n" + 
							"  <p class=MsoNormal align=center style='margin-top:0cm;margin-right:0cm;\r\n" + 
							"  margin-bottom:0cm;margin-left:9.65pt;text-align:center'><span\r\n" + 
							"  style='font-size:8.0pt;line-height:107%'>:</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=41 valign=top style='width:45.05pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:11.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm;text-align:justify;text-justify:\r\n" + 
							"  inter-ideograph'><span style='font-size:8.0pt;line-height:107%'>$ "+shippingAmountTernary+"</span></p>\r\n" + 
							"  </td>\r\n" + 
							" </tr>\r\n" +
							" <tr style='height:11.4pt'>\r\n" + 
							"  <td width=179 valign=top style='width:134.0pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:11.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm'><span style='font-size:8.0pt;\r\n" + 
							"  line-height:107%'>Amount Payable</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=37 valign=top style='width:27.6pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:11.4pt'>\r\n" + 
							"  <p class=MsoNormal align=center style='margin-top:0cm;margin-right:0cm;\r\n" + 
							"  margin-bottom:0cm;margin-left:9.65pt;text-align:center'><span\r\n" + 
							"  style='font-size:8.0pt;line-height:107%'>:</span></p>\r\n" + 
							"  </td>\r\n" + 
							"  <td width=41 valign=top style='width:35.05pt;padding:0cm 0cm 0cm 0cm;\r\n" + 
							"  height:11.4pt'>\r\n" + 
							"  <p class=MsoNormal style='margin-bottom:0cm;text-align:justify;text-justify:\r\n" + 
							"  inter-ideograph'><span style='font-size:8.0pt;line-height:107%'>$ "+amountPayable+"</span></p>\r\n" + 
							"  </td>\r\n" + 
							" </tr>\r\n" + 
							"</table>\r\n" + 
							"\r\n" + 
							"<h1 align=left style='margin-left:0cm;margin-top: 350px;text-align:left'><span style='font-size:\r\n" + 
							"10.0pt;line-height:107%;color:black'><b>NTUC LearningHub</b></span></h1>\r\n" + 
							"\r\n" + 
							"</div>\r\n" + 
							"\r\n" + 
							"</body>\r\n" + 
							"\r\n" + 
							"</html>\r\n" + 
							"";

					//log.info("final string is:::::::::a::::::::::"+stringBuilderHtml1.toString());
//					HtmlConverter.convertToPdf(stringBuilderHtml1, new FileOutputStream(ntucPdfPath+date+"_.pdf"));
					
					//new code start
					
//					PdfReader pdfReader = new PdfReader(ntucPdfPath+date+"_.pdf");
			        WriterProperties writerProperties = new WriterProperties();
			      //Generate password string
			        String passwordInstruction = "";
			        if(accType == 2) {
						String companyCode = (String) account2.getExpandoBridge().getAttribute("Company Code");
						log.info(" company code: "+companyCode);
						String UEN = (String) account2.getExpandoBridge().getAttribute("UEN Number");
						log.info("UEN: "+UEN);
						if(companyCode!=null && companyCode.trim().length()>0 && UEN!=null && UEN.trim().length()>0) {
							String subUEN = UEN.substring(UEN.length()-4);
							passwordInstruction = companyCode+subUEN.toUpperCase();
						}
			        }
			        else {
			        	SimpleDateFormat sdfExpandoDob = new SimpleDateFormat("ddMMyyyy");
						String dob = sdfExpandoDob.format(user.getBirthday());
						String nric = (String) account2.getExpandoBridge().getAttribute("NRIC");
						passwordInstruction = dob+nric.toUpperCase();
			        }			        
					log.info("password instruction 17042024: "+passwordInstruction);
			        writerProperties.setStandardEncryption(passwordInstruction.getBytes(), "NTUC1@123".getBytes(), EncryptionConstants.ALLOW_PRINTING, EncryptionConstants.ENCRYPTION_AES_128);
			        PdfWriter pdfWriter2 = new PdfWriter(new FileOutputStream(ntucPdfPath+date+".pdf"), writerProperties);
			        
			        HtmlConverter.convertToPdf(stringBuilderHtml1, pdfWriter2);
//			        PdfDocument pdfDocument = new PdfDocument(pdfReader, pdfWriter2);
//			        pdfDocument.close();
				        
						//new code start

					java.nio.file.Path pdfPath = Paths.get(ntucPdfPath+date+".pdf");
					byte[] pdfBytes = Files.readAllBytes(pdfPath);
					base64Data = java.util.Base64.getEncoder().encodeToString(pdfBytes);

		}

		catch (Exception e) {
			e.printStackTrace();
			log.error("Error while senda email test: "+e.getMessage());

		}
		return base64Data;
	}

}