import logging
from os import getenv

from dotenv import load_dotenv
from telegram.ext import (
    Updater,
    CommandHandler,
    MessageHandler,
    CallbackContext, Filters,
)
from typing import TypeVar

import kafka_connector

url = "http://localhost:7150/parse"

load_dotenv()

token = getenv('TELEGRAM_BOT_TOKEN')
updater = Updater(token=token, use_context=True)
logging.basicConfig(format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
                    level=logging.INFO)
dispatcher = updater.dispatcher
RT = TypeVar('RT')


def start(update, context):
    context.bot.send_message(chat_id=update.effective_chat.id, text="Hello, I'm Journey. What can I do for you?")


def telegram_message_received(update: Updater, context: CallbackContext) -> RT:
    msg = update.message.text
    context.bot.send_message(chat_id=update.effective_chat.id, text=f"Sending '{msg}' to journey_nlp over kafka.")
    kafka_connector.send('journey_nlp.incoming_messages', msg)
    return 200


message_handler = MessageHandler(Filters.text & (~Filters.command), telegram_message_received)
dispatcher.add_handler(message_handler)
start_handler = CommandHandler('start', start)
dispatcher.add_handler(start_handler)

updater.start_polling()
