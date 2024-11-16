package com.igrowker.miniproject.User.Dto;

import org.springframework.web.multipart.MultipartFile;

public record UserProfilePhotoDto(Long user,
                                  MultipartFile photo) {
}
