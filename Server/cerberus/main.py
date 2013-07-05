import os

import webapp2
import jinja2
from controllers.HttpTableHandler import HttpTableHandler
from controllers.ViewTableHandler import ViewTableHandler

_jinja = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'])

app = webapp2.WSGIApplication([
    ('/', ViewTableHandler),
    ('/get', HttpTableHandler),
    ('/post', HttpTableHandler)
], debug=False)


def render(template, values):
    return _jinja.get_template(template).render(values)