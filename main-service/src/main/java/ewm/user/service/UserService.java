package ewm.user.service;

import ewm.user.dto.NewUserRequest;
import ewm.user.dto.UserDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(Integer from, Integer size, List<Long> ids);

    UserDto postUser(@Valid NewUserRequest request);

    void deleteUser(@Positive @NotNull Long itemId);
}
