import json
import logging
import threading

from kafka import KafkaConsumer, KafkaProducer


class NLPResponse:
    def __init__(self, intent, entities):
        self.intent = intent
        self.entities = entities


producer = KafkaProducer(bootstrap_servers='kafka:9092', value_serializer=lambda v: json.dumps(v).encode('utf-8'))


def add_knowledge_base(intent, entities):
    if intent == "telegram_message":
        for entity in entities:
            if entity['name'] == "Sweta":
                entity['chat_id'] = "5223390622"
    return entities


def nlp_parser(message):
    if message.value:
        value = json.loads(message.value)
        msg = value['msg']
        if msg == 'message telegram sweta':
            # send kafka thing to journey_central
            entities = [{"name": "Sweta", "type": "person"}]
            intent = "telegram_message"
            entities = add_knowledge_base(intent, entities)
            producer.send('journey_central.incoming_messages', {"msg": msg, "intent": intent, "entities": entities})
            print("sent message to journey_central")


class Consumer(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)
        self.stop_event = threading.Event()

    def stop(self):
        self.stop_event.set()

    def run(self):
        consumer = KafkaConsumer(bootstrap_servers='kafka:9092',
                                 auto_offset_reset='earliest',
                                 consumer_timeout_ms=1000)
        consumer.subscribe(['journey_nlp.incoming_messages'])
        print("Starting to consume messages")
        while not self.stop_event.is_set():
            for message in consumer:
                nlp_parser(message)
                if self.stop_event.is_set():
                    break

        consumer.close()


logging.basicConfig(format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
                    level=logging.INFO)
print("creating consumer")
consumer = Consumer()
consumer.start()
consumer.join()
print("stopping consuming.")
