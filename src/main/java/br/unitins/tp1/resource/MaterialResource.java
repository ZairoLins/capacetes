package br.unitins.tp1.resource;

import br.unitins.tp1.dto.MaterialDTO;
import br.unitins.tp1.dto.MaterialResponseDTO;
import br.unitins.tp1.model.Material;
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

@Path("/materiais")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaterialResource {

    @Inject
    MaterialService service;

    @GET
    public Response listar() {
        return Response.ok(service.findAll().stream()
                .map(MaterialResponseDTO::fromEntity)
                .toList()).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok(MaterialResponseDTO.fromEntity(service.findById(id))).build();
    }

    @GET
    @Path("/search")
    public Response buscarPorNome(@QueryParam("nome") String nome) {
        return Response.ok(service.findByNome(nome).stream().map(MaterialResponseDTO::fromEntity).toList()).build();
    }

    @POST
    public Response inserir(@Valid MaterialDTO dto) {
        Material material = new Material();
        material.setNome(dto.nome());
        material.setDescricao(dto.descricao());
        return Response.status(Response.Status.CREATED).entity(MaterialResponseDTO.fromEntity(service.create(material))).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, @Valid MaterialDTO dto) {
        Material material = new Material();
        material.setNome(dto.nome());
        material.setDescricao(dto.descricao());

        service.update(id, material);
        return Response.noContent().build();

    }

    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }

}
