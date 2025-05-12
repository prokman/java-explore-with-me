package ewm.user.service;

import ewm.exceptions.DuplicateDataException;
import ewm.user.dto.NewUserRequest;
import ewm.user.dto.UserDto;
import ewm.user.dto.UserMapper;
import ewm.user.model.User;
import ewm.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(Integer from, Integer size, List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            Pageable pageable = getPageable(from,size);
            List<User> users = userRepository.findAll(pageable).toList();
            return users
                    .stream()
                    .map(UserMapper::userToDto)
                    .toList();
        } else {
            Pageable pageable = getPageable(from,size);
            List<User> users = userRepository.findUserById(ids, pageable);
            return users
                    .stream()
                    .map(UserMapper::userToDto)
                    .toList();
        }
    }

    @Override
    @Transactional
    public UserDto postUser(NewUserRequest request) {
        User user;
        try {
            user = userRepository.save(UserMapper.requestToUser(request));
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMessage().contains("email_uq")) {
                throw new DuplicateDataException("Email is busy");
            }
            throw ex;
        }
        return UserMapper.userToDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long itemId) {
        userRepository.deleteById(itemId);
    }

    private Pageable getPageable(Integer from, Integer size) {
        if (from == null) {
            from = 0;
        }
        if (size == null) {
            size = 10;
        }
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);
        return pageable;
    }
}
