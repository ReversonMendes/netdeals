package br.com.reverson.netdeal.controller;

import br.com.reverson.netdeal.dto.ColaboradorDto;
import br.com.reverson.netdeal.service.ColaboradorService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/colaboradores")
public class ColaboradorController {
    @Autowired
    private ColaboradorService colaboradorService;

    @GetMapping
    public List<ColaboradorDto> listColaborador() {
        return colaboradorService.findAll();
    }

    @PostMapping
    public ResponseEntity<ColaboradorDto> registerColaborador(@RequestBody ColaboradorDto colaboradorDto, UriComponentsBuilder uriComponentsBuilder){
       // colaboradorDto = validarSenha(colaboradorDto);
        ColaboradorDto colaboradorDtoRegister = colaboradorService.createColaborador(colaboradorDto);
        URI cargo = uriComponentsBuilder.path("/colaboradores/{id}").buildAndExpand(colaboradorDtoRegister.getId()).toUri();
        return ResponseEntity.created(cargo).body(colaboradorDtoRegister);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ColaboradorDto> deleteColaborador(@PathVariable @NotNull Long id){
        colaboradorService.deleteColaborador(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColaboradorDto> updateColaborador(@PathVariable @NotNull Long id, @RequestBody ColaboradorDto colaboradorDto){
        ColaboradorDto colaboradorDtoUpdate = colaboradorService.updateColaborador(id, colaboradorDto);
        return ResponseEntity.ok(colaboradorDtoUpdate);
    }


}
