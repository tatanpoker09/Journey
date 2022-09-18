import json

from kafka import KafkaProducer

producer = KafkaProducer(bootstrap_servers='kafka:9092', value_serializer=lambda v: json.dumps(v).encode('utf-8'))


def send(topic, message):
    producer.send(topic, {"msg": message, "source": "telegram"})
