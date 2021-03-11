package util;

import java.io.File;
import java.io.FilenameFilter;

public class FiltroPDF implements FilenameFilter {

    public boolean accept(File dir, String name) {
        return name.endsWith(".pdf");
    }
}
