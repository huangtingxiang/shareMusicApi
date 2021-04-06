# 歌曲实体
class Song(object):
    # 歌曲名称 歌手 专辑 歌曲url 歌曲图片url
    def __init__(self, name, artist, album, song_url, pic_url, origin, song_id, album_id):
        self.name = name
        self.artist = artist
        self.album = album
        self.song_url = song_url
        self.pic_url = pic_url
        self.origin = origin
        self.song_id = song_id
        self.album_id = album_id

    def to_dict(self):
        return {"name": self.name, "artist": self.artist, "album": self.album, "song_url": self.song_url,
                "pic_url": self.pic_url, "origin": self.origin, "song_id": self.song_id, "album_id": self.album_id}
