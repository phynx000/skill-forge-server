package com.skillforge.skillforge_api.controller;

import com.skillforge.skillforge_api.dto.request.CreateVideoRequest;
import com.skillforge.skillforge_api.dto.response.ApiResponse;
import com.skillforge.skillforge_api.dto.response.BunnyStreamVideoResponse;
import com.skillforge.skillforge_api.dto.response.VideoDTO;
import com.skillforge.skillforge_api.service.BunnyStreamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
@Slf4j
public class BunnyStreamController {

    private final BunnyStreamService bunnyStreamService;

    public BunnyStreamController(BunnyStreamService bunnyStreamService) {
        this.bunnyStreamService = bunnyStreamService;
    }

    /**
     * API để tạo và upload video trong một bước
     */
    @PostMapping("/videos/upload-complete")
    public ResponseEntity<VideoDTO> uploadCompleteVideo(
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "collectionId", required = false) String collectionId,
            @RequestParam(value = "enabledResolutions", defaultValue = "true") boolean enabledResolutions) throws Exception {
            // 1. Tạo video
            CreateVideoRequest createRequest = CreateVideoRequest.builder()
                    .title(title)
                    .collectionId(collectionId)
                    .build();
            BunnyStreamVideoResponse video = bunnyStreamService.createVideo(createRequest);
            // 2. Upload file
            bunnyStreamService.uploadVideo(video.getGuid(), file, enabledResolutions);
            VideoDTO videoDTO = bunnyStreamService.getVideoPlayData(video.getGuid());
            return ResponseEntity.status(HttpStatus.CREATED).body(videoDTO);

    }
    
    /**
     * API để lấy thông tin để phát video
     */
    @GetMapping("/videos/{videoId}/play")
    public ResponseEntity<VideoDTO> getVideoPlayInfo(@PathVariable String videoId) throws Exception {
        VideoDTO videoResponse = bunnyStreamService.getVideoPlayData(videoId);
        if (videoResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(videoResponse);
    }

    /**
     * API để lấy thông tin video
     */
    @GetMapping("/videos/{videoId}")
    public ResponseEntity<ApiResponse<BunnyStreamVideoResponse>> getVideoInfo(@PathVariable String videoId) {
        try {
            BunnyStreamVideoResponse video = bunnyStreamService.getVideoInfo(videoId);

            ApiResponse<BunnyStreamVideoResponse> response = ApiResponse.<BunnyStreamVideoResponse>builder()
                    .success(true)
                    .message("Video info retrieved successfully")
                    .data(video)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting video info", e);

            ApiResponse<BunnyStreamVideoResponse> response = ApiResponse.<BunnyStreamVideoResponse>builder()
                    .success(false)
                    .message("Failed to get video info")
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
