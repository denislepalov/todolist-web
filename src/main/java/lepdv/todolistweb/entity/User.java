package lepdv.todolistweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "todoList", callSuper = false)
@ToString(exclude = "todoList")
@Builder
@Entity
@Table(name = "users")
@Audited
public class User extends BaseEntity<Long> implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{username.can.not.be.empty}")
    @Size(min = 3, max = 100, message = "{username.should.be.from.3.to.100.symbols}")
    private String username;

    @NotBlank(message = "{password.can.not.be.empty}")
    @Size(min = 3, max = 100, message = "{password.should.be.from.3.to.100.symbols}")
    private String password;

    @Size(max = 100, message = "{full.name.can.not.be.more.than.100.symbols}")
    private String fullName;

    @PastOrPresent(message = "{date.of.birth.can.not.be.in.future}")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Boolean isNonLocked;

    @OneToMany(mappedBy = "user")
    @NotAudited
    private List<Task> todoList;

    private String image;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(getRole());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
