package br.unitins.tp1.resource;

import br.unitins.tp1.dto.CorDTO;
import br.unitins.tp1.dto.CorResponseDTO;
import br.unitins.tp1.model.Cor;
import br.unitins.tp1.service.CorService;
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

@Path("/cores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CorResource {

    @Inject
    CorService service;

    @GET
    public Response listar() {
        return Response.ok(service.findAll().stream()
                .map(CorResponseDTO::fromEntity)
                .toList()).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok(CorResponseDTO.fromEntity(service.findById(id))).build();
    }

    @GET
    @Path("/search")
    public Response buscarPorNome(@QueryParam("nome") String nome) {
        return Response.ok(service.findByNome(nome).stream().map(CorResponseDTO::fromEntity).toList()).build();
    }

    @POST
    public Response inserir(@Valid CorDTO dto) {
        Cor cor = new Cor();
        cor.setNome(dto.nome());
        cor.setCodigoHexadecimal(dto.codigoHexadecimal());
        return Response.status(Response.Status.CREATED).entity(CorResponseDTO.fromEntity(service.create(cor))).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, @Valid CorDTO dto) {
        Cor cor = new Cor();
        cor.setNome(dto.nome());
        cor.setCodigoHexadecimal(dto.codigoHexadecimal());

        service.update(id, cor);
        return Response.noContent().build();

    }

    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }

}
