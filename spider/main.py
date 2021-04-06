import asyncio

import aiohttp
from aiohttp import web
from service import neteaseMusicService

routes = web.RouteTableDef()


# 先进行登陆
async def main():
    timeout = aiohttp.ClientTimeout(total=10)
    async with aiohttp.ClientSession(timeout=timeout) as session:
        neteaseMusicService.session = session
        await neteaseMusicService.login('17302212273', 'testlogin')
        return session


session = asyncio.get_event_loop().run_until_complete(main())
cookie_jar = session.cookie_jar


# 搜索歌曲 返回歌曲实体集合
@routes.get('/search/{name}')
async def search(request):
    timeout = aiohttp.ClientTimeout(total=10)
    async with aiohttp.ClientSession(timeout=timeout) as session:
        neteaseMusicService.session = session
        netease_result = await neteaseMusicService.search(request.match_info['name'])
        return web.json_response([obj.to_dict() for obj in netease_result])


# 获取排行榜歌曲
@routes.get('/top/list/{id}')
async def top_list(request):
    timeout = aiohttp.ClientTimeout(total=10)
    async with aiohttp.ClientSession(timeout=timeout, cookie_jar=cookie_jar) as session:
        neteaseMusicService.session = session
        netease_result = await neteaseMusicService.find_top_list(request.match_info['id'])
        return web.json_response([obj.to_dict() for obj in netease_result])


# 跟据歌曲id获取歌曲
@routes.get('/song/link/{id}')
async def top_list(request):
    timeout = aiohttp.ClientTimeout(total=10)
    async with aiohttp.ClientSession(timeout=timeout) as session:
        neteaseMusicService.session = session
        netease_result = await neteaseMusicService.find_song_link(request.match_info['id'])
        return web.json_response(netease_result)


# 每日歌单
@routes.get('/recommend/playList')
async def recommend_play_list(request):
    timeout = aiohttp.ClientTimeout(total=10)
    async with aiohttp.ClientSession(timeout=timeout, cookie_jar=cookie_jar) as session:
        neteaseMusicService.session = session
        netease_result = await neteaseMusicService.commend_play_list()
        return web.json_response([obj.to_dict() for obj in netease_result])


# 每日歌曲推荐
@routes.get('/recommend/songs')
async def recommend_songs(request):
    timeout = aiohttp.ClientTimeout(total=10)
    async with aiohttp.ClientSession(timeout=timeout, cookie_jar=cookie_jar) as session:
        neteaseMusicService.session = session
        netease_result = await neteaseMusicService.commend_songs()
        return web.json_response([obj.to_dict() for obj in netease_result])


# 获取歌单详情
@routes.get('/playlist/detail/{id}')
async def recommend_songs(request):
    timeout = aiohttp.ClientTimeout(total=10)
    async with aiohttp.ClientSession(timeout=timeout, cookie_jar=cookie_jar) as session:
        neteaseMusicService.session = session
        netease_result = await neteaseMusicService.play_list_detail(request.match_info['id'])
        return web.json_response([obj.to_dict() for obj in netease_result])


app = web.Application()
app.add_routes(routes)
web.run_app(app)
