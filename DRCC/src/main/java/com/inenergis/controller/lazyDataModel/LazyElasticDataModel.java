package com.inenergis.controller.lazyDataModel;

import com.inenergis.model.SearchMatch;
import com.inenergis.service.aws.ElasticUtilsService;
import io.searchbox.core.SearchResult;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class LazyElasticDataModel extends LazyDataModel<SearchMatch> {

    public static final int MAX_RESULT_SIZE = 10000;

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @NonNull
    ElasticUtilsService elasticUtilsService;

    @NonNull
    protected String querySearch;

    @NonNull
    protected String indexName;

    @NonNull
    protected String typeName;

    protected List<SearchMatch> datasource = new ArrayList<>();

    @Override
    public List<SearchMatch> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        SearchResult result;
        try {
            if (StringUtils.isEmpty(querySearch)) {
                result = elasticUtilsService.defaultSearch(new String[]{indexName}, new String[]{typeName}, first, pageSize);
            } else {
                result = elasticUtilsService.getSearchResult(querySearch, new String[]{indexName}, new String[]{typeName}, first, pageSize);
            }
            int totalHits = result.getTotal() <= MAX_RESULT_SIZE ? result.getTotal() : MAX_RESULT_SIZE;
            this.setRowCount(totalHits);
            datasource = elasticUtilsService.generateList(result, new String[]{typeName});
        } catch (IOException e) {
            log.error("Error loading elasticSearch data", e);
            this.setRowCount(0);
        }
        return datasource;
    }

    @Override
    public SearchMatch getRowData(String rowKey) {
        for (Object o : datasource) {
            SearchMatch match = (SearchMatch) o;
            if (match.getId() != null && match.getId().equals(rowKey)) {
                return match;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(SearchMatch o) {
        return o.getId();
    }
}