package com.skillforge.skillforge_api.entity;

import com.skillforge.skillforge_api.entity.ENUM.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

// lớp này giải thích rằng việc user nào đăng ký course nào và vào lúc nào...

@Entity
@Table(name = "enrollments")
@Getter
@Setter
public class Enrollments {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private Instant enrolledAt;

    @Column(nullable = false)
    private boolean isCompleted = false;
    private Instant completedAt;

    @Column(nullable = false)
    private double progress = 0.0;  // tỷ lệ hoàn thành khóa học %

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus status = EnrollmentStatus.ENROLLED;

    private String paymentId;
    private boolean isPaid = true; // Tạm thời miễn phí

    @PrePersist
    protected void onCreate() {
        enrolledAt = Instant.now();
    }

}
