
# Journey
Journey is a NLP-Powered virtual personal assistant

While the project still doesn't have an NLP engine, there are ideas on using either [Rhino](https://picovoice.ai/platform/rhino/) speech to intent or [rasa](https://rasa.com/). Contributions on any of these are welcome! 
This projects aims to assist people with the most powerful tools, in their day to day life. 
Using any interface, such as Telegram, you can use Journey to integrate with the things you use every day, even your own PC. 
Both human interfaces (called channels) and device interfaces (called integrations) can be extended through scriptable templates. These are found in the **channels** and **integrations** folders respectively.

# How to run the project?
- First make sure you have [docker](https://docs.docker.com/get-docker/) and [docker compose](https://docs.docker.com/compose/install/) installed.

- Once that's set, you can clone the repo and go into the directory with:
```shell
git clone https://github.com/tatanpoker09/Journey.git
cd Journey
```

- Once that is downloaded you will want to set up 2 Telegram accounts, one for the Telegram channel and another one for the Telegram integration.
You can do this by going into Telegram and talking to https://t.me/BotFather. If lost you can follow [this guide](https://sendpulse.com/knowledge-base/chatbot/telegram/create-telegram-chatbot).
As long as you get 2 different tokens you're good.

- Go into channels/JourneyTelegramBot and rename .env.example to .env, afterwards you can replace <Telegram bot token> with one of the tokens you got in the previous step.
- Go into integrations/TelegramSenderIntegration and rename .env.example to .env, afterwards you can replace <Telegram bot token> with the other token you got.

- Go to journey_core_nlp/main.py line 21 and replace the chat_id with your own Telegram chat_id. To find this you can use [this guide](https://www.alphr.com/find-chat-id-telegram/). Also, replace "tatanpoker09" with your own Telegram username.

Now that everything is ready just run the following commands (make sure you're in the root of the Journey project):
```shell
    docker-compose build
    docker-compose up
```

- You're ready to talk to the bot! Say, 'message myself on telegram' (without the quotes). You should get a reply in your second bot!




## Planned features:
- Speech to text channel using [Porcupine](https://github.com/Picovoice/porcupine) and [Vosk](https://alphacephei.com/vosk/) or other cloud solutions.
- NLP Engine with [Rhino](https://picovoice.ai/platform/rhino/) and/or [rasa](https://rasa.com/) (customizable as to which solution is being used)
- Visual GUI configuration for e.g.: the speech to text engine, the channels and integrations installed, or the language in use).
- An integration API to build your own integrations. 
- A channel API to build your own channels.
- Integrations for Pizza (just chat to order pizza), Trello (create new cards/move them when done?), Music Player (queue songs, play/pause)
- Channels for Google Assistant / Alexa (they're already listening to you!)
- Easily load in new channels and integrations to the Journey assistant from a package-manager.

# Project Architecture:
![Project Architecture](/../main/docs/img/journey-project-architecture.png)

# How to contribute?
If you want to contribute in any of the areas listed above, you can send in a pull request for it! If you're not sure where to start, just open a new issue and we'll point you in the right direction 😀.
