package br.unitins.tp1.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.ws.rs.BadRequestException;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Capacete extends DefaultEntity {

    private String modelo;
    private BigDecimal preco;
    @Column(name = "idTamanho")
    private Tamanho tamanho;
    private Integer quantidadeEstoque;

    // agregacao
    @JoinColumn (name = "id_marca", nullable = false)
    @ManyToOne
    private Marca marca;

    @JoinColumn (name = "id_categoria", nullable = false)
    @ManyToOne
    private Categoria categoria;

    @JoinColumn (name = "id_material", nullable = false)
    @ManyToOne
    private Material material;

    @JoinColumn (name = "id_cor", nullable = false)
    @ManyToOne
    private Cor cor;

    // composicao
    @JoinColumn (name = "id_especificacao", nullable = false, unique = true)
    @OneToOne (cascade = CascadeType.ALL, orphanRemoval = true)
    private EspecificacaoCapacete especificacao;

    // relacionamento N:N
    @ManyToMany
    @JoinTable (name = "capacete_fornecedor",
        joinColumns = @JoinColumn(name = "id_capacete"),
        inverseJoinColumns = @JoinColumn(name = "id_fornecedor"))
    private List<Fornecedor> fornecedores;

    public void atualizarEstoque(Integer qtd) {
        int novaQuantidade = (quantidadeEstoque == null ? 0 : quantidadeEstoque) + qtd;
        if (novaQuantidade < 0) {
            throw new BadRequestException("Estoque insuficiente.");
        }
        this.quantidadeEstoque = novaQuantidade;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Tamanho getTamanho() {
        return tamanho;
    }

    public void setTamanho(Tamanho tamanho) {
        this.tamanho = tamanho;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

    public EspecificacaoCapacete getEspecificacao() {
        return especificacao;
    }

    public void setEspecificacao(EspecificacaoCapacete especificacao) {
        this.especificacao = especificacao;
    }

    public List<Fornecedor> getFornecedores() {
        return fornecedores;
    }

    public void setFornecedores(List<Fornecedor> fornecedores) {
        this.fornecedores = fornecedores;
    }

}
