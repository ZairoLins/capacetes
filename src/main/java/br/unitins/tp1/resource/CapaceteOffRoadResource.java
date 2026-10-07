package br.unitins.tp1.resource;

import java.util.ArrayList;
import java.util.List;

import br.unitins.tp1.dto.CapaceteOffRoadDTO;
import br.unitins.tp1.dto.CapaceteOffRoadResponseDTO;
import br.unitins.tp1.model.CapaceteOffRoad;
import br.unitins.tp1.model.EspecificacaoCapacete;
import br.unitins.tp1.model.Fornecedor;
import br.unitins.tp1.model.Tamanho;
import br.unitins.tp1.model.TipoFechamento;
import br.unitins.tp1.service.CapaceteOffRoadService;
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

@Path("/capacetes-offroad")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CapaceteOffRoadResource {

    @Inject
    CapaceteOffRoadService service;

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
                .map(CapaceteOffRoadResponseDTO::fromEntity)
                .toList()).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok(CapaceteOffRoadResponseDTO.fromEntity(service.findById(id))).build();
    }

    @GET
    @Path("/search")
    public Response buscarPorModelo(@QueryParam("modelo") String modelo) {
        return Response.ok(service.findByModelo(modelo).stream().map(CapaceteOffRoadResponseDTO::fromEntity).toList()).build();
    }

    @POST
    public Response inserir(@Valid CapaceteOffRoadDTO dto) {
        CapaceteOffRoad capaceteOffRoad = new CapaceteOffRoad();
        capaceteOffRoad.setModelo(dto.modelo());
        capaceteOffRoad.setPreco(dto.preco());
        capaceteOffRoad.setTamanho(Tamanho.fromId(dto.idTamanho()));
        capaceteOffRoad.setQuantidadeEstoque(dto.quantidadeEstoque());
        capaceteOffRoad.setMarca(marcaService.findById(dto.idMarca()));
        capaceteOffRoad.setCategoria(categoriaService.findById(dto.idCategoria()));
        capaceteOffRoad.setMaterial(materialService.findById(dto.idMaterial()));
        capaceteOffRoad.setCor(corService.findById(dto.idCor()));

        EspecificacaoCapacete especificacao = new EspecificacaoCapacete();
        especificacao.setPeso(dto.especificacao().peso());
        especificacao.setTipoFechamento(TipoFechamento.fromId(dto.especificacao().idTipoFechamento()));
        capaceteOffRoad.setEspecificacao(especificacao);

        List<Fornecedor> fornecedores = new ArrayList<>();
        if (dto.idFornecedores() != null) {
            for (Long idFornecedor : dto.idFornecedores()) {
                fornecedores.add(fornecedorService.findById(idFornecedor));
            }
        }
        capaceteOffRoad.setFornecedores(fornecedores);

        capaceteOffRoad.setPossuiPala(dto.possuiPala());
        return Response.status(Response.Status.CREATED).entity(CapaceteOffRoadResponseDTO.fromEntity(service.create(capaceteOffRoad))).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, @Valid CapaceteOffRoadDTO dto) {
        CapaceteOffRoad capaceteOffRoad = new CapaceteOffRoad();
        capaceteOffRoad.setModelo(dto.modelo());
        capaceteOffRoad.setPreco(dto.preco());
        capaceteOffRoad.setTamanho(Tamanho.fromId(dto.idTamanho()));
        capaceteOffRoad.setQuantidadeEstoque(dto.quantidadeEstoque());
        capaceteOffRoad.setMarca(marcaService.findById(dto.idMarca()));
        capaceteOffRoad.setCategoria(categoriaService.findById(dto.idCategoria()));
        capaceteOffRoad.setMaterial(materialService.findById(dto.idMaterial()));
        capaceteOffRoad.setCor(corService.findById(dto.idCor()));

        EspecificacaoCapacete especificacao = new EspecificacaoCapacete();
        especificacao.setPeso(dto.especificacao().peso());
        especificacao.setTipoFechamento(TipoFechamento.fromId(dto.especificacao().idTipoFechamento()));
        capaceteOffRoad.setEspecificacao(especificacao);

        List<Fornecedor> fornecedores = new ArrayList<>();
        if (dto.idFornecedores() != null) {
            for (Long idFornecedor : dto.idFornecedores()) {
                fornecedores.add(fornecedorService.findById(idFornecedor));
            }
        }
        capaceteOffRoad.setFornecedores(fornecedores);

        capaceteOffRoad.setPossuiPala(dto.possuiPala());

        service.update(id, capaceteOffRoad);
        return Response.noContent().build();

    }

    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }

}
