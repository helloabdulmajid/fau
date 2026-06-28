package in.abdulmajid.cardiq.admin.controller;

import in.abdulmajid.cardiq.admin.dto.AdminStatsResponse;
import in.abdulmajid.cardiq.admin.service.AdminService;
import in.abdulmajid.cardiq.auth.dto.UserProfileResponse;
import in.abdulmajid.cardiq.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/stats")
    public ApiResponse<AdminStatsResponse> getStats() {
        return ApiResponse.<AdminStatsResponse>builder()
                .success(true)
                .message("Stats fetched successfully")
                .data(adminService.getStats())
                .build();
    }

    @GetMapping("/users")
    public ApiResponse<List<UserProfileResponse>> getUsers() {
        return ApiResponse.<List<UserProfileResponse>>builder()
                .success(true)
                .message("Users fetched successfully")
                .data(adminService.getUsers())
                .build();
    }
}
