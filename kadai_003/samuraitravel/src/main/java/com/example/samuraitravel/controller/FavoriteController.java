package com.example.samuraitravel.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.security.UserDetailsImpl;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {

	private final HouseRepository houseRepository;
	private final FavoriteRepository favoriteRepository;

	public FavoriteController(
			HouseRepository houseRepository,
			FavoriteRepository favoriteRepository) {

		this.houseRepository = houseRepository;
		this.favoriteRepository = favoriteRepository;
	}

	//お気に入り登録
	@PostMapping("/{houseId}/create")
	public String create(
			@PathVariable Integer houseId,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {

		House house = houseRepository.getReferenceById(houseId);

		Favorite favorite = new Favorite();

		favorite.setUser(userDetails.getUser());
		favorite.setHouse(house);

		favoriteRepository.save(favorite);

		return "redirect:/houses/" + houseId;
	}

	//お気に入り削除
	@PostMapping("/{houseId}/delete")
	public String delete(
			@PathVariable Integer houseId,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		System.out.println("削除処理開始");
		House house = houseRepository.getReferenceById(houseId);

		Favorite favorite = favoriteRepository.findByUserAndHouse(
				userDetails.getUser(),
				house);

		favoriteRepository.delete(favorite);

		return "redirect:/houses/" + houseId;
	}

}
