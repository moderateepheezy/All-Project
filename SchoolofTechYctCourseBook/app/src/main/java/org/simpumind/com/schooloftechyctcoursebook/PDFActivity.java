package org.simpumind.com.schooloftechyctcoursebook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.joanzapata.pdfview.PDFView;
import static java.lang.String.format;

public class PDFActivity extends AppCompatActivity implements OnPageChangeListener{

    public static final String SAMPLE_FILE = "mtn112.pdf";

    public static final String ABOUT_FILE = "sta111.pdf";

    PDFView pdfView;

    String pdfName = SAMPLE_FILE;

    Integer pageNumber = 1;

    void afterViews() {
        display(pdfName, false);
    }

    public void about() {
        if (!displaying(ABOUT_FILE))
            display(ABOUT_FILE, true);
    }

    private void display(String assetFileName, boolean jumpToFirstPage) {
        if (jumpToFirstPage) pageNumber = 1;
        setTitle(pdfName = assetFileName);

        pdfView.fromAsset(assetFileName)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(format("%s %s / %s", pdfName, page, pageCount));
    }

    @Override
    public void onBackPressed() {
        if (ABOUT_FILE.equals(pdfName)) {
            display(SAMPLE_FILE, true);
        } else {
            super.onBackPressed();
        }
    }

    private boolean displaying(String fileName) {
        return fileName.equals(pdfName);
    }

    @Override   
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

}
