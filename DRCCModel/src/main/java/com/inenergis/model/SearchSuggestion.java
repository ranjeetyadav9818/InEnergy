package com.inenergis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Antonio on 18/08/2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchSuggestion implements SearchMatch {

    private String suggestion;
    private String outcome;
    private String icon;
    private String group;
    private String iconGroup;
    private String permission;

    @Override
    public String getId() {
        return "";
    }

    @Override
    public String toString() {
        return suggestion;
    }

    @Override
    public String getType() {
        return "SUGGESTION";
    }
}
