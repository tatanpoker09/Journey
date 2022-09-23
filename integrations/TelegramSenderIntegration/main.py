import json
import logging
import threading

from dotenv import load_dotenv
from os import getenv
from typing import TypeVar

from kafka import KafkaConsumer
from telegram.ext import (
    Updater,
    CommandHandler,
)

load_dotenv()

token = getenv('TELEGRAM_BOT_TOKEN')
updater = Updater(token=token, use_context=True)
logging.basicConfig(format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
                    level=logging.INFO)
dispatcher = updater.dispatcher
RT = TypeVar('RT')


def start(update, context):
    context.bot.send_message(chat_id=update.effective_chat.id, text="Hello, I'm the integration!")


def send_message(bot, chat_id, message):
    print('sending message')
    bot.send_message(chat_id=chat_id, text=message)


def handle_message(message):
    if message.value:
        value = json.loads(message.value)
        print(value)
        if value['intent'] == 'telegram_message':
            entities = value['entities']
            if 'entity' in entities[0]:
                entity = entities[0]['entity']
                if 'attributes' in entity:
                    chat_id = entity['attributes']['chat_id']
                    send_message(updater.bot, chat_id, "We made a whole trip!")


class UpdaterPolling(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)

    def run(self):
        start_handler = CommandHandler('start', start)
        dispatcher.add_handler(start_handler)
        updater.start_polling()


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
        consumer.subscribe(['telegram_sender.telegram_message'])
        print("Starting to consume messages")
        while not self.stop_event.is_set():
            for message in consumer:
                handle_message(message)
                if self.stop_event.is_set():
                    break

        consumer.close()


logging.basicConfig(format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
                    level=logging.INFO)
print("creating consumer")


consumer = Consumer()
consumer.start()
poller = UpdaterPolling()
poller.start()
consumer.join()
poller.join()
