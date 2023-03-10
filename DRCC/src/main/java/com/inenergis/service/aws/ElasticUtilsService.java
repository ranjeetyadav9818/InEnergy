package com.inenergis.service.aws;

import com.inenergis.model.ElasticAggregator;
import com.inenergis.model.ElasticAgreementPointMap;
import com.inenergis.model.ElasticAsset;
import com.inenergis.model.ElasticContract;
import com.inenergis.model.ElasticContractEntity;
import com.inenergis.model.ElasticDevice;
import com.inenergis.model.ElasticEvent;
import com.inenergis.model.ElasticEventNotification;
import com.inenergis.model.ElasticISO;
import com.inenergis.model.ElasticInvoice;
import com.inenergis.model.ElasticLocation;
import com.inenergis.model.ElasticProgram;
import com.inenergis.model.ElasticRegistration;
import com.inenergis.model.ElasticResource;
import com.inenergis.model.MultiSearchMatch;
import com.inenergis.model.SearchMatch;
import com.inenergis.util.ElasticConnectionPool;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ElasticUtilsService {
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String APM = "apm";
    public static final String AGGREGATOR = "aggregator";
    public static final String ISO = "iso";
    public static final String PROGRAM = "program";
    public static final String CONTRACT = "contract";
    public static final String CONTRACT_ENTITY = "contractEntity";
    public static final String EVENT_NOTIFICATION = "event_notification";
    public static final String EVENT = "event";
    public static final String ASSET = "asset";
    public static final String DEVICE = "device";
    public static final String LOCATION = "location";
    public static final String REGISTRATION = "registration";
    public static final String RESOURCE = "resource";
    public static final String INVOICE = "invoice";

    private static Map<String, Class> map = new LinkedHashMap<>();

    static {
        map.put(APM, ElasticAgreementPointMap.class);
        map.put(AGGREGATOR, ElasticAggregator.class);
        map.put(ISO, ElasticISO.class);
        map.put(PROGRAM, ElasticProgram.class);
        map.put(CONTRACT, ElasticContract.class);
        map.put(CONTRACT_ENTITY, ElasticContractEntity.class);
        map.put(EVENT_NOTIFICATION, ElasticEventNotification.class);
        map.put(EVENT, ElasticEvent.class);
        map.put(ASSET, ElasticAsset.class);
        map.put(DEVICE, ElasticDevice.class);
        map.put(LOCATION, ElasticLocation.class);
        map.put(REGISTRATION, ElasticRegistration.class);
        map.put(RESOURCE, ElasticResource.class);
        map.put(INVOICE, ElasticInvoice.class);
    }

    @Inject
    ElasticConnectionPool elasticConnectionService;

    public List<SearchMatch> generateList(SearchResult response, String typeNames[]) {
        List<SearchMatch> result = new ArrayList<>();
        if (response.getTotal() != null && response.getTotal() > 0) {
            for (String typeName : typeNames) {
                List<SearchResult.Hit<SearchMatch, Void>> hits = response.getHits(map.get(typeName));
                Iterator<SearchResult.Hit<SearchMatch, Void>> hitIterator = hits.iterator();
                while (hitIterator.hasNext()) {
                    SearchResult.Hit<SearchMatch, Void> next = hitIterator.next();
                    if (next.type.equals(typeName)) {
                        addResults(result, next.source);
                    }
                }
            }
        }
        return result;
    }

    public void addResults(List<SearchMatch> result, SearchMatch match) {
        if (MultiSearchMatch.class.isAssignableFrom(match.getClass())) {
            for (String action : ((MultiSearchMatch) match).getActions()) {
                MultiSearchMatch clone = ((MultiSearchMatch) match).clone(action);
                result.add(clone);
            }
        } else {
            result.add(match);
        }
    }

    public SearchResult getSearchResult(String query, String[] indexNames, String[] typeNames, int first, int pageSize) throws IOException {
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(query, "_all");
        multiMatchQueryBuilder.type(MultiMatchQueryBuilder.Type.CROSS_FIELDS).operator(Operator.AND);
        return getSearchResultFromElastic(indexNames, typeNames, first, pageSize, multiMatchQueryBuilder);
    }

    public List<SearchMatch> partialSearch(String query, String[] indexNames, String[] typeNames) throws IOException {
        SearchResult response;
        if (StringUtils.isEmpty(query)) {
            response = defaultSearch(indexNames, typeNames, 0, DEFAULT_PAGE_SIZE);
        } else {
            response = getSearchResult(query, indexNames, typeNames, 0, DEFAULT_PAGE_SIZE);
        }
        List<SearchMatch> searchMatches = generateList(response, typeNames);
        return searchMatches;
    }

    public SearchResult defaultSearch(String[] indexNames, String[] typeNames, int first, int pageSize) throws IOException {
        return getSearchResultFromElastic(indexNames, typeNames, first, pageSize, QueryBuilders.matchAllQuery());
    }

    public SearchResult getSearchResultFromElastic(String[] indexNames, String[] typeNames, int first, int pageSize, QueryBuilder query) throws IOException {
        JestClient client = elasticConnectionService.getClient();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(first).size(pageSize).query(query);
        Search.Builder builder = new Search.Builder(searchSourceBuilder.toString());
        for (String indexName : indexNames) {
            builder.addIndex(indexName);
        }
        for (String typeName : typeNames) {
            builder.addType(typeName);
        }
        Search search = builder.build();
        return client.execute(search);
    }
}
