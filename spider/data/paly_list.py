# 歌单实体
class PlayList(object):
    # 歌单名称 歌单描述 歌单id 封面
    def __init__(self, name, des_name, play_list_id, pic_url):
        self.name = name
        self.des_name = des_name
        self.play_list_id = play_list_id
        self.pic_url = pic_url

    def to_dict(self):
        return {"name": self.name, "des_name": self.des_name, "play_list_id": self.play_list_id,
                "pic_url": self.pic_url}
