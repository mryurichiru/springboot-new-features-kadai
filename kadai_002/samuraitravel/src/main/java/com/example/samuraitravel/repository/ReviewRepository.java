package com.example.samuraitravel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Integer>{
	
	//民宿ごとのレビュー一覧
	public Page<Review> findByHouseOrderByCreatedAtDesc(
			House house,
			Pageable pageable
			);
	
	//ユーザーがレビュー済み
	//Optional<Review> findByHouseAndUser(House house, User user);
	 boolean existsByHouseAndUser(House house, User user);

	//詳細画面用最新6件
	List<Review> findTop6ByHouseOrderByCreatedAtDesc(House house);
	
}
