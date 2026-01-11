import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "CRUD-операции над пользователями")
public class UserController {

    private final UserService userService;
    private final UserModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Получить список всех пользователей")
    public CollectionModel<EntityModel<UserDto>> getAll() {
        List<EntityModel<UserDto>> models = userService.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(models,
                linkTo(methodOn(UserController.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Найти пользователя по id")
    @ApiResponse(responseCode = "200",
            content = @Content(schema = @Schema(implementation = UserDto.class)))
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public EntityModel<UserDto> getById(@PathVariable Long id) {
        UserDto dto = userService.findById(id);
        return assembler.toModel(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать пользователя")
    public EntityModel<UserDto> create(@Valid @RequestBody UserDto dto) {
        UserDto saved = userService.save(dto);
        return assembler.toModel(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя")
    public EntityModel<UserDto> update(@PathVariable Long id,
                                       @Valid @RequestBody UserDto dto) {
        UserDto updated = userService.update(id, dto);
        return assembler.toModel(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить пользователя")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}