package com.courtroom.config;
import com.courtroom.enums.ForgotPasswordMethod;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.forgot-password")
public class ForgotPasswordProperties {

    private ForgotPasswordMethod method;

    public ForgotPasswordMethod getMethod() {
        return method;
    }

    public void setMethod(ForgotPasswordMethod method) {
        this.method = method;
    }
}