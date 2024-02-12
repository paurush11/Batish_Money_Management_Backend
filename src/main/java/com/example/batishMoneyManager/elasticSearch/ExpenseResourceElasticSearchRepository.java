package com.example.batishMoneyManager.elasticSearch;


import com.example.batishMoneyManager.expenseData.ExpenseData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.List;

@EnableElasticsearchRepositories
public interface ExpenseResourceElasticSearchRepository extends ElasticsearchRepository<ExpenseData, String> {
    // Elasticsearch does not use JPA's @Id annotation for query methods, you query directly by property names
    List<ExpenseData> findByName(String name);
    // Assuming Category, PaymentStatus, Frequency, and PaymentMethod are enums or strings that can be indexed by Elasticsearch
    List<ExpenseData> findByCategory(String category);
    List<ExpenseData> findByPaymentStatus(String paymentStatus);
    List<ExpenseData> findByAmountGreaterThan(Double amount);
    List<ExpenseData> findByFrequency(String frequency);
    List<ExpenseData> findByPaymentMethod(String paymentMethod);
    List<ExpenseData> findByRemindersTrue();
    // For pageable results, you can directly use Pageable as an argument

}
