package com.skillforge.skillforge_api.config;

import com.skillforge.skillforge_api.entity.Course;
import com.skillforge.skillforge_api.entity.Review;
import com.skillforge.skillforge_api.entity.User;
import com.skillforge.skillforge_api.repository.CourseRepository;
import com.skillforge.skillforge_api.repository.ReviewRepository;
import com.skillforge.skillforge_api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.List;

@Configuration
public class DataInitializer {
//    @Bean
//    CommandLineRunner initRoles(RoleRepository roleRepository) {
//        return args -> {
//            List<String> roles = List.of("ADMIN", "INSTRUCTOR", "STUDENT");
//            for (String roleName : roles) {
//                roleRepository.findByName(roleName)
//                        .orElseGet(() -> roleRepository.save(new Role(roleName)));
//            }
//        };
//    }

//    @Bean
//    CommandLineRunner initReviews(ReviewRepository reviewRepository, UserRepository userRepository, CourseRepository courseRepository) {
//        return args -> {
//            User user = userRepository.findById(502L).orElse(null);
//            Course course = courseRepository.findById(102L).orElse(null);
//
//            if (user != null && course != null) {
//                Review review1 = new Review();
//                review1.setCommnent("This is a great course!");
//                review1.setRatingValue(5);
//                review1.setUser(user);
//                review1.setCourse(course);
//                review1.setCreatedAt(Instant.now());
//
//                Review review2 = new Review();
//                review2.setCommnent("Very informative and well-structured.");
//                review2.setRatingValue(4);
//                review2.setUser(user);
//                review2.setCourse(course);
//                review2.setCreatedAt(Instant.now().minusSeconds(86400)); // 1 day ago
//
//                Review review3 = new Review();
//                review3.setCommnent("Could be better, some parts were confusing.");
//                review3.setRatingValue(3);
//                review3.setUser(user);
//                review3.setCourse(course);
//                review3.setCreatedAt(Instant.now().minusSeconds(172800)); // 2 days ago
//
//                reviewRepository.saveAll(List.of(review1, review2, review3));
//            }
//        };
//    }

}
