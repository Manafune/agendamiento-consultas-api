package med.voll.api.domain.consulta;

import med.voll.api.domain.consulta.validaciones.HorarioDeAnticipacion;
import med.voll.api.domain.consulta.validaciones.ValidadorCancelamientoConsultas;
import med.voll.api.domain.consulta.validaciones.ValidadorDeConsultas;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errores.ValidacionDeIntregridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultaService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    List<ValidadorDeConsultas> validadores;

    @Autowired
    List<ValidadorCancelamientoConsultas> validadoresCancelamiento;

    public DatosDetalleConsulta agendar(DatosAgendarConsulta datos){

        if(datos.idPaciente()!=null && !pacienteRepository.findById(datos.idPaciente()).isPresent()){
            throw new ValidacionDeIntregridad("Este id para el paciente no fue encontrado");
        }
        
        if(datos.idMedico()!=null && !medicoRepository.existsById(datos.idMedico())){
            throw new ValidacionDeIntregridad("El id para el medico no fue encontrado");
        }

        //validaciones
        validadores.forEach(v -> v.validar(datos));


        var paciente = pacienteRepository.findById(datos.idPaciente()).get();

        var medico = seleccionarMedico(datos);

        if(medico == null){
            throw new ValidacionDeIntregridad("No existen Medicos disponibles para este horario y especialidad");
        }

        var consulta = new Consulta(medico,paciente,datos.fecha());

        consultaRepository.save(consulta);

        return new DatosDetalleConsulta(consulta);
    }

    private Medico seleccionarMedico(DatosAgendarConsulta datos) {
        if(datos.idMedico()!=null){
            return medicoRepository.getReferenceById(datos.idMedico());
        }
        if(datos.especialidad()==null){
            throw new ValidacionDeIntregridad("Debe seleccionarse una especialidad para el medico");
        }

        return medicoRepository.seleccionarMedicoConEspecialidadEnFecha(datos.especialidad(),datos.fecha());
    }

    public void cancelar(DatosCancelamientoConsulta datos) {
        if(!consultaRepository.existsById(datos.idConsulta())){
            throw new ValidacionDeIntregridad("Id de la consulta informado no existe!");
        }

        validadoresCancelamiento.forEach(v -> v.validar(datos));

        var consulta = consultaRepository.getReferenceById(datos.idConsulta());
        consulta.cancelar(datos.motivo());
    }
}
