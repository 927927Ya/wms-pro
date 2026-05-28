package com.d0dd.wms.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class LoginBody implements Serializable {
    private String username;
    private String password;
}
