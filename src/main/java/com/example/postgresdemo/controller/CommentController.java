package com.example.postgresdemo.controller;

import com.example.postgresdemo.exception.ResourceNotFoundException;
import com.example.postgresdemo.model.Comment;
import com.example.postgresdemo.repository.CommentRepository;
import com.example.postgresdemo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/books/{bookId}/reviews/{reviewId}/comments")
    public List<Comment> getCommentsByReviewId(@PathVariable Long reviewId) {
        return commentRepository.findByReviewId(reviewId);
    }

    @PostMapping("/books/{bookId}/reviews/{reviewId}/comments")
    public Comment addComment(@PathVariable Long reviewId,
                            @Valid @RequestBody Comment comment) {
        return reviewRepository.findById(reviewId)
                .map(review -> {
                    comment.setReview(review);
                    return commentRepository.save(comment);
                }).orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + reviewId));
    }

    @PutMapping("/books/{bookId}/reviews/{reviewId}/comments/{commentId}")
    public Comment updateComment(@PathVariable Long reviewId,
                               @PathVariable Long commentId,
                               @Valid @RequestBody Comment commentRequest) {
        if(!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found with id " + reviewId);
        }

        return commentRepository.findById(commentId)
                .map(comment -> {
                    comment.setText(commentRequest.getText());
                    return commentRepository.save(comment);
                }).orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + commentId));
    }

    @DeleteMapping("/books/{bookId}/reviews/{reviewId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long reviewId,
                                          @PathVariable Long commentId) {
        if(!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found with id " + reviewId);
        }

        return commentRepository.findById(commentId)
                .map(comment -> {
                    commentRepository.delete(comment);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + commentId));

    }
}