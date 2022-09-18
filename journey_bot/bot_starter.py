import os
import mindmeld
from telegram.ext import (
    Updater,
    CommandHandler,
    MessageHandler,
    Filters,
    ConversationHandler,
    CallbackContext,
)
import logging
import requests
from dotenv import load_dotenv

url = "http://localhost:7150/parse"

load_dotenv()

updater = Updater(token=os.getenv('BOT_TOKEN'), use_context=True)
logging.basicConfig(format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
                    level=logging.INFO)
dispatcher = updater.dispatcher


def start(update, context):
    context.bot.send_message(chat_id=update.effective_chat.id, text="Hello, I'm Journey. What can I do for you?")


def message_received(update: Updater, context: CallbackContext):
    msg = update.message.text
    body = {"text": msg}
    response = requests.post(url, json=body)
    print(response.json())
    context.bot.send_message(chat_id=update.effective_chat.id, text=response.json()["directives"][0].payload.text)


message_handler = MessageHandler(Filters.text, message_received)
dispatcher.add_handler(message_handler)

start_handler = CommandHandler('start', start)
dispatcher.add_handler(start_handler)

updater.start_polling()
