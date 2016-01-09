# TwitterStreamProcessing
Twitter Stream Data.

This Application use 
- java 1.8
- Kafka 0.8.2

Construct Kafka Server.  
See also:[Apache Kafka](http://kafka.apache.org/)  

## Twitter Stream Producer
This Application is Kafka Producer for Twitter Stream Data.

### How to Use
#### Preparation

create config file
```
$ cat producer_config.properties
# kafka configuration
topic=<produce topic name>
bootstrap.servers=<kafka host>:<kafka port>
compression.type=gzip
key.serializer=org.apache.kafka.common.serialization.StringSerializer
value.serializer=org.apache.kafka.common.serialization.StringSerializer

# Twitter Streaming API configuration
consumerKey=************************
consumerSecret=************************
accessToken=************************
secretToken=************************
```

#### Execution

**Application run**
```
$ java -cp TwitterStreamProcessing-1.1-SNAPSHOT.jar fuji.twitter.stream.producer.App -c producer_config.properties -l ja
```

**Option**
```
Got Exception: Option "-c (--config)" is required
 -c (--config) <config> : configuration file
 -h (--help)            : print usage message and exit (default: true)
 -l (--lang) <lang>     : filter lang
```

## Twitter Stream Fetcher
This Application consume twitter data from Kafka and store this data to file.

### How to Use
#### Preparation

create config file
```
$ cat fetcher_config.properties
zookeeper.connect=<zookeeper host>:<zookeeper port>
topic=<kafka topic name>
serializer.class=kafka.serializer.StringEncoder
auto.offset.reset=largest

## Kafka Consumer ##
group.id=<consuemr-group>
partition=<partition num>
```

#### Execution

**Application run**
```
$ java -cp TwitterStreamProcessing-1.1-SNAPSHOT.jar fuji.twitter.stream.consumer.TwitterStreamStoreApp -c fetcher_config.properties -p /tmp
```

**Option**
```
Got Exception: Option "-c (--config)" is required
 -c (--config) <config> : configuration file
 -h (--help)            : print usage message and exit (default: true)
 -p (--path) <path>     : store path
```
