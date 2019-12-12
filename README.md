# 2019_group_04_s3224708_s2344076_s2723034

# Making a replica set from mongodb:
1. Connect to one of the mongo instances: docker exec -it mongo1 mongo
2. Paste the following:

rs.initiate(
  {
    _id : 'rs0',
    members: [
      { _id : 0, host : "mongo1:27017" },
      { _id : 1, host : "mongo2:27017" },
      { _id : 2, host : "mongo3:27017" }
    ]
  }
)

3. Success!

# Creating the SES Keystore
1. Connect to Cassandra: docker exec -it cassandra1 cqlsh
2. Paste the following:

CREATE KEYSPACE ses
  WITH REPLICATION = { 
   'class' : 'SimpleStrategy', 
   'replication_factor' : 3 
  };

  3. Success!

  # Exporting from Kubernetes
  for n in $(kubectl get -o=name configmaps,ingress,services,deployments,statefulsets,jobs)
do
    mkdir -p $(dirname $n)
    kubectl get -o=yaml --export $n > $n.yaml
done