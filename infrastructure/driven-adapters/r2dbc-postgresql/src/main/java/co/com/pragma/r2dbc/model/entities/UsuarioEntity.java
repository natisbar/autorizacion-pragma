package co.com.pragma.r2dbc.model.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(schema= "public", name = "usuario")
public class UsuarioEntity {
    @Id
    @Column("id")
    private Long id;
    @Column("nombres")
    private String nombres;
    @Column("apellidos")
    private String apellidos;
    @Column("fecha_nacimiento")
    private LocalDate fechaNacimiento;
    @Column("documento_identidad")
    private String identificacion;
    @Column("direccion")
    private String direccion;
    @Column("telefono")
    private String telefono;
    @Column("email")
    private String correoElectronico;
    @Column("salario_base")
    private BigDecimal salarioBase;
}
