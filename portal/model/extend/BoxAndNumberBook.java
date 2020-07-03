package com.dxn.model.extend;

import com.dxn.model.entity.BoxAndNumberBookEntity;
import com.dxn.system.Framework;
import com.dxn.system.ResponseEntity;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.configuration.DxnConfig;
import com.dxn.system.io.FileHelper;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "Prj_BoxAndNumberBook")
public class BoxAndNumberBook extends BoxAndNumberBookEntity {

    @SystemService(args = "boxAndNumberBookIdList,attachmentId")
    public ResponseEntity printLabelPaper(List<String> boxAndNumberBookIdList, String attachmentId) throws Exception {

        Attachment attachment = Attachment.findById(Long.parseLong(attachmentId));

        Path path = Paths.get(DxnConfig.getFileUploadPath(), attachment.getPath(), attachment.getMd5() + attachment.getSuffix());
        Path path2 = Paths.get(DxnConfig.getFileUploadPath(), attachment.getPath(), Framework.getMD5(Framework.getRandom() + "") + attachment.getSuffix());
        File file = path.toFile();
        File file2 = path2.toFile();
        InputStream ins = new FileInputStream(file);
        HSSFWorkbook wb = new HSSFWorkbook(ins);
        HSSFSheet sheet = wb.getSheetAt(0);
        float heightInPoints = 0.0f;
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            if (sheet.getRow(i) != null) {
                heightInPoints += sheet.getRow(i).getHeightInPoints();
            }
        }
        int num = (int) (780 / heightInPoints);

        for (int index = 0; index < boxAndNumberBookIdList.size(); index++) {
            BoxAndNumberBook boxAndNumberBook = BoxAndNumberBook.findById(Long.parseLong(boxAndNumberBookIdList.get(index)));
            int rows = sheet.getPhysicalNumberOfRows();

            if (index > 0) {
                if ((index + 1) % num == 0) {
                    copyRows(1, 14, rows + 1, sheet, boxAndNumberBook);
                    sheet.createRow(sheet.getPhysicalNumberOfRows() + 1).setHeightInPoints(15.75f);
                    sheet.createRow(sheet.getPhysicalNumberOfRows() + 1).setHeightInPoints(780.0f - heightInPoints * num);

                } else {
                    sheet.createRow(sheet.getPhysicalNumberOfRows() + 1).setHeightInPoints(15.75f);
                    copyRows(1, 14, sheet.getPhysicalNumberOfRows() + 1, sheet, boxAndNumberBook);
                    sheet.createRow(sheet.getPhysicalNumberOfRows() + 1).setHeightInPoints(15.75f);
                }
            }
            if (index == boxAndNumberBookIdList.size() - 1) {
                replectExcelData(sheet, BoxAndNumberBook.findById(Long.parseLong(boxAndNumberBookIdList.get(0))));
            }
        }
        wb.setPrintArea(0, 0, 4, 0, sheet.getPhysicalNumberOfRows());
        OutputStream fileOutputStream = new FileOutputStream(file2);
        wb.write(fileOutputStream);
        String ss = excelToHtml(file2);
        fileOutputStream.close();
        ResponseEntity response = new ResponseEntity();
        response.setCode(200);
        response.setData(ss);
        FileHelper.deleteFile(file2.getPath());
        return response;
    }


    private String excelToHtml(File excelFile) throws Exception {
        InputStream is = null;
        OutputStream out = null;
        StringWriter writer = null;
        String content = null;
        try {
            if (excelFile.exists()) {

                is = new FileInputStream(excelFile);
//                Workbook workBook = WorkbookFactory.create(is);
                HSSFWorkbook workBook = new HSSFWorkbook(is);
                ExcelToHtmlConverter converter = new ExcelToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
                //设置不输出行号(1 2 3...)及列标(A B C...)等
                converter.setOutputColumnHeaders(false);
                converter.setOutputHiddenColumns(false);
                converter.setOutputColumnHeaders(false);
                converter.setOutputLeadingSpacesAsNonBreaking(false);
                converter.setOutputRowNumbers(false);
                converter.processWorkbook(workBook);

                writer = new StringWriter();
                Transformer serializer = TransformerFactory.newInstance().newTransformer();
                serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                serializer.setOutputProperty(OutputKeys.INDENT, "YES");
                serializer.setOutputProperty(OutputKeys.METHOD, "HTML");
                serializer.transform(
                        new DOMSource(converter.getDocument()),
                        new StreamResult(writer));
//                out = new FileOutputStream(htmlFile);
                content = writer.toString();
                //替换掉Sheet1 Sheet2 Sheet3...
                // String split = "(.{1})";
                content = content.replaceAll("<h2.*h2>", "")
                        .replaceAll("&lt;/br&gt; ", "</br>");

            }
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return content;
    }

    private void copyRows(int startRow, int endRow, int pPosition, HSSFSheet sheet, BoxAndNumberBook boxAndNumberBook) {
        int pstartrow = startRow - 1;
        int pendrow = endRow - 1;
        int targetrowfrom;
        int targetrowto;
        int columncount;
        int i;
        int j;
        if (pstartrow == -1 || pendrow == -1) {
            return;
        }
// 拷贝合并的单元格
        CellRangeAddress region = null;
        for (i = 0; i < sheet.getNumMergedRegions(); i++) {
            region = sheet.getMergedRegion(i);
            if ((region.getFirstRow() >= pstartrow) && (region.getLastRow() <= pendrow)) {
                targetrowfrom = region.getFirstRow() - pstartrow + pPosition;
                targetrowto = region.getLastRow() - pstartrow + pPosition;
                CellRangeAddress newregion = region.copy();
                newregion.setFirstRow(targetrowfrom);
                newregion.setFirstColumn(region.getFirstColumn());
                newregion.setLastRow(targetrowto);
                newregion.setLastColumn(region.getLastColumn());
                sheet.addMergedRegion(newregion);
            }
        }
// 设置列宽
        for (i = pstartrow; i <= pendrow; i++) {
            HSSFRow sourcerow = sheet.getRow(i);
            columncount = sourcerow.getLastCellNum();
            if (sourcerow != null) {
                HSSFRow newrow = sheet.createRow(pPosition - pstartrow + i);
                newrow.setHeight(sourcerow.getHeight());
                for (j = 0; j < columncount; j++) {
                    HSSFCell templatecell = sourcerow.getCell(j);
                    if (templatecell != null) {
                        HSSFCell newcell = newrow.createCell(j);
                        copyCell(templatecell, newcell, boxAndNumberBook);
                    }
                }
            }
        }
    }

    private void copyCell(HSSFCell srcCell, HSSFCell distCell, BoxAndNumberBook boxAndNumberBook) {
        distCell.setCellStyle(srcCell.getCellStyle());
        if (srcCell.getCellComment() != null) {
            distCell.setCellComment(srcCell.getCellComment());
        }
        CellType srcCellType = srcCell.getCellTypeEnum();
        distCell.setCellType(srcCellType);
        String compostionCustomerName = boxAndNumberBook.getManuScirptItemArchive().getCompostionCustomer().getName();
        if (srcCellType.equals(CellType.NUMERIC)) {
            if (HSSFDateUtil.isCellDateFormatted(srcCell)) {
                distCell.setCellValue(srcCell.getDateCellValue());
            } else {
                distCell.setCellValue(srcCell.getNumericCellValue());
            }
        } else if (srcCellType.equals(CellType.STRING)) {
            if ("ManuScirptItemArchiveId_ReportNumber".equals(srcCell.getRichStringCellValue().getString())) {
                distCell.setCellValue(boxAndNumberBook.getManuScirptItemArchive().getReportNumber());
            } else if ("createDate".equals(srcCell.getRichStringCellValue().getString())) {
                distCell.setCellValue(boxAndNumberBook.getManuScirptItemArchive().getCreatedOn().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            } else if ("ManuScirptItemArchiveId_CompostionCustomerId_Name".equals(srcCell.getRichStringCellValue().getString())) {
                distCell.setCellValue(compostionCustomerName);
            } else if ("bookNumber".equals(srcCell.getRichStringCellValue().getString())) {
                distCell.setCellValue(boxAndNumberBook.getBookRange() + "/共" + boxAndNumberBook.getManuScirptItemArchive().getBookNumber() + "册");
            } else if ("boxNumber".equals(srcCell.getRichStringCellValue().getString())) {
                distCell.setCellValue(boxAndNumberBook.getBox() + "/共" + boxAndNumberBook.getManuScirptItemArchive().getBoxNumber() + "盒");
            } else {
                distCell.setCellValue(srcCell.getRichStringCellValue());
            }
        }  else if (srcCellType.equals(CellType.BOOLEAN)) {
            distCell.setCellValue(srcCell.getBooleanCellValue());
        } else if (srcCellType.equals(CellType.ERROR)) {
            distCell.setCellErrorValue(srcCell.getErrorCellValue());
        } else if (srcCellType.equals(CellType.FORMULA)) {
            replaceExpression(srcCell, distCell, boxAndNumberBook, compostionCustomerName);
        }
    }
    private void replaceExpression(HSSFCell srcCell, HSSFCell distCell, BoxAndNumberBook boxAndNumberBook, String compostionCustomerName) {
        if ("C3".equals(srcCell.getCellFormula())) {
            if (distCell.getColumnIndex() == 2) {
                distCell.setCellValue(compostionCustomerName);
            } else {
                String split = "(.{1})";
                String reName = compostionCustomerName.replaceAll(split, "$1</br> ");
                distCell.setCellValue(reName);
            }
        } else if ("C2".equals(srcCell.getCellFormula())) {
            distCell.setCellValue(boxAndNumberBook.getManuScirptItemArchive().getReportNumber());
        } else if ("C8".equals(srcCell.getCellFormula())) {
            distCell.setCellValue(boxAndNumberBook.getBookRange() + "/共" + boxAndNumberBook.getManuScirptItemArchive().getBookNumber() + "册");
        } else if ("C9".equals(srcCell.getCellFormula())) {
            distCell.setCellValue(boxAndNumberBook.getBox() + "/共" + boxAndNumberBook.getManuScirptItemArchive().getBoxNumber() + "盒");
        }
    }

    public void replectExcelData(HSSFSheet sheet, BoxAndNumberBook boxAndNumberBook) {
        String compostionCustomerName = boxAndNumberBook.getManuScirptItemArchive().getCompostionCustomer().getName();
        for (int rowNumber = 0; rowNumber < 14; rowNumber++) {
            for (int cellNumber = 0; cellNumber < sheet.getLastRowNum(); cellNumber++) {
                HSSFCell cell = sheet.getRow(rowNumber).getCell(cellNumber);
                if (cell != null) {
                    CellType srcCellType = cell.getCellTypeEnum();

                    if (srcCellType.equals(CellType.STRING)) {
                        if ("ManuScirptItemArchiveId_ReportNumber".equals(cell.getRichStringCellValue().getString())) {
                            cell.setCellValue(boxAndNumberBook.getManuScirptItemArchive().getReportNumber());
                        } else if ("createDate".equals(cell.getRichStringCellValue().getString())) {
                            cell.setCellValue(boxAndNumberBook.getManuScirptItemArchive().getCreatedOn().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
                        } else if ("ManuScirptItemArchiveId_CompostionCustomerId_Name".equals(cell.getRichStringCellValue().getString())) {
                            cell.setCellValue(compostionCustomerName);
                        } else if ("bookNumber".equals(cell.getRichStringCellValue().getString())) {
                            cell.setCellValue(boxAndNumberBook.getBookRange() + "/共" + boxAndNumberBook.getManuScirptItemArchive().getBookNumber() + "册");
                        } else if ("boxNumber".equals(cell.getRichStringCellValue().getString())) {
                            cell.setCellValue(boxAndNumberBook.getBox() + "/共" + boxAndNumberBook.getManuScirptItemArchive().getBoxNumber() + "盒");
                        }

                    } else if (srcCellType.equals(CellType.FORMULA)) {
                        replaceExpression(cell, cell, boxAndNumberBook, compostionCustomerName);
                    }
                }
            }
        }
    }
}
