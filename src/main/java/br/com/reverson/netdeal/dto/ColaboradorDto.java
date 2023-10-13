package br.com.reverson.netdeal.dto;

import br.com.reverson.netdeal.model.ForcaSenha;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ColaboradorDto {

    private Long id;
    private String nome;
    private int nivel;
    private String senha;
    private Double score;
    private ForcaSenha forca;
}
