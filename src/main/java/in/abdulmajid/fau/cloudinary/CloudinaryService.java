package in.abdulmajid.fau.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("upload_preset", "fau_cards");
        Map result = cloudinary.uploader().upload(file.getBytes(), params);
        return (String) result.get("secure_url");
    }

    public void deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) return;
        try {
            String publicId = extractPublicId(imageUrl);
            if (publicId == null) return;
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Deleted Cloudinary image: {}", publicId);
        } catch (Exception e) {
            log.warn("Failed to delete Cloudinary image: {}", e.getMessage());
        }
    }

    private String extractPublicId(String imageUrl) {
        try {
            String marker = "/image/upload/";
            int idx = imageUrl.indexOf(marker);
            if (idx == -1) return null;
            String after = imageUrl.substring(idx + marker.length());
            if (after.startsWith("v")) {
                int slash = after.indexOf('/');
                if (slash != -1) after = after.substring(slash + 1);
                else return null;
            }
            int dot = after.lastIndexOf('.');
            if (dot != -1) after = after.substring(0, dot);
            return after.isEmpty() ? null : after;
        } catch (Exception e) {
            return null;
        }
    }
}
