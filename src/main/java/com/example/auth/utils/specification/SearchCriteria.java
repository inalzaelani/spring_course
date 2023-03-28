package com.example.auth.utils.specification;

import com.example.auth.utils.constants.Operator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchCriteria {

    private String key;

    private Operator operator;

    private String value;
}
