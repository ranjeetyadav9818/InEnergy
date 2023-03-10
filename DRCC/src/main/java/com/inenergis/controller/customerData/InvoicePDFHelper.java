package com.inenergis.controller.customerData;

import com.inenergis.entity.billing.Invoice;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;

import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.component.export.PDFOptions;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Getter
@Setter
public class InvoicePDFHelper {

    private Invoice selectedInvoice;

    public InvoicePDFHelper(Invoice selectedInvoice) {
        this.selectedInvoice = selectedInvoice;
    }

    public PDFOptions createPDFOptionsForInvoice() {
        PDFOptions pdfOpt = new PDFOptions();
        pdfOpt.setFacetBgColor("#E0E0E0");
        pdfOpt.setFacetFontColor("#212121");
        pdfOpt.setFacetFontStyle("BOLD");
        pdfOpt.setCellFontSize("12");
        return pdfOpt;
    }

    public void preProcessInvoicePDF(Object document) throws IOException, BadElementException, DocumentException {

        Document pdf = (Document) document;
        pdf.open();
        pdf.setPageSize(PageSize.A4);

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String logo = externalContext.getRealPath("") + File.separator + "resources" + File.separator + "images" + File.separator + "Inenergis_Logo.png";
        final Image img = Image.getInstance(logo);
        img.scaleAbsolute(80, 80);
        pdf.add(img);

        Paragraph totalsParagraph = new Paragraph("Invoice number #" + selectedInvoice.getInvoiceName() + " detail and totals:");
        pdf.add(totalsParagraph);
        pdf.add(new Paragraph(" ")); //Empty one
        pdf.add(new Paragraph(" ")); //Empty one

    }
}
