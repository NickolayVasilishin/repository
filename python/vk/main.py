from settings import token, my_id, api_v, max_workers, delay, deep

"""https://oauth.vk.com/blank.html#access_token=554b28b1190527b589e1ab9b0b29d66a2c3bd8e9a9cb315061bd9a12e067219744fff9e2d4b56cda4b2fb&expires_in=0&user_id=2747415"""
class VkException(Exception):
    __init__(self, message):
        self.message = message

        def __str__(self):
            return self.message

class VkFriends():

    def base_info(self, ids):
        r = requests.get(self.request_url('users.get', 'user_ids=%s&fields=photo' % (','.join(map(str,ids))))).json()
        if 'error' in r.keys():
            raise VkException('Error message: %s. Error code: %s' % (r['error']['errpr_msg'], r['error']['error_code']))
        r = r['response']
        if 'deactivated' in r[0].keys():
            raise VkException("User deactivated")
        return r

    
    def friends(self, id):
        r = requests.get(self.request_url('friends.get', 'user_id=%s&fields=uid,first_name,last_name,photo' % id)).json()['response']
        return {item['id']: item for item in r['items']}
