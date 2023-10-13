package br.com.reverson.netdeal.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "colaboradores")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String nome;

    @Column(nullable = false)
    private int nivel;

    @Column(length = 100, nullable = false)
    private String senha;

    @Column(nullable = false)
    private Double score;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ForcaSenha forca;
}
