package main.xiaoqihui.common.document;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import main.xiaoqihui.common.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class HtmlPdfRenderService {

    public byte[] render(String html) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new BusinessException(7001, "PDF 生成失败");
        }
    }
}
