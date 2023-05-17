# CoreConsumer Camel

Core consumer implementation using Apache Camel

## Getting Started

To get started using this starter, add the following dependency:

For using asychronous request integration, use the camel-async-consumer-spring-boot-starter library/ dependancy
```xml
<dependencies>
    <dependency>
        <groupId>com.juliuskrah</groupId>
        <artifactId>camel-async-consumer-spring-boot-starter</artifactId>
        <version>LATEST_VERSION</version>
    </dependency>
    <!-- ... -->
</dependencies>

```

For using sychronous request integration, use the camel-async-consumer-spring-boot-starter library/ dependancy
```xml
<dependencies>
    <dependency>
        <groupId>com.juliuskrah</groupId>
        <artifactId>camel-sync-consumer-spring-boot-starter</artifactId>
        <version>LATEST_VERSION</version>
    </dependency>
    <!-- ... -->
</dependencies>

```

A set of utilities for camel consumers such as REST, SOAP and others available, add the following dependancy

```xml

<dependencies>
    <dependency>
        <groupId>com.juliuskrah</groupId>
        <artifactId>camel-utils-consumer-spring-boot-starter</artifactId>
        <version>LATEST_VERSION</version>
    </dependency>
    <!-- ... -->
</dependencies>

```


The following Spring Boot properties are available to you:

| Property                                     | Default Value    | Description     |
| :------------------------------------------- | :--------------: | --------------: |
| `camel.opentracing.enabled`                  | `false`          | Whether to enable tracing or not |
| `camel.component.rabbitmq.hostname`          | `localhost`      | Sets the hostname for your rabbit instance |
| `camel.component.rabbitmq.port-number`       | `5672`           | Sets the port for your rabbit instance |
| `camel.component.rabbitmq.username`          | `guest`          | Sets the username for your rabbit instance |
| `camel.component.rabbitmq.password`          | `guest`          | Sets the password for your rabbit instance |
| `camel.component.rabbitmq.vhost`             |                  | Sets the vhost for your rabbit instance |
| `camel.component.rabbitmq.auto-delete`       | `false`          |                 |
| `camel.component.rabbitmq.thread-pool-size`  | `10`             | This setting allows you to set that number of threads |
| `camel.component.http.connect-timeout`       |                  |                 |
| `camel.component.http.connections-per-route` |                  |                 |
| `camel.component.http.max-total-connections` |                  |                 |
| `camel.component.http.socket-timeout`        |                  |                 |
| `consumer.rabbit.request.queue-name`         |                  | Indicate your request queue |
| `consumer.rabbit.request.exchange-name`      |                  | Indicate your exchange name |
| `consumer.rabbit.request.exchange-type`      |                  | Indicate your exhange type e.g. `topic` |
| `consumer.rabbit.request.concurrent-consumers`| `2`             | Number of concurrent consumers when consuming from broker |
| `consumer.rabbit.acknowledgement.queue-name` |                  |                 |
| `consumer.rabbit.acknowledgement.exchange-name`|                |                 |
| `consumer.rest.host-port`                    | `localhost:8080` | Hostname and Port of external merchant API |
| `consumer.rest.uri-template`                 | `/`              | Uri template of the external merchant API |
| `consumer.tracer.service-name`               |                  | The name of the service used for tracing |
| `consumer.tracer.agent-host`                 | `localhost`      | The host of your running jaeger-agent. In a Kubernentes deployment, this is mostly a sidecar container deployed on the same pod |
| `consumer.tracer.agent-port`                 | `6831`           | The port of your running jaeger-agent |

## Developing

Use the included `docker-compose.yml` file to startup a `rabbitmq` instance.

```bash
> docker-compose -f ./.devcontainer/docker-compose.yml up -d
```

Alternatively if you have the [Remote Container](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers)
extension enabled for VS Code, you can `Remote-Containers: Reopen in Container`.

From the VS Code `explorer` scroll down to the `Spring-Boot Dashboard` and click `run`

## Accessing RabbitMQ

Visit rabbitmq at [localhost:15672](http://localhost:15672) and login with username `guest` with password `guest`.

## Payload

```json
{  
   "isTokenService":1,
   "invoiceNumber ":"",
   "merchantPaymentID":123,
   "merchantPaymentAmount":23.00,
   "merchantPaymentMSISDN":"254700210076",
   "merchantPaymentAccountNumber":"ACC",
   "serviceUrl":"http://localhost:8000",
   "protocol":"JSON",
   "serviceID":123,
   "merchantPaymentCustomerName":"Samuel Ndara",
   "merchantPaymentDate":"2018-12-23 12:10:10",
   "merchantPaymentCurrencyCode":"KES",
   "autoAcknowledgePayment":0,
   "apiFunctionName":"dstv.pay",
   "apiPortNumber":8080,
   "sslEnabled":0,
   "wsdlFile ":"",
   "apiUserName":"mtumizi",
   "apiPassword":"password",
   "sslCertificatePath ":"",
   "serviceCode":"DSTVKE",
   "narration":"Paid for stuff",
   "paymentMode ":[  
      "MOMO"
   ],
   "payerClientCode":[  
      "BBBW",
      "SAFKE",
      "DTB"
   ],
   "hubCreationDate":"2018-11-08 09:52:03",
   "numberOfSends":0,
   "lastSend":"",
   "merchantPaymentStatus":139,
   "payerTransactionID":[  
      "PAYERTRX123",
      "PAYERTRX124",
      "PAYERTRX125"
   ],
   "requestOriginIDs":[  
      1,
      2,
      3
   ],
   "extraData": { 
      "12": { 
         "isCombined":"false"
      }
   },
   "nextSend":"",
   "firstSend":"",
   "processorClass":"AIRTEL.php"
}
```
