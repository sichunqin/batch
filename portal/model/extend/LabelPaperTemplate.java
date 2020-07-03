package com.dxn.model.extend;

import com.dxn.model.entity.LabelPaperTemplateEntity;
import com.dxn.system.annotation.SystemService;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

@Entity
@Table(name = "PM_LabelPaperTemplate")
public class LabelPaperTemplate extends LabelPaperTemplateEntity {


}
