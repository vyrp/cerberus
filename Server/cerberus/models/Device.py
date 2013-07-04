from google.appengine.ext import ndb


class Device(ndb.Model):
    name = ndb.StringProperty(indexed=True)

    @classmethod
    def get_all(cls):
        return cls.query().order(cls.name).fetch()