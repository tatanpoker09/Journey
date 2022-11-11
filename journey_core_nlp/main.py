import json
import logging
import requests
import threading

from kafka import KafkaConsumer, KafkaProducer


class NLPResponse:
    def __init__(self, intent, entities):
        self.intent = intent
        self.entities = entities


try:
    producer = KafkaProducer(bootstrap_servers='kafka:9092', value_serializer=lambda v: json.dumps(v).encode('utf-8'))
except:
    print("no producer")


# http://localhost:5005/model/parse
# http://localhost:5005/webhooks/rest/webhook
def get_nlp_analysis(msg):
    response = requests.post('http://rasa:5005/model/parse',
                             data=json.dumps({"text": msg}))
    return response.json()


def get_nlp_response(sender, msg):
    response = requests.post('http://rasa:5005/webhooks/rest/webhook',
                             data=json.dumps({"sender": sender, "message": msg}))
    return response.json()


def nlp_parser(message):
    value = json.loads(message)

    if 'msg' in value['message']:
        msg = value['message']['msg']
        channel = value['channel']
        sender = value['message']['sender']
    else:
        channel = value['channel']
        sender = "undefined"
        msg = value['message']
    # send kafka thing to journey_central
    nlp_analysis = get_nlp_analysis(msg)

    response = get_nlp_response(channel, msg)
    # we need to send post request to rasa @ /webhooks/rest/webhook
    producer.send(channel+'.incoming_message', {"msg": response, "sender": sender })
    producer.send('journey_central.incoming_messages', {"msg": msg, "intent": nlp_analysis['intent']['name'],
                                                        "entities": nlp_analysis['entities']})
    print("sent message to journey_central")



class Consumer(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)
        self.stop_event = threading.Event()

    def stop(self):
        self.stop_event.set()

    def run(self):
        try:
            consumer = KafkaConsumer(bootstrap_servers='kafka:9092',
                                     auto_offset_reset='earliest',
                                     consumer_timeout_ms=1000)
            consumer.subscribe(['journey_nlp.incoming_messages'])
            print("Starting to consume messages")
            while not self.stop_event.is_set():
                for message in consumer:
                    if message.value:
                        nlp_parser(message.value)
                    if self.stop_event.is_set():
                        break

            consumer.close()
        except Exception as err:
            print(err)
            pass


logging.basicConfig(format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
                    level=logging.INFO)
print("creating consumer")
consumer = Consumer()
consumer.start()
consumer.join()
print("stopping consuming.")
