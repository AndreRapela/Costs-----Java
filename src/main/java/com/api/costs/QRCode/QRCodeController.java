package com.api.costs.QRCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/qrcode")
public class QRCodeController {
    
    @Autowired
    private QRCodeService service;
    
    @PostMapping("/gerar")
    public ResponseEntity<byte[]> gerarQrCode(@RequestBody Map<String, String> request) throws Exception {
        String texto = request.get("texto");
        byte[] qrCode = service.gerar(texto);
        
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(qrCode);
    }
}
