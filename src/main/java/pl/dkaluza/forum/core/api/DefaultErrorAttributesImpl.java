package pl.dkaluza.forum.core.api;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;

@Component
public class DefaultErrorAttributesImpl extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> attrs = super.getErrorAttributes(webRequest, options);
        attrs.remove("error");
        attrs.remove("path");
        attrs.put("timestamp", ZonedDateTime.now(ZoneOffset.UTC));
        return attrs;
    }
}
