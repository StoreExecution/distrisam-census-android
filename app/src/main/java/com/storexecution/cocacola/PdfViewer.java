package com.storexecution.cocacola;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PdfViewer extends AppCompatActivity {
    /**
     * ButterKnife Code
     **/
    @BindView(R.id.pdfView)
    com.github.barteksc.pdfviewer.PDFView pdfView;

    /**
     * ButterKnife Code
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        ButterKnife.bind(this);
        String path = "";
        Bundle b = getIntent().getExtras();
        if (b != null) {
            String source = b.getString("source", "");
            if (source.equals("fridge"))
                path = "pdf/fridge.pdf";
            else if (source.equals("plve"))
                path = "pdf/plve.pdf";
            else if (source.equals("plvi"))
                path = "pdf/plvi.pdf";
        }
        if (path.length() > 0)
            pdfView.fromAsset(path)
                    .pages(0) // all pages are displayed by default
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .load();

    }

    @OnClick(R.id.btClose)
    public void close() {
        PdfViewer.this.finish();
    }
}
