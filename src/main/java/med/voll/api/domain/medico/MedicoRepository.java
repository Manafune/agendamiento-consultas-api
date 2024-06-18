package med.voll.api.domain.medico;

import med.voll.api.domain.medico.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Page<Medico> findByActivoTrue(Pageable paginacion);

    @Query("""
        select m from Medico m 
        where m.activo=1 and 
        m.especialidad=:especialidad and 
        m.id not in(
            select c.medico.id from Consulta c 
            c.fecha=:fecha
        )
        order by rand()
        limit 1
    """)
    Medico seleccionarMedicoConEspecialidadEnFecha(Especialidad especialidad, LocalDateTime fecha);
}
