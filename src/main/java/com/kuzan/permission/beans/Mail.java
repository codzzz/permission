package com.kuzan.permission.beans;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * Created by sui on 2019/3/23.
 */
@Data
@Builder
public class Mail {
    private String subject;
    private String message;
    private Set<String> receivers;
}
