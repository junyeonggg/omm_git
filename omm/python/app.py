from flask import Flask,request, render_template
import pandas as pd
import numpy as np


app = Flask(__name__)


@app.route("/") # 어떤 요청을 받을지 정의
def hello_flask():
    return "hello"

