package com.skillforge.skillforge_api.service;

import com.skillforge.skillforge_api.dto.mapper.BioMapper;
import com.skillforge.skillforge_api.dto.mapper.UserMapper;
import com.skillforge.skillforge_api.dto.request.UserCreateRequest;
import com.skillforge.skillforge_api.dto.request.UserUpdateReq;
import com.skillforge.skillforge_api.dto.response.BioDTO;
import com.skillforge.skillforge_api.dto.response.ResultPaginationDTO;
import com.skillforge.skillforge_api.dto.response.UserDTO;
import com.skillforge.skillforge_api.entity.User;
import com.skillforge.skillforge_api.repository.CourseRepository;
import com.skillforge.skillforge_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final BioMapper bioMapper;
    private final CourseRepository courseRepository;




    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, RoleService roleService, BioMapper bioMapper, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.bioMapper = bioMapper;
        this.courseRepository = courseRepository;
    }

    public ResultPaginationDTO getAllUsersFromDatabase(Specification<User> spec, Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(spec,pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber()+ 1); // Spring Data JPA uses 0-based index
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(usersPage.getTotalPages());
        meta.setTotalItems(usersPage.getTotalElements());

        result.setMeta(meta);

        List<UserDTO> listUsers = usersPage.getContent().stream()
                .map(userMapper::toDto)
                .toList();
        result.setResults(listUsers);

        return result;
    }

    public User fetchUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    @Transactional
    public UserDTO handleCreateUser(UserCreateRequest request) {
        User user = userMapper.toEntity(request);
        // Mã hóa mật khẩu trước khi lưu
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String hashed = user.getPassword();

        // check role
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(roleService.getDefaultRolesForUser());
        }

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }


    @Transactional
    public UserDTO handleUpdateUser(UserUpdateReq request) {
        User currentUser = fetchUserById(request.getId());
        if (currentUser == null) {
            throw new EntityNotFoundException("User not found with id: " + request.getId());
        }
        currentUser = userMapper.updateEntity(currentUser, request);

        userRepository.save(currentUser);
        UserDTO userDTO = userMapper.toDto(currentUser);

        return userDTO;
    }


    public void handleDeleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
    }

    public User handeFindByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow( () -> new EntityNotFoundException("User not found with email: " + email));

    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handeFindByEmail(email);
        if (currentUser == null) {
            throw new EntityNotFoundException("User not found with email: " + email);
        }
        currentUser.setRefreshToken(token);
        this.userRepository.save(currentUser);
    }


    public User findUserByTRefreshTokenAndEmail(String refreshToken, String email) {
        return userRepository.findByRefreshTokenAndEmail(refreshToken, email);
    }

    /**
     * cần cải tiến lại hàm này
     *  principal.toString() chưa chắc là email, nếu principal là UserDetails hoặc Jwt.
     *  Nên kiểm tra kết quả userRepository.findByEmail(...) có null không
      * @return
     */
    public User getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null || context.getAuthentication() == null || !context.getAuthentication().isAuthenticated()) {
            throw new UsernameNotFoundException("No authenticated user found");
        }

        Object principal = context.getAuthentication().getPrincipal();

        String email = null;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername(); // hoặc getEmail() nếu bạn custom UserDetails
        } else if (principal instanceof Jwt) {
            email = ((Jwt) principal).getClaimAsString("sub"); // nếu dùng JWT chứa email
        } else if (principal instanceof String && !"anonymousUser".equals(principal)) {
            email = principal.toString(); // fallback
        }

        if (email != null) {
            User user = this.handeFindByEmail(email);
            if (user != null) {
                return user;
            }
        }

        throw new UsernameNotFoundException("Cannot find current user from principal");
    }

    public User getUserByCourseId(Long courseId) {
        User user = fetchUserById(courseId);
        if (user == null) {
            throw new EntityNotFoundException("User not found with id: " + courseId);
        }
        return user;
    }


    public BioDTO getUserBioByCourseId(Long courseId) {
        Optional<User> userFromDb = courseRepository.findInstructorByCourseId(courseId);
        User user = userFromDb.isPresent() ? userFromDb.get() : null;
        return bioMapper.toBioDTO(user);
    }
}
