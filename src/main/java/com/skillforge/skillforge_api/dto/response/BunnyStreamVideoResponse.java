package com.skillforge.skillforge_api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BunnyStreamVideoResponse {
    private Long id;
    private int videoLibraryId;
    private String guid;
    private String title;
    private boolean isPublic;
    private double length;
    private int status;
    private int rotation;
    private String availableResolutions;
    private boolean hasMP4Fallback;
    private String collectionId;
    private String thumbnailFileName;
    private String thumbnailUrl;
    private String videoPlaylistUrl;
    private String category;
    private boolean jitEncodingEnabled;
    private Instant createdAt;

}
