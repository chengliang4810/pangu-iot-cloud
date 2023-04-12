package com.pangu.auth.form;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class TokenLoginBody {

    @NotBlank(message = "token不能为空")
    private String token;

}
