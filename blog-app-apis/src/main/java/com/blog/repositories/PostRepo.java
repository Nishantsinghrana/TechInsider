package com.blog.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.blog.models.Category;
import com.blog.models.Post;
import com.blog.models.User;



public interface PostRepo extends JpaRepository<Post, Integer>{
	List<Post> findByUser(User user);
	List<Post> findByCategory(Category category);
	List<Post> findByTitleContaining(String title);


}