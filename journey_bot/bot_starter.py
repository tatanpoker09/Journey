from telegram.ext import Updater
from telegram.ext import CommandHandler
import logging

updater = Updater(token='2146936618:AAF9CpQQaOkfpCT4Fi0DNGPbFnAxEa0T_bo', use_context=True)
logging.basicConfig(format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
                    level=logging.INFO)
dispatcher = updater.dispatcher


def start(update, context):
    context.bot.send_message(chat_id=update.effective_chat.id, text="I'm a bot, please talk to me!")


start_handler = CommandHandler('start', start)
dispatcher.add_handler(start_handler)

updater.start_polling()
