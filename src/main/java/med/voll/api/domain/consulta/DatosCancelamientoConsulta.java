package med.voll.api.domain.consulta;

import jakarta.validation.constraints.NotNull;

public record DatosCancelamientoConsulta(
        Long idConsulta,
        @NotNull
        MotivoCancelamiento motivo

) {
}
