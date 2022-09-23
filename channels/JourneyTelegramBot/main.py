import logging
from os import getenv

from dotenv import load_dotenv
from telegram import Update
from telegram.bot import RT
from telegram.ext import MessageHandler, Filters, Updater, CommandHandler, CallbackContext

from journey_channel import JourneyChannel

channel = JourneyChannel('telegram_sender')


@channel.on_message_received("incoming_message")
def read_incoming_message(message):
    print("Received message", message)


def start(update: Update, context: CallbackContext) -> RT:
    context.bot.send_message(chat_id=update.effective_chat.id, text="Hello, I'm Journey. What can I do for you?")


def telegram_message_received(update: Update, context: CallbackContext) -> RT:
    msg = update.message.text
    context.bot.send_message(chat_id=update.effective_chat.id, text=f"Sending '{msg}' to journey_nlp over kafka.")
    channel.send_message_to_nlp(msg)
    return 200


load_dotenv()
token = getenv('TELEGRAM_BOT_TOKEN')
updater = Updater(token=token, use_context=True)
logging.basicConfig(format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
                    level=logging.INFO)
dispatcher = updater.dispatcher

message_handler = MessageHandler(Filters.text & (~Filters.command), telegram_message_received)
dispatcher.add_handler(message_handler)
start_handler = CommandHandler('start', start)
dispatcher.add_handler(start_handler)

updater.start_polling()
