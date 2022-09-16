import logging
import os

from dotenv import load_dotenv
from telegram.ext import (
    Updater,
    CommandHandler,
    MessageHandler,
    Filters,
    CallbackContext,
)

from channels.voice import JourneyChannel

url = "http://localhost:7150/parse"

load_dotenv()

updater = Updater(token=os.getenv('BOT_TOKEN'), use_context=True)
logging.basicConfig(format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
                    level=logging.INFO)
dispatcher = updater.dispatcher

bot_channel = JourneyChannel(__name__)


class TelegramBot:
    def __init__(self):
        pass


def start(update, context):
    context.bot.send_message(chat_id=update.effective_chat.id, text="Hello, I'm Journey. What can I do for you?")


def telegram_message_received(update: Updater, context: CallbackContext, channel: JourneyChannel):
    msg = update.message.text
    dialogue_id = channel.process_message(msg)
    session = channel.get_session(dialogue_id)
    session.chat_id = update.effective_chat.id
    session.context = context


@bot_channel.process_message
def process_message(session, message):
    context = session.context
    context.bot.send_message(chat_id=session.chat_id, text=message)


message_handler = MessageHandler(Filters.text, lambda x, y: telegram_message_received(x, y, bot_channel))
dispatcher.add_handler(message_handler)
start_handler = CommandHandler('start', start)
dispatcher.add_handler(start_handler)

updater.start_polling()
