package com.skillforge.skillforge_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillforge.skillforge_api.config.BunnyStreamConfig;
import com.skillforge.skillforge_api.dto.mapper.VideoMapper;
import com.skillforge.skillforge_api.dto.request.CreateVideoRequest;
import com.skillforge.skillforge_api.dto.response.BunnyStreamVideoResponse;
import com.skillforge.skillforge_api.dto.response.VideoDTO;
import com.skillforge.skillforge_api.entity.Video;
import com.skillforge.skillforge_api.repository.VideoRepositoty;
import com.skillforge.skillforge_api.utils.BunnyTokenSigner;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

@Service
@Slf4j
public class BunnyStreamService {

    private final BunnyStreamConfig config;
    private final HttpClient httpClient;
    private final VideoMapper videoMapper;
    private final VideoRepositoty videoRepositoty;
    private final BunnyTokenSigner tokenSigner;

    @Value("${bunny.stream.bunny-cdn-host}")
    private String bunnyCdnHost;

    @Value("${bunny.stream.bunny-auth-key}")
    private String securityKey;

    public BunnyStreamService(BunnyStreamConfig config, VideoMapper videoMapper, VideoRepositoty videoRepositoty, BunnyTokenSigner tokenSigner) {
        this.config = config;
        this.videoMapper = videoMapper;
        this.videoRepositoty = videoRepositoty;
        this.tokenSigner = tokenSigner;
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Tạo video mới trên BunnyStream
     */

    public BunnyStreamVideoResponse createVideo(CreateVideoRequest request) throws Exception {
        String url = String.format("%s/library/%s/videos", config.getBaseUrl(), config.getLibraryId());

        // Tạo JSON body
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .header("AccessKey", config.getAccessKey())
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Failed to create video: " + response.body());
        }
        BunnyStreamVideoResponse videoRes = mapper.readValue(response.body(), BunnyStreamVideoResponse.class);
        Video video = videoMapper.resToEntity(videoRes);
        videoRepositoty.save(video);

        //
        return mapper.readValue(response.body(), BunnyStreamVideoResponse.class);
    }

    /**
     * Upload video file lên BunnyStream
     */
    public void uploadVideo(String videoId, MultipartFile file, boolean enabledResolutions) throws Exception {
        String url = String.format("%s/library/%s/videos/%s",
                config.getBaseUrl(), config.getLibraryId(), videoId);

        if (enabledResolutions) {
            url += "?enabledResolutions=240p,360p,480p,720p,1080p";
        }

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("AccessKey", config.getAccessKey())
                .PUT(HttpRequest.BodyPublishers.ofByteArray(file.getBytes()))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Failed to upload video: " + response.body());
        }

        log.info("Video uploaded successfully. Response: {}", response.body());
    }

    public String getVideoLink(String videoId) {
        var allowedPath = "/" + videoId + "/";
        // Kiểm tra xem securityKey có được cấu hình không
        System.out.println("Security Key: " + securityKey);
        var videoUrl = String.format("https://%s/%s/playlist.m3u8", bunnyCdnHost, videoId);
        return tokenSigner.signUrl(videoUrl, securityKey, allowedPath);
    }



    @Transactional
    public VideoDTO getVideoPlayData(String videoId) throws Exception {
        String url = String.format("%s/library/%s/videos/%s/play",
                config.getBaseUrl(), config.getLibraryId(), videoId);


        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/json")
                .header("AccessKey", config.getAccessKey())
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response: " + response.body());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to get video: " + response.body());
        }

        JSONObject jsonResponse = new JSONObject(response.body());
        String videoPlaylistUrl = jsonResponse.getString("videoPlaylistUrl");
        String thumbnailUrl = jsonResponse.getString("thumbnailUrl");
//        String fallbackUrl = jsonResponse.getString("fallbackUrl");
//        String playbackSpeeds = jsonResponse.getString("playbackSpeeds");
        JSONObject videobj = jsonResponse.getJSONObject("video");
        ObjectMapper mapper = new ObjectMapper();
        VideoDTO videoDto = mapper.readValue(videobj.toString(), VideoDTO.class);

        Video video = videoRepositoty.findByGuid(videoId);
        video.setTitle(videoDto.getTitle());
        video.setVideoLibraryId(videoDto.getVideoLibraryId());
        video.setAvailableResolutions(videoDto.getAvailableResolutions());
        video.setGuid(videoDto.getGuid());
        video.setPublic(videoDto.isPublic());
        video.setStatus(videoDto.getStatus());
        video.setPlayURL(videoPlaylistUrl);
        video.setHlsUrl(getVideoLink(videoId));
        video.setThumbnailUrl(thumbnailUrl);
        video.setAvailableResolutions(videoDto.getAvailableResolutions());
        videoRepositoty.save(video);
        videoDto = videoMapper.toDto(video);
        return videoDto;
    }

//    public String generateSignedUrl(String streamHostname, String path, long validDurationSeconds) throws Exception {
//        // Kiểm tra xem securityKey có được cấu hình không
//        if (securityKey == null || securityKey.isEmpty()) {
//            throw new RuntimeException("Security key is not configured");
//        }
//        // Tạo URL đã ký
//        return StreamUtils.generateSignedUrl(streamHostname, path, securityKey, validDurationSeconds);
//    }

    public Video handleFindVideoByVideoId(String videoId){
        Video video = videoRepositoty.findByGuid(videoId);
        if (video == null) {
            throw new RuntimeException("Video not found with ID: " + videoId);
        }
        return video;
    }
    
    /**
     * Lấy thông tin video
     */
    public BunnyStreamVideoResponse getVideoInfo(String videoId) throws Exception {
        String url = String.format("%s/library/%s/videos/%s",
                config.getBaseUrl(), config.getLibraryId(), videoId);



        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/json")
                .header("AccessKey", config.getAccessKey())
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to get video info: " + response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), BunnyStreamVideoResponse.class);
    }
}