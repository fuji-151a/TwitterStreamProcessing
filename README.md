# TwitterStreamProcessing
Kafka Producer for Twitter Stream Data

## Twitter Stream Producer
This Application is Kafka Producer for Twitter Stream Data.
### How to Use
#### Preparation
This Application use 
- java 1.8
- Kafka 0.8.2

Construct Kafka Server.  
See also:[Apache Kafka](http://kafka.apache.org/)  

create config file
```
$ cat config.properties
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
$ java -cp TwitterStreamProcessing-1.1-SNAPSHOT.jar fuji.twitter.stream.producer.App -c config.properties -l ja
```

**Option**
```
Got Exception: Option "-c (--config)" is required
 -c (--config) <config> : configuration file
 -h (--help)            : print usage message and exit (default: true)
 -l (--lang) <lang>     : filter lang
```
