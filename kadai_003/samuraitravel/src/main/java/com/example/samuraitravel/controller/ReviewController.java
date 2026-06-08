package com.example.samuraitravel.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

	private final ReviewRepository reviewRepository;
	private final HouseRepository houseRepository;

	public ReviewController(
			ReviewRepository reviewRepository,
			HouseRepository houseRepository) {
		this.reviewRepository = reviewRepository;
		this.houseRepository = houseRepository;

	}

	//レビュー投稿
	@GetMapping("/{houseId}/register")
	public String create(
			@PathVariable Integer houseId,
			Model model) {
		House house = houseRepository.getReferenceById(houseId);
		ReviewRegisterForm form = new ReviewRegisterForm();

		form.setRating(5);

		model.addAttribute("house",house);
		model.addAttribute("reviewRegisterForm", form);

		return "reviews/register";
	}

	@PostMapping("/{houseId}")
	public String create(
			@PathVariable Integer houseId,
			@ModelAttribute @Validated ReviewRegisterForm form,
			BindingResult bindingResult,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		if (bindingResult.hasErrors()) {
			return "reviews/register";
		}

		House house = houseRepository.getReferenceById(houseId);

		Review review = new Review();

		review.setHouse(house);
		review.setUser(userDetails.getUser());
		review.setRating(form.getRating());
		review.setComment(form.getComment());

		reviewRepository.save(review);

		return "redirect:/houses/" + houseId;
	}

	//レビュー編集
	@GetMapping("/{reviewId}/edit")
	public String edit(
			@PathVariable Integer reviewId,
			Model model) {

		Review review = reviewRepository.getReferenceById(reviewId);

		ReviewRegisterForm form = new ReviewRegisterForm();

		form.setId(review.getId());
		form.setRating(review.getRating());
		form.setComment(review.getComment());

		model.addAttribute("house", review.getHouse());
		model.addAttribute("reviewEditForm", form);

		return "reviews/edit";
	}

	@PostMapping("/update")
	public String update(
			@ModelAttribute @Validated ReviewRegisterForm form,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "reviews/edit";
		}

		Review review = reviewRepository.getReferenceById(form.getId());

		review.setRating(form.getRating());
		review.setComment(form.getComment());

		reviewRepository.save(review);

		return "redirect:/houses/" +
				review.getHouse().getId();
	}

	//レビュー削除
	@PostMapping("/{reviewId}/delete")
	public String delete(@PathVariable Integer reviewId, RedirectAttributes redirectAttributes) {
		Review review = reviewRepository.getReferenceById(reviewId);

		Integer houseId = review.getHouse().getId();

		reviewRepository.delete(review);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");

		return "redirect:/houses/" + houseId;
	}


}