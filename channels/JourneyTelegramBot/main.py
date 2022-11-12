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
    print(message)
    if 'sender' in message:
        sender = message['sender']
    else:
        sender = None
    if message['msg']:
        msgs = message['msg']
    else:
        msgs = ["No message"]
    if sender and not sender == "undefined":
        for msg in msgs:
            if 'text' in msg:
                text = msg['text']
                updater.bot.send_message(chat_id=sender, text=text)
            elif 'image' in msg:
                image = msg['image']
                updater.bot.send_message(chat_id=sender, text=image)


def start(update: Update, context: CallbackContext) -> RT:
    return context.bot.send_message(chat_id=update.effective_chat.id, text="Hello, I'm Journey. What can I do for you?")


def telegram_message_received(update: Update, context: CallbackContext) -> RT:
    msg = update.message.text
    context.bot.send_message(chat_id=update.effective_chat.id, text=f"Sending '{msg}' to journey_nlp over kafka.")
    channel.send_message_to_nlp({"msg": msg, "sender": update.effective_chat.id})
    return 200


load_dotenv()
token = getenv('TELEGRAM_BOT_TOKEN')
updater = Updater(token=token, use_context=True)
logging.basicConfig(format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
                    level=logging.INFO)
logging.debug("we are starting the telegram bot.")
dispatcher = updater.dispatcher

message_handler = MessageHandler(Filters.text & (~Filters.command), telegram_message_received)
dispatcher.add_handler(message_handler)
start_handler = CommandHandler('start', start)
dispatcher.add_handler(start_handler)

updater.start_polling()
