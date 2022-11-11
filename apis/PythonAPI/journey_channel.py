import json
import threading

from kafka import KafkaConsumer, KafkaProducer

JOURNEY_NLP_TOPIC = 'journey_nlp.incoming_messages'
JOURNEY_BACKEND_TOPIC = 'journey_central.incoming_messages'
KAFKA_BROKER_URL = 'kafka:9092'


class JourneyChannel:
    def __init__(self, name):
        self.name = name
        self.consumer = None
        self.producer = None
        self.stop_event = threading.Event()

    def stop(self):
        self.stop_event.set()

    def on_message_received(self, kafka_topic):
        print("consuming from ", kafka_topic)
        if not self.consumer:
            self.consumer = KafkaConsumer(bootstrap_servers='kafka:9092',
                                          auto_offset_reset='earliest',
                                          consumer_timeout_ms=1000)
        kafka_topic = self.name + '.' + kafka_topic
        print("consuming from 2", kafka_topic)
        self.consumer.subscribe([kafka_topic])

        def handler(func):
            runner = threading.Thread(target=handle_thread, args=(func,))
            runner.start()

        def handle_thread(func):
            print("consuming from 3", kafka_topic)
            while not self.stop_event.is_set():
                for message in self.consumer:
                    print(message.value)
                    func(json.loads(message.value))
                    if self.stop_event.is_set():
                        break

            self.consumer.close()

        return handler

    def send_message_to_nlp(self, msg):
        if not self.producer:
            self._initialize_kafka_producer()
        self.producer.send(JOURNEY_NLP_TOPIC, {"message": msg, "channel": self.name})

    def send_message_to_backend(self, msg):
        if not self.producer:
            self._initialize_kafka_producer()
        self.producer.send(JOURNEY_BACKEND_TOPIC, {"message": msg, "channel": self.name})

    def _initialize_kafka_producer(self):
        self.producer = KafkaProducer(bootstrap_servers=KAFKA_BROKER_URL,
                                      value_serializer=lambda v: json.dumps(v).encode('utf-8'))
