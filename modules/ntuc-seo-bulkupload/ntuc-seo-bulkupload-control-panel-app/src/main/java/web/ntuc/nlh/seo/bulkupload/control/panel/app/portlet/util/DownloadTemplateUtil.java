package web.ntuc.nlh.seo.bulkupload.control.panel.app.portlet.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Sagar
 * The type Download template util.
 */
public class DownloadTemplateUtil {
    /**
     * Read write excel byte [ ].
     *
     * @param ios        the ios
     * @param headerList the header list
     * @return the byte [ ]
     */
    public static byte[] readWriteExcel(InputStream ios, List<String> headerList) {
        byte[] outArray = null;
        try (ByteArrayOutputStream outByteStream = new ByteArrayOutputStream()) {
            Workbook wb = getWorkbook(ios);
            if (Validator.isNotNull(wb)) {
                short s = 0;
                Sheet excelSheet = wb.getSheetAt(0); // Getting the sheet at 0th position
                Font headerFont = wb.getFontAt(s);
                headerFont.setBold(true);
                CellStyle headerCellStyle = wb.getCellStyleAt(0);
                headerCellStyle.setFont(headerFont);
                Row firstRow = excelSheet.rowIterator().next();
                if (Validator.isNotNull(firstRow)) {
                    int lastColumn = firstRow.getLastCellNum();
                    int totalColumns = lastColumn + headerList.size();
                    for (int i = lastColumn; i < totalColumns; i++) {
                        Cell headerCell = firstRow.createCell(i);
                        headerCell.setCellValue(headerList.get(i - lastColumn));
                        headerCell.setCellStyle(headerCellStyle);
                    }

                    for (int i = lastColumn; i < totalColumns; i++) {
                        excelSheet.autoSizeColumn(i);
                    }

                    wb.write(outByteStream);
                    outArray = outByteStream.toByteArray();
                }
            }
        } catch (Exception e) {
            _log.error("Error in read  write excel : " + e);
        } finally {
            if (Validator.isNotNull(ios)) {
                try {
                    ios.close();
                } catch (Exception e) {
                    _log.error("Error in closing inputstream object : " + e);
                }
            }
        }

        return outArray;
    }

    private static Workbook getWorkbook(InputStream inputStream) throws IOException {
        return new XSSFWorkbook(inputStream);
    }

    private static final Log _log = LogFactoryUtil.getLog(DownloadTemplateUtil.class.getName());
}
