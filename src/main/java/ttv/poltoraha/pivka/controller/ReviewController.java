package ttv.poltoraha.pivka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ttv.poltoraha.pivka.dao.request.ReviewRequestDto;
import ttv.poltoraha.pivka.service.ReviewService;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    public void createReview(@RequestBody ReviewRequestDto dto) {
        reviewService.createReview(dto);
    }

    public void updateReview(@RequestParam Integer reviewId, @RequestBody ReviewRequestDto dto) {
        reviewService.updateReview(reviewId, dto);
    }

    public void deleteReview(@RequestParam Integer reviewId) {
        reviewService.deleteReview(reviewId);
    }
}
