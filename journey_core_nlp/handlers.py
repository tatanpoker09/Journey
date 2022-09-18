from root import app


@app.handle(intent='greet')
def welcome(request, responder):
    try:
        responder.slots['name'] = request.context['name']
        prefix = 'Hello, {name}. '
    except KeyError:
        prefix = 'Hello. '
    responder.reply(f'{prefix}What can I help you with today?')
    responder.listen()


@app.handle(intent='play_song')
def play_song(request, responder):
    song_name = next((e for e in request.entities if e['type'] == 'song_name'), None)
    print(song_name['text'])
    responder.reply('Playing song...'+song_name['text'])
    responder.listen()


@app.handle(default=True)
def default(request, responder):
    """This is a default handler."""
    responder.reply('Hello there!')


@app.handle(intent='create_new_project')
def create_new_project(request, responder):
    active_programming_language = None
    programming_language_entity = next((e for e in request.entities if e['type'] == 'programming_language'), None)
    if programming_language_entity:
        try:
            programming_languages = app.question_answerer.get(index='programming_languages',
                                                              id=programming_language_entity['value']['id'])
        except TypeError:
            # failed to resolve entity
            programming_languages = app.question_answerer.get(index='programming_languages',
                                                              store_name=programming_language_entity['text'])
        try:
            active_programming_language = programming_languages[0]
            responder.frame['target_language'] = active_programming_language
        except IndexError:
            # No active store... continue
            pass
    elif 'target_language' in request.frame:
        active_store = request.frame['target_language']

    if active_programming_language:
        responder.slots['language_name'] = active_programming_language['language_name']
        responder.slots['context'] = active_programming_language['context']
        responder.reply('Creating new {language_name} project...')
        return

    responder.reply('In what language/framework would you like to create your project?')
    responder.listen()


@app.handle(intent='exit')
def say_goodbye(request, responder):
    responder.reply(['Bye', 'Goodbye', 'Have a nice day.'])
