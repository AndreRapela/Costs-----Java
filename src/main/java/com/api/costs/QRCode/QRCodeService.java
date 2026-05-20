package com.api.costs.QRCode;

import org.springframework.stereotype.Service;

@Service
public class QRCodeService {
    private QRCodeGenerator gerador = new QRCodeGenerator();
    
    public byte[] gerar(String texto) throws Exception {
        return gerador.gerarQrCodePNG(texto, 10);
    }
}
