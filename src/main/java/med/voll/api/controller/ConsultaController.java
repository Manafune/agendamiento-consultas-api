package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.consulta.AgendaDeConsultaService;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import med.voll.api.domain.consulta.DatosCancelamientoConsulta;
import med.voll.api.domain.consulta.DatosDetalleConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private AgendaDeConsultaService service;


    @PostMapping
    @Transactional
    public ResponseEntity agendar(@RequestBody @Valid DatosAgendarConsulta datos){
        var response = service.agendar(datos);

        return ResponseEntity.ok(response);
    }

//    @DeleteMapping
//    @Transactional
//    public ResponseEntity cancelar(@RequestBody @Valid DatosCancelamientoConsulta datos){
//        service.cancelar(datos);
//        return ResponseEntity.noContent().build();
//    }
}
