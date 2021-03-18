package util;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PDFHandler {

    private PDFHandler(){}

    //Returns the .pdf filename in the folder or finishes the program
    public static String findPdfFile(String path) {
        File directory = new File(path);

        // store all names with same name
        // with/without extension
        String[] flist = directory.list(new PDFFilter());

        if (flist == null || flist.length == 0) {
            System.out.println("Não há arquivo .pdf na pasta.");
            System.exit(0);
        }

        if (flist.length > 1) {
            System.out.println("Mais de um arquivo .pdf na pasta.");
            System.exit(0);
        }

        return flist[0];
    }

    public static PDDocument loadFile(String directory) {
        PDDocument document = null;
        try {
            document = PDDocument.load(new File(directory + findPdfFile(directory)));
        } catch (IOException e) {
            System.out.println("Não é um arquivo PDF ou está corrompido.");
            System.exit(0);
        }

        return document;
    }

    public static List<PDDocument> splitPDF(PDDocument document) throws IOException {
        Splitter splitter = new Splitter();
        return splitter.split(document);
    }
}
