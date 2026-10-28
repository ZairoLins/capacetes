package br.unitins.tp1.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.unitins.tp1.dto.CapaceteAbertoDTO;
import br.unitins.tp1.dto.CorDTO;
import br.unitins.tp1.dto.EspecificacaoCapaceteDTO;
import br.unitins.tp1.exception.ProblemDetails.Violation;
import br.unitins.tp1.resource.CapaceteAbertoResource;
import br.unitins.tp1.resource.CorResource;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.executable.ExecutableValidator;
import jakarta.ws.rs.core.UriInfo;

/**
 * Usa violacoes reais, geradas validando os parametros dos metodos dos
 * resources (como o Quarkus faz), para conferir o nome de campo devolvido.
 */
@ExtendWith(MockitoExtension.class)
class ConstraintViolationExceptionMapperTest {

    private static final ExecutableValidator VALIDATOR = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator()
            .forExecutables();

    @Mock
    UriInfo uriInfo;

    ConstraintViolationExceptionMapper mapper;

    @BeforeEach
    void setUp() {
        when(uriInfo.getPath()).thenReturn("/recurso");
        mapper = new ConstraintViolationExceptionMapper();
        mapper.uriInfo = uriInfo;
    }

    private static <T> ConstraintViolationException validar(T resource, String metodo, Class<?> tipoParametro,
            Object argumento) throws NoSuchMethodException {
        Method method = resource.getClass().getMethod(metodo, tipoParametro);
        return new ConstraintViolationException(
                new HashSet<>(VALIDATOR.validateParameters(resource, method, new Object[] { argumento })));
    }

    private ProblemDetails mapear(ConstraintViolationException e) {
        return (ProblemDetails) mapper.toResponse(e).getEntity();
    }

    @Test
    void retorna400ComTipoDeValidacao() throws Exception {
        ProblemDetails problem = mapear(validar(new CorResource(), "inserir", CorDTO.class, new CorDTO("Preto", "FFF")));

        assertEquals(400, problem.status());
        assertEquals(URI.create("/problems/validation-error"), problem.type());
        assertEquals("Um ou mais campos possuem valores invalidos.", problem.detail());
        assertEquals(URI.create("/recurso"), problem.instance());
    }

    @Test
    void campoDoCorpoApareceSemOMetodoEOParametro() throws Exception {
        // propertyPath original: inserir.dto.codigoHexadecimal
        ProblemDetails problem = mapear(validar(new CorResource(), "inserir", CorDTO.class, new CorDTO("Preto", "FFF")));

        assertEquals(List.of(new Violation("codigoHexadecimal", "O codigo hexadecimal deve estar no formato #RRGGBB.")),
                problem.violations());
    }

    @Test
    void violacoesSaoOrdenadasPorCampoEMensagem() throws Exception {
        ProblemDetails problem = mapear(validar(new CorResource(), "inserir", CorDTO.class, new CorDTO(" ", null)));

        assertEquals(List.of(
                new Violation("codigoHexadecimal", "O codigo hexadecimal deve ser informado."),
                new Violation("nome", "O nome deve ser informado."),
                new Violation("nome", "O nome deve ter entre 2 e 60 caracteres.")), problem.violations());
    }

    @Test
    void parametroDeConsultaUsaONomeDoParametro() throws Exception {
        // propertyPath original: buscarPorNome.nome
        ProblemDetails problem = mapear(validar(new CorResource(), "buscarPorNome", String.class, null));

        assertEquals(List.of(new Violation("nome", "O parametro de busca deve ser informado.")), problem.violations());
    }

    @Test
    void campoAninhadoEItemDeListaMantemOCaminho() throws Exception {
        CapaceteAbertoDTO dto = new CapaceteAbertoDTO("Jet Classic", new BigDecimal("899.90"), 3, 10, 1L, 1L, 1L, 1L,
                new EspecificacaoCapaceteDTO(new BigDecimal("9"), 1), Arrays.asList(1L, -2L), true);

        ProblemDetails problem = mapear(validar(new CapaceteAbertoResource(), "inserir", CapaceteAbertoDTO.class, dto));

        assertEquals(List.of(
                new Violation("especificacao.peso", "O peso deve ser de no maximo 3 kg."),
                new Violation("idFornecedores[1]", "Fornecedor invalido.")), problem.violations());
    }
}
