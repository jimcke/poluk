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

Before running the spark job go to [kibana dev mode](http://localhost:5601/app/kibana#/dev_tools/console?_g=()) and set type for location
```shell script
PUT /police_uk
{
  "mappings": {
    "properties": {
      "location":    { 
        "type": "geo_point"
      }
    }
  }
}
```
If there are any error you can try to delete the mapped index `DELETE /police_uk`  
Now we can run the spark job.  
In order to run the spark job we should execute:  
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

GET /_search
{
    "query": {
      "match_phrase_prefix": {
        "districtName": "bed"
      }
    }
}

```
