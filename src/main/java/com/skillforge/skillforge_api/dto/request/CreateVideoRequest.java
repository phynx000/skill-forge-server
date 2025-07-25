package com.skillforge.skillforge_api.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
public class CreateVideoRequest {
    private String title;
    private int videoLibraryId;
    private String guid;
    private boolean isPublic;
    private double length;
    private int status;
    private int rotation;
    private String availableResolutions;
    private boolean hasMP4Fallback;
    private String collectionId;
    private String thumbnailUrl;
    private String category;
    private boolean jitEncodingEnabled;
    private String videoId;
    private String videoPlaylistUrl;

}