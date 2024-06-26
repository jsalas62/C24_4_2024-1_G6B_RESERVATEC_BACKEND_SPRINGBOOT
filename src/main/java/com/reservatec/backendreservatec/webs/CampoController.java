package com.reservatec.backendreservatec.webs;

import com.reservatec.backendreservatec.entities.Campo;
import com.reservatec.backendreservatec.services.CampoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/campo")
@CrossOrigin(origins = "*")
public class CampoController {

    private final CampoService campoService;

    @Autowired
    public CampoController(CampoService campoService) {
        this.campoService = campoService;
    }

    @GetMapping
    public ResponseEntity<List<Campo>> getAllCampos() {
        List<Campo> campos = campoService.findAllCampos();
        return ResponseEntity.ok(campos);
    }
}
