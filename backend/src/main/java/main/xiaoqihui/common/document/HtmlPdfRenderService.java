package main.xiaoqihui.common.document;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import main.xiaoqihui.common.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;

@Service
public class HtmlPdfRenderService {

    public byte[] render(String html) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            
            File fontFile = new File("C:/Windows/Fonts/msyh.ttc");
            if (fontFile.exists()) {
                builder.useFont(fontFile, "Microsoft YaHei");
            } else {
                File fallbackFont = new File("C:/Windows/Fonts/simsun.ttc");
                if (fallbackFont.exists()) {
                    builder.useFont(fallbackFont, "Microsoft YaHei");
                }
            }
            
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new BusinessException(7001, "PDF generation failed");
        }
    }
}
