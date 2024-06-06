package lepdv.todolistweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "user", callSuper = false)
@ToString(exclude = "user")
@Builder
@FieldNameConstants
@Entity
@Table(name = "task")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Task extends BaseEntity<Long> {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{description.can.not.be.empty}")
    private String description;

    private LocalDate dateOfCreation;

    @FutureOrPresent(message = "{due.date.can.not.be.in.past}")
    private LocalDate dueDate;

    private String isCompleted;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @CreatedBy
    @NotAudited
    protected String createdBy;

    @LastModifiedBy
    private String modifiedBy;

}
