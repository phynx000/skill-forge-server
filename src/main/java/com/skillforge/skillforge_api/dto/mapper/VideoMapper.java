package com.skillforge.skillforge_api.dto.mapper;

import com.skillforge.skillforge_api.dto.request.CreateVideoRequest;
import com.skillforge.skillforge_api.dto.response.BunnyStreamVideoResponse;
import com.skillforge.skillforge_api.entity.Video;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.net.http.HttpResponse;

@Component
public class VideoMapper {

    public Video toVideoEntity(CreateVideoRequest request) {
        Video video = new Video();
        video.setTitle(request.getTitle());
        video.setVideoLibraryId(request.getVideoLibraryId());
        video.setPlayURL(request.getVideoPlaylistUrl());
        video.setGuid(request.getGuid());
        video.setThumbnailUrl(request.getThumbnailUrl());
        video.setDuration(request.getLength());
        video.setPublic(request.isPublic());
        video.setStatus(request.getStatus());
        return video;
    }

    public Video resToEntity(BunnyStreamVideoResponse response) {
        Video video = new Video();
        video.setTitle(response.getTitle());
        video.setVideoLibraryId(response.getVideoLibraryId());
        video.setGuid(response.getGuid());
        video.setThumbnailUrl(response.getThumbnailUrl());
        video.setPlayURL(response.getVideoPlaylistUrl());
        video.setDuration(response.getLength());
        video.setPublic(response.isPublic());
        video.setStatus(response.getStatus());
        video.setAvailableResolutions(response.getAvailableResolutions());
        return video;

    }




    public BunnyStreamVideoResponse toDto(Video video) {
        BunnyStreamVideoResponse response = new BunnyStreamVideoResponse();
        response.setId(video.getId());
        response.setVideoLibraryId(video.getVideoLibraryId());
        response.setGuid(video.getGuid());
        response.setTitle(video.getTitle());
        response.setPublic(video.isPublic());
        response.setLength(video.getDuration());
        response.setStatus(video.getStatus());
        response.setAvailableResolutions(video.getAvailableResolutions());
        response.setCreatedAt(video.getCreatedAt());
        response.setThumbnailUrl(video.getThumbnailUrl());

        return response;

    }


}
