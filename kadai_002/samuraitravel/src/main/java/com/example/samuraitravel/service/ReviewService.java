package com.example.samuraitravel.service;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.ReviewRepository;

@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;

	public ReviewService(ReviewRepository reviewReository) {
		this.reviewRepository = reviewReository;
	}
	
@Transactional
public void create(ReviewRegisterForm reviewRegisterForm) {
	Review review = new Review();
	review.setRating(reviewRegisterForm.getRating());
	review.setComment(reviewRegisterForm.getComment());
	
	reviewRepository.save(review);
	
	}
	

}
