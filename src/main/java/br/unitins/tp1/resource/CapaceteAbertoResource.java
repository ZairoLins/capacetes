package br.unitins.tp1.resource;

import java.util.ArrayList;
import java.util.List;

import br.unitins.tp1.dto.CapaceteAbertoDTO;
import br.unitins.tp1.dto.CapaceteAbertoResponseDTO;
import br.unitins.tp1.model.CapaceteAberto;
import br.unitins.tp1.model.EspecificacaoCapacete;
import br.unitins.tp1.model.Fornecedor;
import br.unitins.tp1.model.Tamanho;
import br.unitins.tp1.model.TipoFechamento;
import br.unitins.tp1.service.CapaceteAbertoService;
import br.unitins.tp1.service.CategoriaService;
import br.unitins.tp1.service.CorService;
import br.unitins.tp1.service.FornecedorService;
import br.unitins.tp1.service.MarcaService;
import br.unitins.tp1.service.MaterialService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/capacetes-abertos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CapaceteAbertoResource {

    @Inject
    CapaceteAbertoService service;

    @Inject
    MarcaService marcaService;

    @Inject
    CategoriaService categoriaService;

    @Inject
    MaterialService materialService;

    @Inject
    CorService corService;

    @Inject
    FornecedorService fornecedorService;

    @GET
    public Response listar() {
        return Response.ok(service.findAll().stream()
                .map(CapaceteAbertoResponseDTO::fromEntity)
                .toList()).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok(CapaceteAbertoResponseDTO.fromEntity(service.findById(id))).build();
    }

    @GET
    @Path("/search")
    public Response buscarPorModelo(@QueryParam("modelo") String modelo) {
        return Response.ok(service.findByModelo(modelo).stream().map(CapaceteAbertoResponseDTO::fromEntity).toList()).build();
    }

    @POST
    public Response inserir(@Valid CapaceteAbertoDTO dto) {
        CapaceteAberto capaceteAberto = new CapaceteAberto();
        capaceteAberto.setModelo(dto.modelo());
        capaceteAberto.setPreco(dto.preco());
        capaceteAberto.setTamanho(Tamanho.fromId(dto.idTamanho()));
        capaceteAberto.setQuantidadeEstoque(dto.quantidadeEstoque());
        capaceteAberto.setMarca(marcaService.findById(dto.idMarca()));
        capaceteAberto.setCategoria(categoriaService.findById(dto.idCategoria()));
        capaceteAberto.setMaterial(materialService.findById(dto.idMaterial()));
        capaceteAberto.setCor(corService.findById(dto.idCor()));

        EspecificacaoCapacete especificacao = new EspecificacaoCapacete();
        especificacao.setPeso(dto.especificacao().peso());
        especificacao.setTipoFechamento(TipoFechamento.fromId(dto.especificacao().idTipoFechamento()));
        capaceteAberto.setEspecificacao(especificacao);

        List<Fornecedor> fornecedores = new ArrayList<>();
        if (dto.idFornecedores() != null) {
            for (Long idFornecedor : dto.idFornecedores()) {
                fornecedores.add(fornecedorService.findById(idFornecedor));
            }
        }
        capaceteAberto.setFornecedores(fornecedores);

        capaceteAberto.setPossuiViseira(dto.possuiViseira());
        return Response.status(Response.Status.CREATED).entity(CapaceteAbertoResponseDTO.fromEntity(service.create(capaceteAberto))).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, @Valid CapaceteAbertoDTO dto) {
        CapaceteAberto capaceteAberto = new CapaceteAberto();
        capaceteAberto.setModelo(dto.modelo());
        capaceteAberto.setPreco(dto.preco());
        capaceteAberto.setTamanho(Tamanho.fromId(dto.idTamanho()));
        capaceteAberto.setQuantidadeEstoque(dto.quantidadeEstoque());
        capaceteAberto.setMarca(marcaService.findById(dto.idMarca()));
        capaceteAberto.setCategoria(categoriaService.findById(dto.idCategoria()));
        capaceteAberto.setMaterial(materialService.findById(dto.idMaterial()));
        capaceteAberto.setCor(corService.findById(dto.idCor()));

        EspecificacaoCapacete especificacao = new EspecificacaoCapacete();
        especificacao.setPeso(dto.especificacao().peso());
        especificacao.setTipoFechamento(TipoFechamento.fromId(dto.especificacao().idTipoFechamento()));
        capaceteAberto.setEspecificacao(especificacao);

        List<Fornecedor> fornecedores = new ArrayList<>();
        if (dto.idFornecedores() != null) {
            for (Long idFornecedor : dto.idFornecedores()) {
                fornecedores.add(fornecedorService.findById(idFornecedor));
            }
        }
        capaceteAberto.setFornecedores(fornecedores);

        capaceteAberto.setPossuiViseira(dto.possuiViseira());

        service.update(id, capaceteAberto);
        return Response.noContent().build();

    }

    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }

}
