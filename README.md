
# Journey
Journey is a NLP-Powered Developer assistant

While the project still doesn't have an NLP engine, there are ideas on using either [Rhino](https://picovoice.ai/platform/rhino/) speech to intent or [rasa](https://rasa.com/). Contributions on any of these are welcome! 
This projects aims to assist a people with the most powerful tools, in their day to day life. 
Using a any possible interface, such as telegram, you can use Journey to integrate with your day-to-day utilities, even your own PC. 
Both human input interfaces (called channels) output interfaces (called integrations) can be extended through scriptable templates in the **channels** and **integrations** folders respectively.

# How to run the project?





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
