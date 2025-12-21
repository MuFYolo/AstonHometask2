import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Min(value = 18, message = "User must be adult")
    private Integer age;
}