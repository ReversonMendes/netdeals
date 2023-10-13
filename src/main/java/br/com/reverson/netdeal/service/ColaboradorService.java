package br.com.reverson.netdeal.service;

import br.com.reverson.netdeal.dto.ColaboradorDto;

import java.util.List;

public interface ColaboradorService {

    List<ColaboradorDto> findAll();
    ColaboradorDto createColaborador(ColaboradorDto dto);
    ColaboradorDto updateColaborador(Long id, ColaboradorDto dto);
    void deleteColaborador(Long id);

}