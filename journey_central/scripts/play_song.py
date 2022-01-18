import urllib.request
import sys

import pafy
import vlc
from pyyoutube import Api
import threading

api = Api(api_key="AIzaSyBl2Uuh-SdIYcSxiTx7B_Mm5TEO8FEbpcY")

output_devices = []


def search_video(keyword):
    response = api.search_by_keywords(q=keyword, search_type=["video"], count=1, limit=1)
    video_id = response.items[0].id.videoId
    return "https://youtube.com/watch?v=" + video_id


def search_output_devices(player):
    mods = player.audio_output_device_enum()
    if mods:
        mod = mods
        while mod:
            mod = mod.contents
            output_devices.append([mod.description, mod.device])
            mod = mod.next


def set_output_device(player, device_name):
    if not output_devices:
        search_output_devices(player)
    device = None

    for elem in output_devices:
        if bytes(device_name) in elem[0]:
            device = elem[1]
    if device:
        player.audio_output_device_set(None, device)


def play_song(url, show_video=False):
    video = pafy.new(url)
    best = video.getbest()
    playurl = best.url
    ins = vlc.Instance("" if show_video else "--no-video")
    player = ins.media_player_new()

    code = urllib.request.urlopen(url).getcode()
    if str(code).startswith('2') or str(code).startswith('3'):
        print('Stream is working')
    else:
        print('Stream is dead')

    Media = ins.media_new(playurl)
    Media.get_mrl()
    player.set_media(Media)
    player.set_hwnd(0)
    player.play()
    player.audio_set_volume(50)

    good_states = ["State.Playing", "State.NothingSpecial", "State.Opening", "State.Paused"]
    while str(player.get_state()) in good_states:
        pass
        # print('Stream is working. Current state = {}'.format(player.get_state()))

    print('Stream is not working. Current state = {}'.format(player.get_state()))
    player.stop()


song_name = sys.argv[1]
show_video = sys.argv[2] == "true" or sys.argv[2] == "True"
print("Song name: {}".format(song_name))
print("Show video: {}".format(show_video))
if song_name:
    url = search_video(song_name)
    thread = threading.Thread(target=play_song, args=(url, show_video))
    thread.start()
