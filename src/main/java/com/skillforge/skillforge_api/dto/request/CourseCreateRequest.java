package com.skillforge.skillforge_api.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CourseCreateRequest {

    private String title;
    private String description;
    private String thumbnailUrl;
    private Long categoryId; // ID của category mà khóa học thuộc về
    private String slug; // Slug cho SEO, thường là tiêu đề viết thường và thay thế khoảng trắng bằng dấu gạch ngang
    private BigDecimal price; // Giá của khóa học, có thể là 0 nếu khóa học miễn phí
    private boolean isPublished; // Trạng thái công khai của khóa học, true nếu đã công khai, false nếu chưa

}
