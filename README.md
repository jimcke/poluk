# TRG ASSIGMENT
###### _by Alexandru Georgescu_  

---

#### Usage
First step is to build the project:  
```shell script
sbt clean package
```
Start the docker services   
```shell script
docker-compose up -d
```  
In order to run the spark job execute:  
```shell script
docker exec -it trg_sparkmaster spark-submit \
  --class com.trg.assignment.Main \
  --master spark://spark:7077 \
  --deploy-mode client \
  --jars "work/lib/elasticsearch-hadoop-7.5.2.jar" \
  --conf spark.es.nodes=elasticsearch \
  --conf spark.es.port=9200 \
  work/trg_2.11-0.1.jar 
```
If you change something in the project, rebuild and then restart master and workers containers.  
```shell script
docker-compose restart spark spark-worker-1 spark-worker-2
```
---

#### Links 
* [Spark Master Server](http://localhost:8080)
* [Elasticsearch](http://localhost:9200)
* [Kibana UI](http://localhost:5601)

---

#### Filter examples for Elasticsearch
```
GET /_search
{
    "query": {
        "match": {
          "districtName" : "avon and somerset"
        }
    }
}
```
