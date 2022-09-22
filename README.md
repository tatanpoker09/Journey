
# Journey
Journey is a NLP-Powered Developer assistant

While the project still doesn't have an NLP engine, there are ideas on using either [Rhino](https://picovoice.ai/platform/rhino/) speech to intent or [rasa](https://rasa.com/). Contributions on any of these are welcome! 
This projects aims to assist a people with the most powerful tools, in their day to day life. 
Using a any possible interface, such as telegram, you can use Journey to integrate with your day-to-day utilities, even your own PC. 
Both human input interfaces (called channels) output interfaces (called integrations) can be extended through scriptable templates in the **channels** and **integrations** folders respectively.

# How to run the project?
- First make sure you have [docker](https://docs.docker.com/get-docker/) and [docker-compose](https://docs.docker.com/compose/install/linux/#install-using-the-repository) installed.

- Once that's set, you can clone the repo and go into the directory with:
```shell
git clone https://github.com/tatanpoker09/Journey.git
cd Journey
```

- Once that is downloaded you will want to set up 2 telegram accounts, one for the telegram channel and another one for the telegram integration.
You can do this by going into telegram and talking to https://t.me/BotFather. If lost you can follow [this guide](https://sendpulse.com/knowledge-base/chatbot/telegram/create-telegram-chatbot).
As long as you get 2 different tokens you're good.

- Go into channels/JourneyTelegramBot and rename .env.example to .env, afterwards you can replace <Telegram bot token> with one of the tokens you got in the previous step.
- Go into integrations/TelegramSenderIntegration and rename .env.example to .env, afterwards you can replace <Telegram bot token> with the other token you got.

- Go to journey_core_nlp/main.py line 21 and replace the chat_id with your own telegram chat_id. To find this you can use [this guide](https://www.alphr.com/find-chat-id-telegram/)

Now that everything is ready just run the following commands (make sure you're in the root of the Journey project):
```shell
    docker-compose build
    docker-compose up
```

- Feel free to talk to the bot and say 'message myself on telegram' (without the quotes). You should get a reply in your second bot!




## Planned features:
- Speech to text channel using [Porcupine](https://github.com/Picovoice/porcupine) and [Vosk](https://alphacephei.com/vosk/) or other cloud solutions.
- NLP Engine with [Rhino](https://picovoice.ai/platform/rhino/) and/or [rasa](https://rasa.com/) (customizable as to which solution is being used)
- Visual GUI configuration through a frontend (configurate things like the speech to text engine, the project templates installed, the language in use or the modules installed).
- An integration API to build your own integrations. 
- A channel API to build your own channels.
- Capabilities to order pizza(?) through the Journey assistant
- Extend the Journey assistant for trello-management (create new cards/move them when done?)
- Extend the Journey assistant as a music player?
- Connect the Journey assistant to cortana
- Connect the Journey assistant to Alexa
- Easily load in new modules to the Journey assistant from a package-manager.

# Project Architecture:
![Project Architecture](/../main/docs/img/journey-project-architecture.png)

# How to contribute?
If you want to contribute in some of the features listed above, you can send in a pull request for it! If you're not sure where to start, just open a new issue and we'll point you in the right direction.
