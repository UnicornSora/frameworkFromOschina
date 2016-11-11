package com.base.utils;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;
import java.util.Map;

/**
 * @author: football98
 * @createTime:  16-9-28
 * @classDescription: IReport视图处理扩展
 */
public class ApplicationIReportView extends JasperReportsMultiFormatView {

    private JasperReport jasperReport;

    public ApplicationIReportView() {
        super();
    }

    protected JasperPrint fillReport(Map<String, Object> model) throws Exception {
        if (model.containsKey("url")) {
            setUrl(String.valueOf(model.get("url")));
            this.jasperReport = loadReport();
        }
        return super.fillReport(model);
    }

    protected JasperReport getReport() {
        return this.jasperReport;
    }
}
