package br.com.tech.challenge.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.imaging.ImageFormats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QRCodeUtils {

    private QRCodeUtils(){}

    private static final Logger logger = LoggerFactory.getLogger(QRCodeUtils.class);

    public static byte[] generateQrCode(final String qrCodeContent, final int width, final int height) {
        try {
            final QRCodeWriter qrCodeWriter = new QRCodeWriter();
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeContent, BarcodeFormat.QR_CODE, width, height);
            MatrixToImageWriter.writeToStream(bitMatrix, ImageFormats.PNG.getName().toUpperCase(), byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        } catch (WriterException | IOException e) {
            logger.error(e.getMessage(), e);
        }
        return new byte[0];
    }
}
