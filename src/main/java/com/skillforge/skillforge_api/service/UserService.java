package com.skillforge.skillforge_api.service;

import com.skillforge.skillforge_api.entity.User;
import com.skillforge.skillforge_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;



    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsersFromDatabase() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public User fetchUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public User handleCreateUser(User user) {
        user.setCreatedAt(java.time.Instant.now());
        return userRepository.save(user);
    }


    @Transactional
    public User handleUpdateUser(User reqUser) {
        // 1. Lấy user hiện tại từ DB
        User currentUser = fetchUserById(reqUser.getId());
        if (currentUser == null) {
            throw new EntityNotFoundException("User not found with id: " + reqUser.getId());
        }

        // 2. Kiểm tra nếu email bị trùng với user khác
        boolean emailTaken = userRepository.existsByEmailAndIdNot(reqUser.getEmail(), reqUser.getId());
        if (emailTaken) {
            throw new IllegalArgumentException("Email đã được sử dụng bởi người dùng khác");
        }

        // 3. Cập nhật thông tin
        currentUser.setFullName(reqUser.getFullName());
        currentUser.setEmail(reqUser.getEmail());
        currentUser.setUsername(reqUser.getUsername());
        currentUser.setPassword(reqUser.getPassword());
        currentUser.setRole(reqUser.getRole());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("🧪 Auth: " + auth);

        // 4. Lưu lại
        return userRepository.save(currentUser);
    }


    public void handleDeleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
    }

    public User handeFindByEmail(String email) {
        return userRepository.findByEmail(email);

    }


    /**
     * cần cải tiến lại hàm này
     *  principal.toString() chưa chắc là email, nếu principal là UserDetails hoặc Jwt.
     *  Nên kiểm tra kết quả userRepository.findByEmail(...) có null không
      * @return
     */
        public User getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        com.skillforge.skillforge_api.entity.User user = new com.skillforge.skillforge_api.entity.User();
        if (securityContext != null && securityContext.getAuthentication() != null) {
            Object principal = securityContext.getAuthentication().getPrincipal();
                user = userRepository.findByEmail(principal.toString());
                return user;

        }
        throw new UsernameNotFoundException("No authenticated user found");
    }
}
