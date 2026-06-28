package in.abdulmajid.cardiq.cloudinary;

import in.abdulmajid.cardiq.common.ApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/upload-image")
@RequiredArgsConstructor
public class ImageUploadController {

    private final CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadImage(
            @RequestParam("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<Map<String, String>>builder()
                            .success(false)
                            .message("File is empty")
                            .build());
        }

        try {
            String url = cloudinaryService.uploadImage(file);

            return ResponseEntity.ok(
                    ApiResponse.<Map<String, String>>builder()
                            .success(true)
                            .message("Image uploaded successfully")
                            .data(Map.of("url", url))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<Map<String, String>>builder()
                            .success(false)
                            .message("Upload failed: " + e.getMessage())
                            .build());
        }
    }
}
