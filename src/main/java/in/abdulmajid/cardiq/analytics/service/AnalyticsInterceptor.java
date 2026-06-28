package in.abdulmajid.cardiq.analytics.service;

import in.abdulmajid.cardiq.auth.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AnalyticsInterceptor implements HandlerInterceptor {

    private final AnalyticsService analyticsService;

    private static final List<String> EXCLUDED_PREFIXES = List.of(
        "/api/v1/admin/",
        "/api/v1/auth/",
        "/health",
        "/error"
    );

    private static final String[] MOBILE_KEYWORDS = {"mobile", "android", "iphone", "ipad", "ipod", "phone", "blackberry", "opera mini"};
    private static final String[] TABLET_KEYWORDS = {"tablet", "ipad", "kindle", "playbook", "samsung", "nexus 7", "nexus 10"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();
        for (String prefix : EXCLUDED_PREFIXES) {
            if (path.startsWith(prefix)) return true;
        }
        try {
            String url = request.getRequestURL().toString();
            String queryString = request.getQueryString();
            String referrer = request.getHeader("Referer");
            String userAgent = request.getHeader("User-Agent");
            String ip = extractIp(request);

            String sessionId = getOrCreateSessionId(request);
            Long userId = getCurrentUserId();

            String ipHash = hashIp(ip);
            String deviceType = detectDevice(userAgent);
            String browser = detectBrowser(userAgent);
            String os = detectOs(userAgent);
            String countryCode = detectCountry(request);

            analyticsService.recordPageView(url, path, queryString, referrer, userAgent,
                    ipHash, sessionId, userId, deviceType, browser, os, countryCode,
                    response.getStatus());

            if (path.contains("/api/v1/search") && queryString != null && queryString.contains("keyword=")) {
                String keyword = extractKeyword(queryString);
                if (keyword != null && !keyword.isBlank()) {
                    analyticsService.logSearch(keyword, 0, referrer, sessionId, userId, deviceType);
                }
            }
        } catch (Exception e) {
            // Don't block requests on analytics failure
        }
        return true;
    }

    private String extractIp(HttpServletRequest request) {
        String xForwarded = request.getHeader("X-Forwarded-For");
        if (xForwarded != null && !xForwarded.isBlank()) {
            return xForwarded.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isBlank()) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }

    private String hashIp(String ip) {
        if (ip == null || ip.isBlank()) return "unknown";
        try {
            String salt = LocalDate.now().toString();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((ip + salt).getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.substring(0, 16);
        } catch (Exception e) {
            return "unknown";
        }
    }

    private String getOrCreateSessionId(HttpServletRequest request) {
        String sessionId = (String) request.getAttribute("analytics_session_id");
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString().substring(0, 8);
            request.setAttribute("analytics_session_id", sessionId);
        }
        return sessionId;
    }

    private Long getCurrentUserId() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof User user) {
                return user.getId();
            }
        } catch (Exception ignored) {}
        return null;
    }

    private String extractKeyword(String queryString) {
        for (String part : queryString.split("&")) {
            if (part.startsWith("keyword=")) {
                return java.net.URLDecoder.decode(part.substring(8), StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    static String detectDevice(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) return "Unknown";
        String ua = userAgent.toLowerCase();
        for (String kw : TABLET_KEYWORDS) {
            if (ua.contains(kw)) return "Tablet";
        }
        for (String kw : MOBILE_KEYWORDS) {
            if (ua.contains(kw)) return "Mobile";
        }
        if (ua.contains("bot") || ua.contains("crawler") || ua.contains("spider")) return "Bot";
        if (ua.contains("windows") || ua.contains("macintosh") || ua.contains("linux") || ua.contains("x11")) return "Desktop";
        return "Unknown";
    }

    static String detectBrowser(String userAgent) {
        if (userAgent == null) return "Unknown";
        String ua = userAgent.toLowerCase();
        if (ua.contains("edg/") || ua.contains("edge/")) return "Edge";
        if (ua.contains("chrome/") && !ua.contains("chromium")) return "Chrome";
        if (ua.contains("safari/") && ua.contains("version/")) return "Safari";
        if (ua.contains("firefox/")) return "Firefox";
        if (ua.contains("opera") || ua.contains("opr/")) return "Opera";
        if (ua.contains("trident") || ua.contains("msie")) return "IE";
        return "Other";
    }

    static String detectOs(String userAgent) {
        if (userAgent == null) return "Unknown";
        String ua = userAgent.toLowerCase();
        if (ua.contains("windows")) return "Windows";
        if (ua.contains("macintosh") || ua.contains("mac os")) return "macOS";
        if (ua.contains("android")) return "Android";
        if (ua.contains("iphone") || ua.contains("ipad") || ua.contains("ipod")) return "iOS";
        if (ua.contains("linux")) return "Linux";
        if (ua.contains("cros")) return "ChromeOS";
        return "Other";
    }

    private String detectCountry(HttpServletRequest request) {
        String[] geoHeaders = {
            "CF-IPCountry",
            "X-Client-Geo-Location",
            "CloudFront-Viewer-Country",
            "X-Geo-Country",
            "X-Country-Code",
            "X-Real-Country",
            "X-App-Cloud-Country"
        };
        for (String header : geoHeaders) {
            String val = request.getHeader(header);
            if (val != null && !val.isBlank()) return val.toUpperCase();
        }
        return "";
    }
}
