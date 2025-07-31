package com.skillforge.skillforge_api.service;

import com.skillforge.skillforge_api.entity.Review;
import com.skillforge.skillforge_api.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private  ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public double calculateAverageRating(Long courseId) {
        List<Review> reviews = reviewRepository.findByCourseId(courseId);
        if (reviews.isEmpty()) {
            return 0.0; // No reviews, return 0
        }

        double totalRating = 0.0;
        for (Review review: reviews) {
            totalRating += review.getRatingValue();
        }
        return totalRating / reviews.size();
    }

    public int getTotalReviews(Long courseId) {
        List<Review> reviews = reviewRepository.findByCourseId(courseId);
        return reviews.size();
    }

}
