package ttv.poltoraha.pivka.service;

import ttv.poltoraha.pivka.dao.request.ReviewRequestDto;

public interface ReviewService {
    public void createReview(ReviewRequestDto requestDto);
    public void deleteReview(Integer reviewId);
    public void updateReview(Integer reviewId, ReviewRequestDto requestDto);
    public void updateAvgRating(Integer reviewId);
    public void createReviewRating(String username, Integer reviewId, Integer rating);
}
