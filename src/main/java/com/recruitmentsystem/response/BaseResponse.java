package com.recruitmentsystem.response;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"err_code", "message"})
public class BaseResponse {

    @JsonProperty("err_code")
    private String err_code;

    @JsonProperty("message")
    private String message;

    public BaseResponse() {
        this.err_code = "0";
        this.message = "SUCCESS";
    }

    public BaseResponse(String err_code, String message) {
        super();
        this.err_code = err_code;
        this.message = message;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public void setAdditionalProperty(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}

