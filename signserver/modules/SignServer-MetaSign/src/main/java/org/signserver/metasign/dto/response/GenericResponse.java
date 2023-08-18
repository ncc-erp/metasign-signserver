package org.signserver.metasign.dto.response;

import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
public class GenericResponse<T> {

    private T payload;
    private boolean success;
    private String message;

    public static <T> GenericResponse<T> succeed(T payload) {
        return new GenericResponse<>(payload, true, null);
    }

    public static <T> GenericResponse<T> fail(String errorMessage) {
        return new GenericResponse<>(null, false, errorMessage);
    }

    public String toJSON() {
        return new GsonBuilder().serializeNulls().create().toJson(this);
    }
}
