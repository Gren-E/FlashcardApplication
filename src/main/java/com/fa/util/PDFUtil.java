package com.fa.util;

import org.apache.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class PDFUtil {

    private static final Logger LOG = Logger.getLogger(PDFUtil.class);

    public static boolean isExistingPDFFile(File file) {
        return file.exists() && file.getName().endsWith(".pdf");
    }

    public static int countPages(File pdfFile) {
        if (!isExistingPDFFile(pdfFile)) {
            LOG.error(String.format("No such .pdf file: %s", pdfFile.getAbsolutePath()));
            return -1;
        }

        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            return document.getNumberOfPages();
        } catch (IOException e) {
            LOG.error("Could not read the document.", e);
            return -1;
        }
    }

    public static Image loadImage(File pdfFile, int numberOfOrder, boolean averse) {
        if (!isExistingPDFFile(pdfFile)) {
            LOG.error(String.format("No such .pdf file: %s", pdfFile.getAbsolutePath()));
            return null;
        }

        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            PDFRenderer renderer = new PDFRenderer(document);
            int pageNumber = numberOfOrder * 2 - (averse ? 2 : 1);
            return renderer.renderImageWithDPI(pageNumber, 100, ImageType.RGB);
        } catch (Exception e) {
            LOG.error("Could not load the image.", e);
            return null;
        }
    }

}
