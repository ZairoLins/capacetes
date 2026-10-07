package br.unitins.tp1.resource;

import java.util.ArrayList;
import java.util.List;

import br.unitins.tp1.dto.CapaceteIntegralDTO;
import br.unitins.tp1.dto.CapaceteIntegralResponseDTO;
import br.unitins.tp1.model.CapaceteIntegral;
import br.unitins.tp1.model.EspecificacaoCapacete;
import br.unitins.tp1.model.Fornecedor;
import br.unitins.tp1.model.Tamanho;
import br.unitins.tp1.model.TipoFechamento;
import br.unitins.tp1.service.CapaceteIntegralService;
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

@Path("/capacetes-integrais")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CapaceteIntegralResource {

    @Inject
    CapaceteIntegralService service;

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
                .map(CapaceteIntegralResponseDTO::fromEntity)
                .toList()).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok(CapaceteIntegralResponseDTO.fromEntity(service.findById(id))).build();
    }

    @GET
    @Path("/search")
    public Response buscarPorModelo(@QueryParam("modelo") String modelo) {
        return Response.ok(service.findByModelo(modelo).stream().map(CapaceteIntegralResponseDTO::fromEntity).toList()).build();
    }

    @POST
    public Response inserir(@Valid CapaceteIntegralDTO dto) {
        CapaceteIntegral capaceteIntegral = new CapaceteIntegral();
        capaceteIntegral.setModelo(dto.modelo());
        capaceteIntegral.setPreco(dto.preco());
        capaceteIntegral.setTamanho(Tamanho.fromId(dto.idTamanho()));
        capaceteIntegral.setQuantidadeEstoque(dto.quantidadeEstoque());
        capaceteIntegral.setMarca(marcaService.findById(dto.idMarca()));
        capaceteIntegral.setCategoria(categoriaService.findById(dto.idCategoria()));
        capaceteIntegral.setMaterial(materialService.findById(dto.idMaterial()));
        capaceteIntegral.setCor(corService.findById(dto.idCor()));

        EspecificacaoCapacete especificacao = new EspecificacaoCapacete();
        especificacao.setPeso(dto.especificacao().peso());
        especificacao.setTipoFechamento(TipoFechamento.fromId(dto.especificacao().idTipoFechamento()));
        capaceteIntegral.setEspecificacao(especificacao);

        List<Fornecedor> fornecedores = new ArrayList<>();
        if (dto.idFornecedores() != null) {
            for (Long idFornecedor : dto.idFornecedores()) {
                fornecedores.add(fornecedorService.findById(idFornecedor));
            }
        }
        capaceteIntegral.setFornecedores(fornecedores);

        capaceteIntegral.setPossuiViseiraSolar(dto.possuiViseiraSolar());
        return Response.status(Response.Status.CREATED).entity(CapaceteIntegralResponseDTO.fromEntity(service.create(capaceteIntegral))).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, @Valid CapaceteIntegralDTO dto) {
        CapaceteIntegral capaceteIntegral = new CapaceteIntegral();
        capaceteIntegral.setModelo(dto.modelo());
        capaceteIntegral.setPreco(dto.preco());
        capaceteIntegral.setTamanho(Tamanho.fromId(dto.idTamanho()));
        capaceteIntegral.setQuantidadeEstoque(dto.quantidadeEstoque());
        capaceteIntegral.setMarca(marcaService.findById(dto.idMarca()));
        capaceteIntegral.setCategoria(categoriaService.findById(dto.idCategoria()));
        capaceteIntegral.setMaterial(materialService.findById(dto.idMaterial()));
        capaceteIntegral.setCor(corService.findById(dto.idCor()));

        EspecificacaoCapacete especificacao = new EspecificacaoCapacete();
        especificacao.setPeso(dto.especificacao().peso());
        especificacao.setTipoFechamento(TipoFechamento.fromId(dto.especificacao().idTipoFechamento()));
        capaceteIntegral.setEspecificacao(especificacao);

        List<Fornecedor> fornecedores = new ArrayList<>();
        if (dto.idFornecedores() != null) {
            for (Long idFornecedor : dto.idFornecedores()) {
                fornecedores.add(fornecedorService.findById(idFornecedor));
            }
        }
        capaceteIntegral.setFornecedores(fornecedores);

        capaceteIntegral.setPossuiViseiraSolar(dto.possuiViseiraSolar());

        service.update(id, capaceteIntegral);
        return Response.noContent().build();

    }

    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }

}
