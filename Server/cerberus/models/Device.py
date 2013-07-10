from google.appengine.ext import ndb


class Device(ndb.Model):
    name = ndb.StringProperty(indexed=True)

    @classmethod
    def get_all(cls):
        return cls.query().order(cls.name).fetch()

    @classmethod
    def get_by_name(cls, device_name):
        return cls.query(cls.name == device_name).get()