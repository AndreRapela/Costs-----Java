package com.api.costs.QRCode;

import br.com.paulo.qrcode.QrCodeGenerator;
import br.com.paulo.qrcode.QrCode;
import br.com.paulo.qrcode.QrRenderer;
import br.com.paulo.qrcode.Ecc;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

public class QRCodeGenerator {
    
    private final QrCodeGenerator gerador = new QrCodeGenerator();
    
    public byte[] gerarQrCodePNG(String texto, int tamanho) throws Exception {
        QrCode qrCode = gerador.generate(texto, Ecc.MEDIUM);
        BufferedImage imagem = QrRenderer.render(qrCode.getModules(), tamanho);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(imagem, "PNG", baos);
        return baos.toByteArray();
    }

}
