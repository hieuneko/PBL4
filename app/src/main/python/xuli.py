import librosa   #for audio processing
import numpy as np
from os.path import dirname, join
from keras.models import Model, load_model

def predict(src):
    filename = join(dirname(__file__),"best_model.hdf5")
    model = load_model(filename)
    samples, sample_rate = librosa.load(src, sr = 8000)
    result = np.zeros((8000,))
    if result.size > samples.size:
        result[:samples.size] = samples
    else:
        result = samples[:result.size]
    classes = ['app', 'gmail', 'image', 'mess', 'phone']
    prob=model.predict(result.reshape(1,8000,1))
    index=np.argmax(prob[0])
    return classes[index]