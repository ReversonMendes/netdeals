package br.com.reverson.netdeal.service.serviceImpl;

import br.com.reverson.netdeal.dto.ColaboradorDto;
import br.com.reverson.netdeal.model.Colaborador;
import br.com.reverson.netdeal.model.ForcaSenha;
import br.com.reverson.netdeal.repository.ColaboradorRepository;
import br.com.reverson.netdeal.service.ColaboradorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColaboradorServiceImpl implements ColaboradorService {

    private PasswordEncoder passwordEncoder;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ColaboradorServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<ColaboradorDto> findAll() {
        return colaboradorRepository
                .findAll().stream().map(p -> modelMapper.map(p, ColaboradorDto.class)).collect(Collectors.toList());
    }


    @Override
    public ColaboradorDto createColaborador(ColaboradorDto dto) {

        dto = scoreSenha(dto);
        dto.setSenha(passwordEncoder.encode(dto.getSenha()));

        Colaborador colaborador = modelMapper.map(dto, br.com.reverson.netdeal.model.Colaborador.class);
        colaboradorRepository.save(colaborador);
        return modelMapper.map(colaborador, ColaboradorDto.class);
    }

    @Override
    public ColaboradorDto updateColaborador(Long id, ColaboradorDto dto) {

        dto = scoreSenha(dto);
        dto.setSenha(passwordEncoder.encode(dto.getSenha()));

        Colaborador colaborador = modelMapper.map(dto, Colaborador.class);
        colaborador.setId(id);
        colaborador = colaboradorRepository.save(colaborador);
        return modelMapper.map(colaborador, ColaboradorDto.class);
    }
    @Override
    public void deleteColaborador(Long id) {
        colaboradorRepository.deleteById(id);
    }

    private ColaboradorDto scoreSenha(ColaboradorDto colaboradorDto){
        int numero = 0;
        int caracterEspecial = 0;
        int minuscula = 0;
        int maiuscula = 0;
        int requisito;
        int midCaracteres = 0;
        int upperConsecutiva = 0;
        int lowerConsecutiva = 0;
        int numeroConsecutivo = 0;


        double score = 0.0;
        String senha = colaboradorDto.getSenha();
        int len = senha.length();

        for(int i = 0; i < len; i++){
            char s = senha.charAt(i);
            if (Character.isDigit(s)) {
                numero ++;

                if(i > 0){
                    if(Character.isDigit(senha.charAt(i-1)))
                        numeroConsecutivo++;
                }

                if(i > 0 && i < len-1){
                    midCaracteres ++;
                }
            } else if (Character. isLetter(s)) {
                if(Character.isUpperCase(s)) {
                    maiuscula++;
                    if(i > 0) {
                        if (Character.isUpperCase(senha.charAt(i - 1)))
                            upperConsecutiva++;
                    }

                }else{
                    minuscula ++;
                    if(i > 0) {
                        if (Character.isLowerCase(senha.charAt(i - 1)))
                            lowerConsecutiva++;
                    }

                }
            } else {
                caracterEspecial++;
                if(i > 0 && i < len-1){
                    midCaracteres ++;
                }
            }
        }

        requisito = contemMaisTresRequisitos(maiuscula, minuscula, numero, caracterEspecial);

        int upper  = maiuscula > 0 ?  (len - minuscula) : 0;
        int lower  = minuscula > 0 ? (len - minuscula) : 0;

        //Adição
        score = ((len * 4) + (numero * 4) + (caracterEspecial * 6) + ( upper * 2) + ( lower * 2) + (midCaracteres * 2) + (requisito * 2));

        //somente numero
        if(numero == len) {
            score = score - len;
        }

        //somente letra
        if((maiuscula + minuscula) == len){
            score = score - len;
        }

        //Letras maiúsculas consecutivas
        if(upperConsecutiva > 0 ){
            score = score - (upperConsecutiva * 2);
        }

        //Letras minúsculas consecutivas
        if(lowerConsecutiva > 0 ){
            score = score - (lowerConsecutiva * 2);
        }

        //Numeros  consecutiv0s
        if(numeroConsecutivo > 0 ) {
            score = score - (numeroConsecutivo * 2);
        }

        score = score - (letrasSequenciais(senha) * 3);
        score = score - (numeroSequenciais(senha) * 3);

        if(score > 100)
            score = 100;
        if(score < 0)
            score = 0;

        if(score<=30){
            colaboradorDto.setForca(ForcaSenha.MUITO_FRACO);
        } else if(score >=31 && score<=60){
            colaboradorDto.setForca(ForcaSenha.FRACO);
        }else if(score >=61 && score<=80){
            colaboradorDto.setForca(ForcaSenha.BOM);
        }else if(score >=81 && score<=90){
            colaboradorDto.setForca(ForcaSenha.FORTE);
        } else {
            colaboradorDto.setForca(ForcaSenha.MUITO_FORTE);
        }

        colaboradorDto.setScore(score);

        return colaboradorDto;
    }

    private int contemMaisTresRequisitos(int maiuscula, int minuscula,  int numero, int caracterEspecial){
        int requisito = 0;
        //contém 3 dos 4 requisitos
        if(maiuscula > 0)
            requisito++;

        if(minuscula > 0)
            requisito++;

        if(numero > 0)
            requisito++;

        if(caracterEspecial > 0)
            requisito++;

        if(requisito >=3 ){
            return 4;
        }else{
            return 0;
        }
    }

    private int letrasSequenciais(String senha){
        String valida = "";
        int contador = 0;

        String sequencia = "abcdefghijklmnopqrstuvwxyz";

        if(senha.length() < 3){
            return 0;
        }

        for(int i = 0; i < senha.length(); i++) {
            char s = senha.charAt(i);

            if(Character.isLetter(s)){
                if(i+3 <= senha.length()){
                    valida = senha.substring(i, (i+3));
                    if(sequencia.contains(valida)){
                        contador++;
                    }
                }

            }
        }

        return contador;
    }

    private int numeroSequenciais(String senha){
        String valida = "";
        int contador = 0;

        String sequencia = "123456789";

        if(senha.length() < 3){
            return 0;
        }

        for(int i = 0; i < senha.length(); i++) {
            char s = senha.charAt(i);

            if(Character.isDigit(s)){
                if(i+3 <= senha.length()){
                    valida = senha.substring(i, (i+3));
                    if(sequencia.contains(valida)){
                        contador++;
                    }
                }

            }
        }

        return contador;
    }

    private int simbolosSequenciais(String senha){
        //TODO
        return 0;
    }
}
