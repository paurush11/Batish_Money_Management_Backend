package com.example.batishMoneyManager.config;

import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;

import javax.net.ssl.SSLContext;

import java.io.IOException;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Configuration
@EnableReactiveElasticsearchRepositories
public class ReactiveElasticsearchConfig extends ElasticsearchConfiguration {
    /**
     * Must be implemented by deriving classes to provide the {@link ClientConfiguration}.
     *
     * @return configuration, must not be {@literal null}
     */


    @Override
    public ClientConfiguration clientConfiguration() {
        Path trustStorePath = Path.of("/Users/paurushbatish/Desktop/ESANDKIB/elasticsearch-8.10.1/truststore.jks");
        String trustStorePassword = "changeit";

        try {
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(trustStorePath.toFile(), trustStorePassword.toCharArray())
                    .build();
            return ClientConfiguration.builder()
                    .connectedTo("localhost:9200").usingSsl(sslContext).withBasicAuth("elastic","shjfMo*Rfz_8wVjVUV94")
                    .build();
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
