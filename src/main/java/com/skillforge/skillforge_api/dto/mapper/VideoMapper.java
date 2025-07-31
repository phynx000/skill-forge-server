package com.skillforge.skillforge_api.dto.mapper;

import com.skillforge.skillforge_api.dto.request.CreateVideoRequest;
import com.skillforge.skillforge_api.dto.response.BunnyStreamVideoResponse;
import com.skillforge.skillforge_api.dto.response.VideoDTO;
import com.skillforge.skillforge_api.entity.Video;
import org.springframework.stereotype.Component;

@Component
public class VideoMapper {

//    public Video toVideoEntity(CreateVideoRequest request) {
//        Video video = new Video();
//        video.setTitle(request.getTitle());
//        video.setVideoLibraryId(request.getVideoLibraryId());
//        video.setPlayURL(request.getVideoPlaylistUrl());
//        video.setGuid(request.getGuid());
//        video.setThumbnailUrl(request.getThumbnailUrl());
//        video.setLength(request.getLength());
//        video.setPublic(request.isPublic());
//        video.setStatus(request.getStatus());
//        return video;
//    }

    public Video resToEntity(BunnyStreamVideoResponse response) {
        Video video = new Video();
        video.setTitle(response.getTitle());
        video.setVideoLibraryId(response.getVideoLibraryId());
        video.setGuid(response.getGuid());
        video.setThumbnailUrl(response.getThumbnailUrl());
        video.setPlayURL(response.getVideoPlaylistUrl());
        video.setLength(response.getLength());
        video.setPublic(response.isPublic());
        video.setStatus(response.getStatus());
        video.setAvailableResolutions(response.getAvailableResolutions());
        return video;

    }


    public VideoDTO toDto(Video video) {
        VideoDTO dto = new VideoDTO();
        dto.setId(video.getId());
        dto.setVideoLibraryId(video.getVideoLibraryId());
        dto.setGuid(video.getGuid());
        dto.setTitle(video.getTitle());
        dto.setPublic(video.isPublic());
        dto.setLength(video.getLength());
        dto.setStatus(video.getStatus());
        dto.setAvailableResolutions(video.getAvailableResolutions());
        dto.setVideoPlaylistUrl(video.getPlayURL());
        dto.setCreatedAt(video.getCreatedAt());
        dto.setThumbnailUrl(video.getThumbnailUrl());
        dto.setHlsURL(video.getHlsUrl());

        return dto;

    }

//    public BunnyStreamVideoResponse toResDto(Video video) {
//        BunnyStreamVideoResponse response = new BunnyStreamVideoResponse();
//        response.setId(video.getId());
//        response.setVideoLibraryId(video.getVideoLibraryId());
//        response.setVideoPlaylistUrl(video.getPlayURL());
//        response.setGuid(video.getGuid());
//        response.setTitle(video.getTitle());
//        response.setPublic(video.isPublic());
//        response.setLength(video.getLength());
//        response.setStatus(video.getStatus());
//        response.setAvailableResolutions(video.getAvailableResolutions());
//        response.setCreatedAt(video.getCreatedAt());
//        response.setThumbnailUrl(video.getThumbnailUrl());
//
//        return response;
//
//    }




}
