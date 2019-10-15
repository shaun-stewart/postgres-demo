package com.example.postgresdemo.controller;

import com.example.postgresdemo.exception.ResourceNotFoundException;
import com.example.postgresdemo.model.Review;
import com.example.postgresdemo.repository.ReviewRepository;
import com.example.postgresdemo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/books/{bookId}/reviews")
    public List<Review> getReviewsByBookId(@PathVariable Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    @PostMapping("/books/{bookId}/reviews")
    public Review addReview(@PathVariable Long bookId,
                            @Valid @RequestBody Review review) {
        return bookRepository.findById(bookId)
                .map(book -> {
                    review.setBook(book);
                    return reviewRepository.save(review);
                }).orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + bookId));
    }

    @PutMapping("/books/{bookId}/reviews/{reviewId}")
    public Review updateReview(@PathVariable Long bookId,
                               @PathVariable Long reviewId,
                               @Valid @RequestBody Review reviewRequest) {
        if(!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Book not found with id " + bookId);
        }

        return reviewRepository.findById(reviewId)
                .map(review -> {
                    review.setText(reviewRequest.getText());
                    return reviewRepository.save(review);
                }).orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + reviewId));
    }

    @DeleteMapping("/books/{bookId}/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long bookId,
                                          @PathVariable Long reviewId) {
        if(!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Book not found with id " + bookId);
        }

        return reviewRepository.findById(reviewId)
                .map(review -> {
                    reviewRepository.delete(review);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + reviewId));

    }
}