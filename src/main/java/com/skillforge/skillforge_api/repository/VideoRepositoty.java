package com.skillforge.skillforge_api.repository;

import com.skillforge.skillforge_api.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepositoty extends JpaRepository<Video, Long> {
    Video findByGuid(String videoId);

}
